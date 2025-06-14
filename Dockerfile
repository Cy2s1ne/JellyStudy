# This is the demo Dockerfile for the generated template project, please change accordingly before building image from it.
# Run the following command to build image: docker build -f ./Dockerfile --build-arg APP_FILE=demo-0.0.1-SNAPSHOT.jar -t demo:latest .
FROM openjdk:17-jdk-alpine
ADD ai-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]
