param(
    [switch]$SkipBuild
)

$ErrorActionPreference = "Continue"
$ProjectRoot = $PSScriptRoot
$LogDir = Join-Path $ProjectRoot ".startup-logs"

if (!(Test-Path $LogDir)) { New-Item -ItemType Directory -Path $LogDir -Force | Out-Null }

$Services = @(
    @{ Name = "eureka-server";      Port = 8761; Jar = "eureka-server\target\*.jar";      Order = 1; DependsOn = $null }
    @{ Name = "api-gateway";         Port = 8089; Jar = "api-gateway\target\*.jar";         Order = 2; DependsOn = 8761 }
    @{ Name = "usuarios-service";    Port = 8081; Jar = "usuarios-service\target\*.jar";    Order = 3; DependsOn = 8089 }
    @{ Name = "catalogo-service";    Port = 8082; Jar = "catalogo-service\target\*.jar";    Order = 3; DependsOn = 8089 }
    @{ Name = "tramites-service";    Port = 8083; Jar = "tramites-service\target\*.jar";    Order = 3; DependsOn = 8089 }
    @{ Name = "progreso-service";    Port = 8084; Jar = "progreso-service\target\*.jar";    Order = 3; DependsOn = 8089 }
    @{ Name = "documentos-service";  Port = 8085; Jar = "documentos-service\target\*.jar";  Order = 3; DependsOn = 8089 }
)

function Write-Status($msg, $color = "White") {
    $ts = Get-Date -Format "HH:mm:ss"
    Write-Host "[$ts] " -NoNewline -ForegroundColor DarkGray
    Write-Host $msg -ForegroundColor $color
}

function Test-Port($port, $timeoutMs = 1000) {
    try {
        $tcp = New-Object System.Net.Sockets.TcpClient
        $result = $tcp.BeginConnect("127.0.0.1", $port, $null, $null)
        $success = $result.AsyncWaitHandle.WaitOne($timeoutMs)
        $tcp.Close()
        return $success
    } catch {
        return $false
    }
}

function Wait-ForPort($port, $maxWaitSec = 90) {
    $elapsed = 0
    while ($elapsed -lt $maxWaitSec) {
        if (Test-Port $port) { return $true }
        Start-Sleep -Seconds 2
        $elapsed += 2
        Write-Status "  Esperando puerto $port... ($elapsed s)" DarkYellow
    }
    return $false
}

function Find-Jar($pattern) {
    $files = Get-ChildItem -Path (Join-Path $ProjectRoot $pattern) -ErrorAction SilentlyContinue
    if ($files -and $files.Count -gt 0) {
        return $files[0].FullName
    }
    return $null
}

function Get-ServiceProcess($port) {
    $conns = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    if ($conns) {
        $pids = $conns | Select-Object -ExpandProperty OwningProcess -Unique
        foreach ($pid in $pids) {
            $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
            if ($proc -and $proc.ProcessName -eq "java") {
                return $proc
            }
        }
    }
    return $null
}

# ============================================================
#  BUILD
# ============================================================
if (-not $SkipBuild) {
    Write-Status "========================================" Cyan
    Write-Status "  FASE 0: Construyendo proyecto Maven" Cyan
    Write-Status "========================================" Cyan

    Push-Location $ProjectRoot
    & mvn clean package -DskipTests -q 2>&1 | Out-Null
    $buildOk = $LASTEXITCODE -eq 0
    Pop-Location

    if (-not $buildOk) {
        Write-Status "ERROR: Build de Maven falló. Abortando." Red
        exit 1
    }
    Write-Status "Build completado exitosamente." Green
    Write-Host ""
} else {
    Write-Status "Build saltado (-SkipBuild)." DarkYellow
    Write-Host ""
}

# ============================================================
#  CHECK: Ya hay algo corriendo?
# ============================================================
$anyRunning = $false
foreach ($svc in $Services) {
    if (Test-Port $svc.Port) {
        $anyRunning = $true
        break
    }
}

if ($anyRunning) {
    Write-Status "Ya hay servicios corriendo en algunos puertos." DarkYellow
    Write-Status "Ejecuta .\stop-all.ps1 primero o usa -SkipBuild si solo quieres reiniciar." DarkYellow
    $resp = Read-Host "Continuar de todos modos? (s/n)"
    if ($resp -ne "s") { exit 0 }
    Write-Host ""
}

# ============================================================
#  FASE 1: Eureka Server
# ============================================================
Write-Status "========================================" Cyan
Write-Status "  FASE 1: Eureka Server (puerto 8761)" Cyan
Write-Status "========================================" Cyan

$eureka = $Services | Where-Object { $_.Port -eq 8761 }
$eurekaJar = Find-Jar $eureka.Jar
if (-not $eurekaJar) {
    Write-Status "ERROR: No se encontró JAR para $($eureka.Name)" Red
    exit 1
}

