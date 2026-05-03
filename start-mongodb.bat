@echo off
echo Starting MongoDB...
start "" "E:\MongoDB\bin\mongod.exe" --dbpath "E:\MongoDB\data\db" --logpath "E:\MongoDB\logs\mongod.log" --port 27017
timeout /t 5 /nobreak > nul
echo MongoDB started on port 27017
