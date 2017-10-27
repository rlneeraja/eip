param(
[string]$deploy_hostname
)

Write-Host Deploying to environment hostname `"$deploy_hostname`"...

Invoke-Command -ComputerName $deploy_hostname -ScriptBlock {
    if (Get-Service routing-services -ErrorAction SilentlyContinue)
    {
       E:\services\routing-services.exe stop
    }
}
# Wait for the service to stop if it was running.  This has to be outside of the script block above.
Start-Sleep -m 1000

Copy-Item -Path ../target/EIPRoutingService-*.jar -Destination \\$deploy_hostname\services\routing-services.jar

Copy-Item -Path ./winsw-2.1.2-bin.exe -Destination \\$deploy_hostname\services\routing-services.exe

Copy-Item -Path ./routing-services.xml -Destination \\$deploy_hostname\services\routing-services.xml

Invoke-Command -ComputerName $deploy_hostname -ScriptBlock {
    if (-NOT (Get-Service routing-services -ErrorAction SilentlyContinue))
    {
        E:\services\routing-services.exe install
        sleep 2
    } 
    
    E:\services\routing-services.exe start
}