$logOut = Join-Path $LogDir "$($eureka.Name)-out.log"
$logErr = Join-Path $LogDir "$($eureka.Name)-err.log"
Start-Process -FilePath "java" -ArgumentList "-jar", "`"$eurekaJar`"" `
    -WorkingDirectory $ProjectRoot -RedirectStandardOutput $logOut -RedirectStandardError $logErr -NoNewWindow | Out-Null
Write-Status "  $($eureka.Name) lanzado. Esperando..." Green

if (Wait-ForPort 8761 60) {
    Write-Status "  Puerto 8761 LISTO" Green
} else {
    Write-Status "  TIMEOUT: Puerto 8761 no respondió en 60s" Red
    exit 1
}
Write-Host ""

# ============================================================
#  FASE 2: API Gateway
# ============================================================
Write-Status "========================================" Cyan
Write-Status "  FASE 2: API Gateway (puerto 8089)" Cyan
Write-Status "========================================" Cyan

$gw = $Services | Where-Object { $_.Port -eq 8089 }
$gwJar = Find-Jar $gw.Jar
if (-not $gwJar) {
    Write-Status "ERROR: No se encontró JAR para $($gw.Name)" Red
    exit 1
}

$logOut = Join-Path $LogDir "$($gw.Name)-out.log"
$logErr = Join-Path $LogDir "$($gw.Name)-err.log"
Start-Process -FilePath "java" -ArgumentList "-jar", "`"$gwJar`"" `
    -WorkingDirectory $ProjectRoot -RedirectStandardOutput $logOut -RedirectStandardError $logErr -NoNewWindow | Out-Null
Write-Status "  $($gw.Name) lanzado. Esperando..." Green

if (Wait-ForPort 8089 60) {
    Write-Status "  Puerto 8089 LISTO" Green
} else {
    Write-Status "  TIMEOUT: Puerto 8089 no respondió en 60s" Red
    exit 1
}
Write-Host ""

# ============================================================
#  FASE 3: Microservicios (paralelo)
# ============================================================
$microservices = $Services | Where-Object { $_.Order -eq 3 }

Write-Status "========================================" Cyan
Write-Status "  FASE 3: Microservicios (paralelo)" Cyan
Write-Status "========================================" Cyan

foreach ($svc in $microservices) {
    $jar = Find-Jar $svc.Jar
    if (-not $jar) {
        Write-Status "  WARN: No se encontró JAR para $($svc.Name), saltando" DarkYellow
        continue
    }

    $logOut = Join-Path $LogDir "$($svc.Name)-out.log"
    $logErr = Join-Path $LogDir "$($svc.Name)-err.log"
    Start-Process -FilePath "java" -ArgumentList "-jar", "`"$jar`"" `
        -WorkingDirectory $ProjectRoot -RedirectStandardOutput $logOut -RedirectStandardError $logErr -NoNewWindow | Out-Null
    Write-Status "  $($svc.Name) lanzado (puerto $($svc.Port))" Green
}

Write-Host ""
Write-Status "Esperando a que todos los microservicios estén listos..." Cyan

$allReady = $true
foreach ($svc in $microservices) {
    if (Wait-ForPort $svc.Port 90) {
        Write-Status "  Puerto $($svc.Port) LISTO ($($svc.Name))" Green
    } else {
        Write-Status "  TIMEOUT: Puerto $($svc.Port) no respondió ($($svc.Name))" Red
        $allReady = $false
    }
}

# ============================================================
#  FASE 4: Dashboard
# ============================================================
Write-Host ""
Write-Status "========================================" Cyan
Write-Status "           DASHBOARD DE ESTADO" Cyan
Write-Status "========================================" Cyan
Write-Host ""

$allOk = $true
foreach ($svc in $Services) {
    $up = Test-Port $svc.Port
    $proc = Get-ServiceProcess $svc.Port
    $pidStr = if ($proc) { $proc.Id.ToString() } else { "---" }

    if ($up) {
        Write-Host "  " -NoNewline
        Write-Host "[OK]  " -NoNewline -ForegroundColor Green
    } else {
        Write-Host "  " -NoNewline
        Write-Host "[FAIL]" -NoNewline -ForegroundColor Red
        $allOk = $false
    }

    Write-Host "$($svc.Name.PadRight(22))" -NoNewline -ForegroundColor White
    Write-Host "Puerto: $($svc.Port)  " -NoNewline -ForegroundColor DarkGray
    Write-Host "PID: $pidStr" -ForegroundColor DarkGray
}

Write-Host ""
if ($allOk) {
    Write-Status "Todos los servicios están corriendo correctamente." Green
    Write-Status "Gateway: http://localhost:8089/api" White
    Write-Status "Eureka:  http://localhost:8761" White
} else {
    Write-Status "Algunos servicios no están respondiendo. Revisa los logs en:" DarkYellow
    Write-Status "  $LogDir" DarkYellow
}

Write-Host ""
Write-Status "Para detener todo: .\stop-all.ps1" DarkGray