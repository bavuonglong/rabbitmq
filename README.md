[![CircleCI](https://circleci.com/gh/bavuonglong/rabbitmq/tree/master.svg?style=svg)](https://circleci.com/gh/bavuonglong/rabbitmq/tree/master)

# Rabbit MQ

This project aims to demonstrate publishing and consuming message with RabbitMQ.

It includes the approach to retry failed message, if message consumes failed, it will be delivered to dead letter queue, wait there some seconds, then message will be delivered to exchange and then is queue, after some times of failure, it will gone.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You can install rabbit mq and start it manually by yourself. 

But by using Docker, it will isolate your project environment with your machine, support delivering to other environment more easier.

So, consider to install Docker on your machine for your development, follow this[link](https://docs.docker.com/docker-for-mac/install/#install-and-run-docker-for-mac)if you're using MacOS.
Then checking your installation result by:

```
docker -v
or
docker-compose -v
```


## Building



### Build application

Using maven command

```
mvn clean package
```

### Build docker image

At root directory, run this command

```
docker build -f=Dockerfile -t=order-app:latest --rm=true .
```

### Run docker container

After successfully build docker image, run that image by

```
docker run -d --name order-app -p "4321:1234" order-app:latest
```

In order to run bash command inside docker container, you should:

. List down all running docker containers

```
docker ps
```

.. Pick container id you'd like to access


```
docker exec -it container_id bash
```


BUT: we need to start another rabbitmq server container, because this project use rabbitmq, so even you start previous docker container successfully, it still can not work properly due to rabbitmq connection problem. So we need to start up docker-compose.yml to start 2 individual containers up.


Start all relevant containers up by

```$xslt
docker-compose up
```

In case you update docker-compose file, in order to build and up all containers again, please use

```$xslt
docker-compose up --build
```

### Verifying
If you run project by docker-compose, open your browser and verify the running application by those endpoints:

```$xslt
localhost:4321 => Welcome to Order Application, Greeting from application yml inside config folder
localhost:15672 => admin/admin, this is rabbitmq ui management page
```

If you run project individually, you have to start rabbitmq server by
```$xslt
docker run -d -h host-name --name rabbitmq -p "4369:4369" -p "5672:5672" -p "15672:15672" -p "25672:25672" -p "35197:35197" -e "RABBITMQ_USE_LONGNAME=true" -e "RABBITMQ_LOGS=/var/log/rabbitmq/rabbit.log" -v /data:/var/lib/rabbitmq -v /data/logs:/var/log/rabbitmq rabbitmq:3.6.6-management

localhost:15672 => admin/admin, this is rabbitmq ui management page
```

then verify running project by
```$xslt
localhost:8080 => Welcome to Order Application, Greeting from application yml inside resource folder
```
## Deployment



## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* [**Cuong Ngo**](bavuonglong93@gmail.com)
