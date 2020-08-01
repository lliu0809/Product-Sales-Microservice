# High Concurrency Product Sales System
Product sales microservice written in Java with distributed session to handle high concurrency situations, optimizing by caching & asynchronous programming.


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


## Designs & thoughts
* Microservice design pattern: *controller* calls *service*, *service* calls *dao*;
* Implement a **Result** class to encapsulate basic information;
* Implement a **Key** class to get the key for accessing database;
* For serialization, use Fast.json insead of Protocal Buffer for better readability;


## Reference
* imooc course: https://coding.imooc.com/class/168.html
* Github Repo: https://github.com/qiurunze123/miaosha
