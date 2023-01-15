# ROUTE API
This service helps our customer to find the best possible routes information based on their locations

route-finder-api service is using `Spider Api` to find the best possible route information
* Spider Api [Swagger](https://spider2.analytics-euw1-dev-1.eks.schibsted.io/swagger/index.html) link

# Development

## Technology:

* Java 17
* Spring boot (3.x)
* Maven as build tool

## Startup instructions

```shell
# go to application directory
cd route-finder-api
# build project using maven
mvn clean install
# build docker image
docker buildx build . -t route-finder-api:latest --platform linux/amd64 --load
# start application and database
docker compose -p route-finder-api -f docker/docker-compose.yml up
# and stop database
docker compose -p route-finder-api -f docker/docker-compose.yml rm
```

## Test

Test route finder api with swagger documentation
* Route Finder Api [Swagger](http://localhost:8082/route-finder-api/swagger-ui/index.html) link

