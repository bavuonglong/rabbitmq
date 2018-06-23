FROM alpine:edge
MAINTAINER Cuong Ngo "bavuonglong93@gmail.com"
VOLUME /tmp

RUN apk add --no-cache openjdk8 \
 bash

USER root
RUN mkdir -p /data/project/orderapp/config
COPY ./config/* /data/project/orderapp/config/
COPY ./target/order-app.jar /data/project/orderapp/app.jar

COPY ./script/* /usr/script/
RUN chmod -R 775 /usr/script/