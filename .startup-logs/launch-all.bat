@echo off
cd /d C:\TDIS\backend2
echo Starting Eureka...
start "Eureka" /MIN java -jar eureka-server\target\eureka-server-1.0.0.jar
timeout /t 30 /nobreak >nul
echo Starting Gateway...
start "Gateway" /MIN java -jar api-gateway\target\api-gateway-1.0.0.jar
timeout /t 20 /nobreak >nul
echo Starting Microservices...
start "Usuarios" /MIN java -jar usuarios-service\target\usuarios-service-1.0.0.jar
start "Catalogo" /MIN java -jar catalogo-service\target\catalogo-service-1.0.0.jar
start "Tramites" /MIN java -jar tramites-service\target\tramites-service-1.0.0.jar
start "Progreso" /MIN java -jar progreso-service\target\progreso-service-1.0.0.jar
start "Documentos" /MIN java -jar documentos-service\target\documentos-service-1.0.0.jar
echo All services launched.
timeout /t 30 /nobreak >nul
echo Checking ports...
netstat -an | findstr "8081 8082 8083 8084 8085"
