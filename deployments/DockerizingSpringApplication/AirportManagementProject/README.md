# DOCKERIZED SPRING BOOT APPLICATION

## DOCKER Steps

1. Create docker file as [here](./Dockerfile).
2. Build the docker image using above file
    ```sh
    docker build -f Dockerfile <image-name> .
    ## Example
    docker build -f Dockerfile -t airport-project:1.0.0 .
   ```
3. Deploy application using following command,
    ```sh
      docker run -p 8080:8080 airport-project:1.0.0
    ```