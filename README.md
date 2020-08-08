# High Concurrency Product Sales System
Distributed product sales microservice written in Java to handle high concurrency situations, optimizing by caching & asynchronous programming.

## Introduction
For popular e-commerce and online shopping product sales website, where large number of queries occur in a short period, the servers are usually facing a high concurrency environment. <br/><br/>
This project optimizes the server's performance by applying different caching methods and message queue asynchronous algorithms. <br/><br/>
The project also implements functionalities such as cryptographic password protection and robot test. <br/><br/>
The application is designed in a distributted mannar to benefit futher expansion.<br/><br/>
To compare the server's performance before and after optimization, the project uses Apache JMeter for load test. <br/><br/>

## Table of Contents
* [Environments](#snvironments)
* [Some design thoughts and log](#some_design_thoughts_and_log)
* [Optimization Methods](#optimization_methods)
* [Database Design](#database_design)
* [UML](#uml)
* [Interfaces](#interfaces)
* [Reference](#reference)


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


## Some design thoughts and log 
* Microservice design pattern: *controller* calls *service*, *service* calls *dao*. <br/>
  Usually a *service* file only calls its own *dao*, if it needs to call the methods from other *dao*'s, call their *service*'s instead. Otherwise, the step of fetching cache (which lies in the *service* files) would be directly skipped. <br/>
  Inside *dao* files, we can directly configure MyBatis without writing seperate XML files with queries.
  
* Implement a **Result** class to encapsulate basic information at server side.<br/>
  Implement a **Key** class to get the key for accessing database. 
  
* For serialization, use **Fast.json** insead of Protocal Buffer for better human data readability.

* Apply [MD5](https://en.wikipedia.org/wiki/MD5) algorithm twice for user login to user's plaintext password: MD5_server(MD5_client(pass + salt) + random salt).

* To check the validity of user input at Login, use JSR 303 to construct a validation annotation; then, allocate a exception package to catch the exceptions.

* **IMPORTANT** **Distributed Session:** after the user logs in, generate a **session ID** for the user, write it to cookie and pass to the server. The server then takes this specific ID to fetch data for the user. Therefore, each session does not directly store data to the server, but instead to our cache managed by Redis.<br/>
  When the user visit the website before the corresponding token expires, the project extends the token's expiration time by adding a new one to the database.
  
* To keep the products' database easy to maintain and ensure its performance, seperate tables for different sales event from the regular product table.

* After implementing the basic product sales functionalities, use JMeter to conduct load test and measure the system's performance; use custom variables to simulate real-world users. As for Redis, use [redis-benchmark](https://redis.io/topics/benchmarks) for testing.<br/>
  JMeter Linux command:
```sh
sh jmeter.sh -n -t XXX.jmx -l result.jtl 
```
  Redis-benchmark Linux command:
```sh
redis-benchmark -h HOST -p PORT -c CONNECTION -n REQUEST
redis-benchmark -h HOST -p PORT -d DATA
```
<br/>

  **Redis Load Test Result:** about 65k QPS for 100 connections and 100,000 total requests.
  **JMeter Load Test Result For the Core Features Before Optimization:** only around 1.2k QPS for 5000 concurrent threads * 10 iterations; in addition, oversold for the products happened according to the database :( <br/>
  *top* command: moniter system resource usage & storage. This shows that the bottleneck for the system is at the MySQL database. 
  
  
* Now moving to optimizing the service: first, ultilize caching to relieve the pressure of database. More specifically, apply **Page Caching, URL Caching & Object Caching** (listed in order of the Granularity level)<br/>
  For HTML template page caching, the caching period should be relatively small. Page caching mainly aims to deal with large concurrent requests in an extreme short period, therefore storing the HTML templates in cache for a long time is not necessary. If they are indeed stored in cache for a long period, it is possible that data fetched for user from the cache might be outdated.<br/>
  On the other hand, Object Caching focuses on a single user, and its cache should exist forever rather than expiring fast. The only case to make changes to the cache is when the user updates his/her password (in this case we need to modify everything related to the user. ie. token & id).<br/>
  After applying these caching methods, the service's QPS increased to 30k.<br/>
  
* To further optimize the system's performance, make the dynamic HTML pages static. HTML pages will be stored in the user's browser, therefore reducing the pressure for the database. Modern language and tools, such as AngularJS & Vue.js are all implemented in this mannar. 
  The client would ask the server whether the resources have been updated since a specific time, and if it receives a 304 code indicating no update, it will directly fetch the cache from the browser. However, this still requires some communication between the server and client. We can completely get rid of this by manually configure our program, and in a specific time period we allocated, the browser will directly ask its cache without querying the server.
  
* During load test, products are being oversold. That is, inside the database, remaining stock in warehouse of the products became negative. To deal with this problem, check the stock count in database to make sure it is larger than 0 everytime a purchase is made. (When the database is updated by a thread, it will be automatically locked, so race conditions won't happen and we can ignore this part) Also, if one user can purchase no more than one sales product, assign an unique index to the related fields in the database.


* Caching improved the system's performance in some degree. However, for an extreme popular e-commerce product sales website, this is not enough. More methods are  needed to further optimize the system by reducing queries to database. This can be done with **Redis**. Make the place order process to be asynchronous. 
  Install, configure, and integrate RabbitMQ. Implement RabbitMQ sender and receiver.


* **Nginx horizontal scaling：** when the scale of the system increases, expand the service with [Nginx](https://www.nginx.com). With the help of previous optimizations, Nginx provides a powerful tool to expand the system while ensuring performance. <br/>
  As the system grows even larger, apply (LVS)[http://www.linuxvirtualserver.org] for further scaling.
  
  


  
## Optimization Methods
### HTML Page Caching, URL Caching & Object Caching
  1. Fetch cache. If HTML template exist in cache, return.<br/>
  2. If data does not exist in cache, manually render the HTML templates to client and add them to cache.<br/>
  3. Return the HTML templates.<br/><br/>
  
  * Page caching and URL caching are all temporary, that they only exist in cache for a short period of time because the HTML files might be changing. Object caching are however permanent, as the cache is assigned for every single user. 
  

### Staticizing dynamic HTML pages
  Store the HTML pages inside the user's browsers, therefore reducing the queries to the database. Modern language and tools, such as AngularJS & Vue.js are all implemented in this mannar. <br/>
  Implement this inside the HTML files with AJAX, and config inside pom.xml.
  
  
### Optimization over static resources
  1. Compress CSS and JavaScript files to reduce throughput ([webpack](https://webpack.js.org))<br/>
  2. Combine multiple JavaScript and CSS files into a single file to reduce connections, otherwise there might be multiple connections for multiple rounds ([Tengine](https://tengine.taobao.org))
  3. Content Delivery Network ([CDN](https://en.wikipedia.org/wiki/Content_delivery_network)) 
  
 
### Optimizing with asynchronous
  We can further reduce the number of queries to MySQL database using Redis.<br/>
  1. When the system is initialized, load the stock count of the products into Redis.<br/>
  2. When an order is placed, reduce stock count in Redis; if there's no stock left, return. In this case, when the stock count stored in Redis is less than 0, all following requests will be directly returned without querying to database.<br/>
  3. Server queries to enter the queue, return to the user that he/she is in the waiting queue.<br/>
  4. Server queries to exit the queue, generate order and decrease stock count asynchronously.<br/>
  5. Server polls the status of the order.
 

### Optimization on Security:
  1. Hide the product sales event interface. Every time there's a new sales event, generate a random URL from server rather than keeping a static one.<br/>
  2. During login time, ask the user to enter a verification code. While preventing boot/robot, this will also extend the period for a user to log in and query for the database, therefore spliting the concurrent queries and reducing the pressure on server side. <br/>  
  3. Given the QPS for the system, limit the time for the user to query for the server (e.g. login and send verification code) within a specific time period to prevent potential attacks. This can be realized with an interceptor reading on cache data. 
  


### Database Sharding
  


## Database Design
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


### Products on sale (seperated from the Product table)
| Name | Type | Length | Decimals | Not Null | Comment |
| --- | --- | --- | --- | --- | --- |
| id | bigint | 20 | 0 | - |
| product_id | bigint | 20 | 0 | - | Link to the Product table
| start_date | datetime | 0 | 0 | NN | Sales event starting time 
| end_date | datetime | 0 | 0 | NN | Sales event ending time 


### Order
| Name | Type | Length | Decimals | Not Null | Comment |
| --- | --- | --- | --- | --- | --- |
| id | bigint | 20 | 0 | - |
| user_id | bigint | 20 | 0 | - |
| product_id | bigint | 20 | 0 | - |
| product_name | varchar | 64 | 0 | NN |
| product_count | int | 11 | 0 | NN |
| product_price | decimal | 10 | 2 | NN | 
| status | int | 11 | 0 | NN | ENUM of order status
| delivery_addr | bigint | 20 | 0 | - |
| date | datetime | 0 | 0 | NN | Order creation time


### Order for sales event (seperated from the Order table)
| Name | Type | Length | Decimals | Not Null | Comment |
| --- | --- | --- | --- | --- | --- |
| id | bigint | 20 | 0 | - |
| user_id | bigint | 20 | 0 | - |
| order_id | bigint | 20 | 0 | - | Link to the Order table





## UML

## Interfaces

## Reference
* imooc course: https://coding.imooc.com/class/168.html
* Github Repo: https://github.com/qiurunze123/miaosha
