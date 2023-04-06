# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Install maven
RUN apt-get update && apt-get install -y maven

# Run Maven to download dependencies
RUN mvn dependency:resolve

# Copy the rest of the project files to the container
COPY . .

# Build the project with Maven
RUN mvn package

# Expose port 8080 for the application
EXPOSE 8080

# Set the default command to run the application
CMD ["java", "-jar", "target/Flowers.jar"]