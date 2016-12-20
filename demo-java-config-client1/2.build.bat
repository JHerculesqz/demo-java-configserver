cd %~dp0
call gradlew clean
cd %~dp0

cd %~dp0
call gradlew build && java -jar build/libs/demo-java-docker-springboot-1.0.0.jar
cd %~dp0