# High Concurrency Product Sales System
Distributed product sales microservice written in Java to handle high concurrency situations, optimizing by caching & asynchronous programming.

## Introduction
For popular e-commerce and online shopping product sales website, where large number of queries occur in a short period, the servers are usually facing a high concurrency environment. <br/><br/>
This project optimizes the server's performance by applying different caching methods and message queue asynchronous algorithms. <br/><br/>
The project also implements functionalities such as cryptographic password protection and robot test. <br/><br/>
The application is designed in a distributted mannar to benefit futher expansion.<br/><br/>
To compare the server's performance before and after optimization, the project uses Apache JMeter for load test. <br/><br/>


## Environments
### Back-end
* Microservice framework: [Spring Boot](https://spring.io)
* Java persistence framework for database access: [MyBatis](https://blog.mybatis.org)
* User input data validation: [JSR303](https://beanvalidation.org/1.0/spec/)

### Front-end
* Front-end framework: [Bootstrap](https://getbootstrap.com)
* DOM manipulation: [JQuery](https://jquery.com)
* Server-side template: [Thymeleaf](https://www.thymeleaf.org)

### Middleware
* [Redis](https://redis.io)
* Asynchronous message queue: [RabbitMQ](https://www.rabbitmq.com)
* Database monitoring: [Druid](https://druid.apache.org)
* Load test & performance measuring: [JMeter](https://jmeter.apache.org)
* Horizontal extension: [Nginx](https://www.nginx.com)


## Some thoughts when designing the system
* Microservice design pattern: *controller* calls *service*, *service* calls *dao*. 
* Usually a *service* file only calls its own *dao*, if we need to call the methods for other *dao*'s, call their *service*'s instead.
* Inside *dao* files, we can directly configure MyBatis without writing seperate XML files with queries.
* Implement a **Result** class to encapsulate basic information at server side.
* Implement a **Key** class to get the key for accessing database.
* For serialization, use **Fast.json** insead of Protocal Buffer for better human data readability.
* Apply [MD5](https://en.wikipedia.org/wiki/MD5) algorithm twice for user login to user's plaintext password: MD5_server(MD5_client(pass + salt) + random salt).
* To check the validity of user input at Login, use JSR 303 to construct a validation annotation; then, allocate a exception package to catch the exceptions.
* **IMPORTANT** **Distributed Session:** after the user logs in, generate a **session ID** for the user, write it to cookie and pass to the server. The server then takes this specific ID to fetch data for the user. Therefore, each session does not directly store data to the server, but instead to our cache managed by Redis.
* When the user visit the website before the corresponding token expires, the project extends the token's expiration time by adding a new one to the database.
* To keep the products' database easy to maintain and ensure its performance, assign seperate tables to different sales event.
* After constructing the product sales functionalities, use JMeter to conduct load test and measure the system's performance; use custom variables to simulate real-world users. As for Redis, use [redis-benchmark](https://redis.io/topics/benchmarks) for testing.
* JMeter Linux command:
```sh
sh jmeter.sh -n -t XXX.jmx -l result.jtl 
```
* Redis-benchmark Linux command:
```sh
redis-benchmark -h HOST -p PORT -c CONNECTION -n REQUEST
redis-benchmark -h HOST -p PORT -d DATA
```
* **Redis Load Test Result:** about 65k QPS for 100 connections and 100,000 total requests.
* **JMeter Load Test Result For the Core Features Before Optimization:** only around 1.2k QPS for 5000 concurrent threads * 10 iterations; in addition, oversold for the products happened according to the database :( 
* *top* command: moniter system resource usage & storage. This shows that the bottleneck for the system is at the MySQL database. 


## Database
### User
| Name | Type | Length | Decimals | Not Null | Comment |
| --- | --- | --- | --- | --- | --- |
| id | bigint | 20 | 0 | - |
| username | varchar | 16 | 0 | NN |
| password | varchar | 11 | 0 | NN |
| salt | varchar | 16 | 0 | NN | Salt for MD5 data process on password
| profile_image | varchar | 64 | 0 | NN |
| register_date | datetime | 0 | 0 | NN | 
| lastLogin_date | datetime | 0 | 0 | NN | 
| login_count | int | 11 | 0 | NN | 


### Products
| Name | Type | Length | Decimals | Not Null | Comment |
| --- | --- | --- | --- | --- | --- |
| id | bigint | 20 | 0 | - |
| product_name | varchar | 16 | 0 | NN |
| product_img | varchar | 64 | 0 | NN |
| product_detail | longtext | 0 | 0 | NN | Product Description
| product_price | decimal | 10 | 2 | NN | Original price of product
| product_stock | int | 11 | 0 | NN | Remaining stock for the product

## Reference
* imooc course: https://coding.imooc.com/class/168.html
* Github Repo: https://github.com/qiurunze123/miaosha
