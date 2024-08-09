# Kitchen Service

This repository contains the Kitchen Service, a component of the Postech Food Challenge system. The Kitchen Service is responsible for managing the kitchen operations within the system.

## Prerequisites

Before building and running the application, ensure you have the following installed and configured:

- **JDK 21**: The project requires JDK 21 to build and run. You can download and install it from [here](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).
- **AWS CLI**: The AWS Command Line Interface (CLI) is required for interacting with AWS services. You can install it from [here](https://aws.amazon.com/cli/). After installation, make sure it's configured with dummy `access_key` and `secret_key`:

  ```bash
  aws configure
  ```

Use dummy values for `access_key` and `secret_key` when prompted.

## Building the Project

To build the project and run the tests, use the following command:

```bash
./gradlew clean build
```

This will compile the source code, run the tests, and create the necessary build artifacts.

## Running the Application Locally

To run the application locally, follow these steps:

1. **Start the Docker environment**:

   The application relies on Docker containers for some of its dependencies. To start these containers, execute:

   ```bash
   docker compose up
   ```

2. **Execute the post-setup script**:

   After the containers are up and running, you need to run a shell script to finalize the setup. Ensure the script has execution permissions, then execute it:

   ```bash
   chmod +x ./localstack/run-this-after-compose-up.sh
   ./localstack/run-this-after-compose-up.sh
   ```

3. **Access the Application**:

   Once the setup is complete, the Kitchen Service will be running and accessible at:

   ```
   http://localhost:8080
   ```

## Notes

- Ensure Docker is installed and running on your machine before attempting to start the environment.
- The application is configured to use LocalStack for simulating AWS services locally.