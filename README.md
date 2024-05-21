# Food Challenge

## Prerequisites

Ensure you have the following installed on your local machine:

- Docker
- Java JDK

## Setting Up Your Local Environment

1. **Clone the repository:**
    First, clone the `kitchen-ms` repository to your local machine. You can do this by running the following command in your terminal:

    ```bash
    git clone git@github.com:postech-food-challenge/kitchen-ms.git
    ```

    Navigate to the project directory:

    ```bash
    cd kitchen-ms
    ```

2. **Run docker-compose:**
    In order to run the application locally, a few steps need to be performed:

   1. **Start the application:** Then, you will need to start the application by running:
       ```bash
       docker-compose up 
       ```
   **Attention:** Beware to set your environment variables before running the application
   
That's it! Your local development environment is set up, and you should be able to test our application.

## Test Coverage

   1. This application uses RestAssured to run integration tests.
   2. It uses Jacoco to create test reports. 
   ![image](./imgs/testCoverage.png)

## Software Structure
The architecture implemented in our software is the Clean Architecture. Below is a drawing representing this architecture:
![img.png](imgs/img.png)