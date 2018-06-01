# rabbitmq

This project aims to demonstrate publish and consume message with RabbitMQ.


It includes the approach to retry failed message, if message consumes failed, it will be delivered to dead letter queue, wait there some seconds, then message will be delivered to exchange and then is queue, after some times of failure, it will gone.