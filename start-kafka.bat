@echo off
set JAVA_HOME=C:\Users\mano-13607\.jdks\openjdk-25.0.2
set KAFKA_LOG4J_OPTS=-Dlog4j2.configurationFile=file:E:\Kafka\kafka_2.13-4.2.0\config\log4j2.yaml
set KAFKA_DATA=C:\tmp\kraft-combined-logs

if not exist "%KAFKA_DATA%\meta.properties" (
    echo Kafka data missing or corrupted - reformatting...
    rmdir /s /q "%KAFKA_DATA%" 2>nul
    mkdir "%KAFKA_DATA%"
    E:\Kafka\kafka_2.13-4.2.0\bin\windows\kafka-storage.bat format --standalone -t ZAyifk0bQqqtG95pE-S4MA -c E:\Kafka\kafka_2.13-4.2.0\config\server.properties
)

E:\Kafka\kafka_2.13-4.2.0\bin\windows\kafka-server-start.bat E:\Kafka\kafka_2.13-4.2.0\config\server.properties
