cd %~dp0
call gradlew clean
cd %~dp0

cd %~dp0
call gradlew build buildDocker
cd %~dp0