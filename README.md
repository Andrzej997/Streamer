# Streamer Backend
This is a backend server for Streamer Web Api.
It is created using Spring Boot and Docker.
The architecture is microservices with REST.

## Auth-Service
This is a microservice for registration, authorisation
and managing users. It allows 3 basic roles: 
Unsigned user, signed user and Admin, every role has his own
authorization context and security context.
And every role may has access to different routes.
JWT (JSON Web Token) is used for secure authorization

## Ebook-Service, Image-Service, Music-Service and Video-Service
These are business logic microservices, created using
Spring Boot and deployed as Docker image.
Every service allows to store, edit and delete files and
metadata. It also allows you to stream your file to client
by REST api. The model layer is created with Hibernate.
The repositories layer is created with CRUD Repository.
The service layer is using repositories to obtain or save
data, maps them to DTO using my custom mapper and then 
sends them to api controller. There are always 3 controllers
/noauth, /auth and /admin, each of then is used for 
different user role.

## Api-Gateway
This is a microservice to map requests from client to
a specific business microservice port. It is also
used to provide authorization for other than Auth-Service
microservice. It has a several number of http filters to provide
user or authorization during request to other microservices.
For example if you want to download a file from music-service,
the api-gateway first creates a request to Auth-Service to provide
your credentials and then allows you to download file, of course 
if you are logged in. There is also a CORS Filter that allows
every one to access api gateway and creates ACL Headers for every request.

## Eureka-Service
This is a microservice called service-registry.
A service registry is a phone book for your microservices. 
Each service registers itself with the service registry and 
tells the registry where it lives (host, port, node name) 
and perhaps other service-specific metadata - things that 
other services can use to make informed decisions about it.

## Config-Server
This is a configuration server that provides a config for
every microservice running. The config is [here](https://github.com/Andrzej997/Streamer-Spring-Cloud-Config-Server)
Every service can have different running profile.
For example for development we have a dev profile, for Docker
we have a docker profile, for Cloud Foundry we have a cloud profile,
and for Unit testing we have a test profile.

## Hystrix-Dashboard
It is a microservice to test efficiency for business services.
 
 ## Docker
 Every service has his own DockerFile to build a docker image.
 The maven has docker plugin which builds docker image every build.
 To run all service enter the main docker package where docker-compose file is
 and type in command line `docker-compose up`, and every microservice is up after
 maybe 15 min.
 To test what is running in firefox type `localhost:8761`
 