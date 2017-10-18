Copy-Item -Path ../target/EIPRoutingService-*.jar -Destination \\EIP-QA\services\routing-services.jar

Copy-Item -Path ./winsw-2.1.2-bin.exe -Destination \\EIP-QA\services\routing-services.exe

Copy-Item -Path ./routing-services.xml -Destination \\EIP-QA\services\routing-services.xml

Invoke-Command -ComputerName EIP-QA -ScriptBlock {
    if (-NOT (Get-Service routing-services -ErrorAction SilentlyContinue))
    {
        E:\services\routing-services.exe install
        
        E:\services\routing-services.exe start
    }
    else
    {
        E:\services\routing-services.exe restart
    }
}
