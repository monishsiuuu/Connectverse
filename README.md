# Project Management App

Welcome to the Project Management App, a modern and efficient tool built with Spring Reactive WebFlux for managing your projects effortlessly. This app offers a reactive and non-blocking architecture, ensuring high performance and scalability.

## Features

- **Reactive Web Interface**: Utilizing Spring WebFlux, the app provides a responsive and interactive user interface.
- **Project Management**: Create, update, and delete projects with ease.
- **Task Tracking**: Manage tasks within each project, track progress, and mark tasks as complete.
- **Reactive Data Access**: Leverage the power of reactive programming with Spring Data MongoDB for efficient data access.
- **RESTful API**: Expose a RESTful API for seamless integration with other systems or client applications.
- **Security**: Secure your app with Spring Security to protect your data and resources.
- **Logging and Monitoring**: Monitor application health and performance with Spring Boot Actuator and Micrometer.

## Getting Started
To get started with the Project Management App, follow these steps:

# For Maven

1. **Clone the Repository**: Clone this repository to your local machine using the following command:

   ```bash
   git clone https://github.com/Navin3d/Reactive-Project-Management.git
   ```
2. Build the Application

Navigate to the project directory and build the application using Maven or Gradle:

```bash
cd project-management-app
./mvnw clean install 
```

3. Run the Application

Run the Spring Boot application using the following command:

```bash
./mvnw spring-boot:run
```
4. Access the Application

Once the application is running, access it in your web browser at [http://localhost:8080](http://localhost:8080).

# For Docker
To run the Project Management App using Docker, follow these steps:

```bash
docker run -p 8080:8080 navin3d/reactive-project-manager-backend:auth-disabled
```

## Configuration

The Project Management App can be configured to meet your specific requirements. Here are some configuration options:

- **Database Configuration**: Configure the MongoDB connection properties in `application.properties` or `application.yml`.
- **Security Configuration**: Customize security settings in `SecurityConfig.java` to match your authentication and authorization requirements.
- **Logging Configuration**: Adjust logging levels and appenders in `logback.xml` for effective monitoring and troubleshooting.

## REST API

The Project Management App provides a RESTful API for interacting with projects and tasks programmatically. Here are some API endpoints:

- **GET /projects**: Get all projects.
- **GET /projects/{id}**: Get a project by ID.
- **POST /projects**: Create a new project.
- **PUT /projects/{id}**: Update a project.
- **DELETE /projects/{id}**: Delete a project.
- **GET /projects/{projectId}/tasks**: Get all tasks for a project.
- **GET /projects/{projectId}/tasks/{taskId}**: Get a task by ID for a project.
- **POST /projects/{projectId}/tasks**: Create a new task for a project.
- **PUT /projects/{projectId}/tasks/{taskId}**: Update a task for a project.
- **DELETE /projects/{projectId}/tasks/{taskId}**: Delete a task for a project.

For more details and examples, refer to the API documentation or Swagger UI at [http://localhost:8080/webjars/swagger-ui/index.html#/](http://localhost:8080/webjars/swagger-ui/index.html#/).

## Contributing

Contributions are welcome! If you'd like to contribute to the Project Management App, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/my-feature`).
3. Make your changes and commit them (`git commit -am 'Add new feature'`).
4. Push your changes to your forked repository (`git push origin feature/my-feature`).
5. Create a new pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Spring Framework
- Spring webflux.
- Reactive MongoDB
- Reactor Core

## References:
- [Reactive Mongo](https://hantsy.github.io/spring-reactive-sample/data/data-mongo.html)
- [mono-map-vs-mono-flatmap](https://stackoverflow.com/questions/56496426/project-reactor-mono-map-vs-mono-flatmap)
