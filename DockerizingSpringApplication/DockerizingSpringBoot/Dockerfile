# fetch docker image if not in local cache
FROM openjdk:11
# Add user group and user name
RUN adduser --system usermanagement
RUN addgroup --system springsample
RUN usermod -a -G springsample usermanagement
USER usermanagement:springsample
ARG JAR_FILE=target/*.jar
# Copy the jar file to /*.jar
COPY ${JAR_FILE} usermanagement.jar
# Command to start the container
ENTRYPOINT ["java", "-jar", "/usermanagement.jar"]