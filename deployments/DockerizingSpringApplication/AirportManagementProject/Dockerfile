# fetch base docker image if not in local cache
FROM amazoncorretto:17-alpine

MAINTAINER "JOJI JOY"

# Add user group and user name
RUN adduser --system airportmanagementuser
RUN addgroup --system airportmanagementgroup
RUN addgroup airportmanagementuser airportmanagementgroup

# Set this new user for running application
USER airportmanagementuser:airportmanagementgroup

ARG JAR_FILE=airport-management-1.0.0.jar
ARG TARGET_JAR_FILE=target/${JAR_FILE}

# Copy the jar file to /*.jar
COPY ${TARGET_JAR_FILE} ${JAR_FILE}

# Command to start the container
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "airport-management-1.0.0.jar"]