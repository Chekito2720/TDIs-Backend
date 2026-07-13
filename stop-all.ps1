$ErrorActionPreference = "Continue"

$Services = @(
    @{ Name = "api-gateway";         Port = 8089 }
    @{ Name = "usuarios-service";    Port = 8081 }
    @{ Name = "catalogo-service";    Port = 8082 }
    @{ Name = "tramites-service";    Port = 8083 }
    @{ Name = "progreso-service";    Port = 8084 }
    @{ Name = "documentos-service";  Port = 8085 }
    @{ Name = "eureka-server";       Port = 8761 }
)

function Write-Status($msg, $color = "White") {
    $ts = Get-Date -Format "HH:mm:ss"
    Write-Host "[$ts] " -NoNewline -ForegroundColor DarkGray
    Write-Host $msg -ForegroundColor $color
}

Write-Host ""
Write-Status "========================================" Cyan
Write-Status "   Deteniendo microservicios (MODO FORZADO)" Cyan
Write-Status "========================================" Cyan
Write-Host ""

# Verificación de permisos de Administrador
$isAdmin = ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Status "⚠️ ADVERTENCIA: No estás ejecutando PowerShell como Administrador." DarkYellow
    Write-Status "Es muy probable que el script no tenga permisos para matar los procesos." DarkYellow
    Write-Host ""
}

$killed = 0

foreach ($svc in $Services) {
    $pidsToKill = @()
    
    # 1. Encontrar CUALQUIER proceso escuchando en el puerto
    try {
        $conns = Get-NetTCPConnection -LocalPort $svc.Port -State Listen -ErrorAction SilentlyContinue
        if ($conns) {
            $pidsToKill = $conns | Select-Object -ExpandProperty OwningProcess -Unique
        }
    } catch {}

    if ($pidsToKill.Count -gt 0) {
        # Cambiamos $pid por $processId para evitar el conflicto con la variable reservada
        foreach ($processId in $pidsToKill) {
            # Ignorar procesos core del sistema (0 = Idle, 4 = System) por seguridad
            if ($processId -eq 0 -or $processId -eq 4) { continue }
            
            Write-Status "  Matando proceso (PID: $processId) en puerto $($svc.Port)..." DarkYellow
            try {
                Stop-Process -Id $processId -Force -ErrorAction Stop
                Write-Status "  $($svc.Name) detenido exitosamente" Green
                $killed++
            } catch {
                Write-Status "  ❌ Error al detener PID $processId (Faltan permisos): $_" Red
            }
        }
    } else {
        Write-Status "  $($svc.Name) no está corriendo (puerto $($svc.Port))" DarkGray
    }
}

Write-Host ""
Start-Sleep -Seconds 2

# Verificación final
$stillRunning = 0
foreach ($svc in $Services) {
    try {
        $conns = Get-NetTCPConnection -LocalPort $svc.Port -State Listen -ErrorAction SilentlyContinue
        if ($conns) {
            $stillRunning++
            Write-Status "  ⚠️ ADVERTENCIA: $($svc.Name) (puerto $($svc.Port)) aún activo" Red
        }
    } catch {}
}

Write-Host ""
if ($stillRunning -eq 0) {
    Write-Status "Todos los servicios detenidos. ($killed procesos terminados)" Green
} else {
    Write-Status "$stillRunning servicio(s) aún activo(s). ¡CIERRA LA CONSOLA Y ÁBRELA COMO ADMINISTRADOR!" Red
}