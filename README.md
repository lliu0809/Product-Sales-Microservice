# High Concurrency Product Sales System
Product sales microservice written in Java with distributed session to handle high concurrency situations, optimizing by caching & asynchronous programming.

## Introduction
For popular e-commerce and online shopping product sales website, where large number of queries occur in a short period, the servers are usually facing a high concurrency environment.
This project optimizes the server's performance by applying different caching methods and message queue asynchronous algorithms. 
The project also implements functionalities such as cryptographic password protection and robot test.
To test the performance of the server under different circumstances, the project uses Apache JMeter for load test.


## Environments
### Back-end
* Microservice framework: [Spring Boot](https://spring.io)
* Java persistence framework for database access: [MyBatis](https://blog.mybatis.org)
* Server side authentication: [JSR303](https://beanvalidation.org/1.0/spec/)

### Front-end
* Front-end framework: [Bootstrap](https://getbootstrap.com)
* DOM manipulation: [JQuery](https://jquery.com)
* Server-side template: [Thymeleaf](https://www.thymeleaf.org)

### Middleware
* Caching: [Redis](https://redis.io)
* Asynchronous message queue: [RabbitMQ](https://www.rabbitmq.com)
* Database monitoring: [Druid](https://druid.apache.org)
* Load test & performance measuring: [JMeter](https://jmeter.apache.org)
* Horizontal extension: [NGINX](https://www.nginx.com)


## Some designs & thoughts
* Microservice design pattern: *controller* calls *service*, *service* calls *dao*;
* Implement a **Result** class to encapsulate basic information;
* Implement a **Key** class to get the key for accessing database;
* For serialization, use Fast.json insead of Protocal Buffer for better code readability;
* Apply [MD5](https://en.wikipedia.org/wiki/MD5) algorithm twice for user login to user's plaintext password: MD5_server(MD5_client(pass + salt) + random salt);
* 



## Reference
* imooc course: https://coding.imooc.com/class/168.html
* Github Repo: https://github.com/qiurunze123/miaosha
