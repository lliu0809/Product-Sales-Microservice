# Product Sales System for High Concurrency Environments
Distributed micro-service written in Java to handle high concurrency situations, optimizing with caching & asynchronous message queues. 


## Table of Contents
* [Introduction](#introduction)
* [Environments and Tools](#environments-and-tools)
* [Some design thoughts and log](#some-design-thoughts-and-log)
* [Optimization Methods](#optimization-methods)
* [Database Design](#database-design)
* [Reference](#reference)
<br/><br/>


## Introduction
For popular e-commerce and online shopping websites, there might be a huge page view in a extreme short period when there's a special event such as product sales.  In this case, the server will be facing a large number of concurrent queries, significantly influencing its performance. <br/><br/>
This project optimizes the server's performance under such circumstances by applying different caching methods and message queue asynchronous algorithms. <br/><br/>
The project also implements functionalities such as cryptographic password protection and robot test framework. <br/><br/>
The system also uses Redis to realize distributed sessions to benefit futher expansion.<br/><br/>
To compare the server's performance before and after optimization, the project uses Apache JMeter for load test. <br/><br/>


## Environments and Tools
### Back-end
* Microservice framework: [Spring Boot](https://spring.io) with [Maven](https://maven.apache.org)
* Java persistence framework for database access: [MyBatis](https://blog.mybatis.org)
* User input validation: [JSR303](https://beanvalidation.org/1.0/spec/)

### Front-end
* Front-end framework: [Bootstrap](https://getbootstrap.com)
* DOM manipulation: [JQuery](https://jquery.com)
* Template: [Thymeleaf](https://www.thymeleaf.org)

### Middleware and others
* [Redis](https://redis.io)
* Asynchronous message queue: [RabbitMQ](https://www.rabbitmq.com)
* Database monitoring: [Druid](https://druid.apache.org)
* Load test & performance measuring: [JMeter](https://jmeter.apache.org)
* Horizontal scaling: [Nginx](https://www.nginx.com)
<br/><br/>



## Some Design Thoughts and Log 
* Microservice design pattern: *controller* calls *service*, *service* calls *dao*. <br/><br/>
  Usually a *service* file only calls its own *dao*, if it needs to call the functions from other *dao*'s, call their *service*'s instead. Otherwise, cache from *service* files cannot be fetched. <br/>
  
* Implement a **Result** class to encapsulate information from server side.<br/><br/>
  Implement a **Key** class to get the key for accessing database. <br/>
  
* For serialization, use **Fast.json** instead of Protocol Buffer for better human data readability.<br/>

* Apply [MD5](https://en.wikipedia.org/wiki/MD5) algorithm twice to the plaintext passwords at user login to protect data transfer: MD5_server(MD5_client(pass + salt) + random salt).<br/>

* To check the validity of user input at Login, use [JSR 303](https://beanvalidation.org/1.0/spec/) to implement a validation annotation; then, allocate a exception package to catch the exceptions thrown.<br/>

* **Distributed Session:** after the user logs in, generate a *session ID* for the user, write it to cookie and pass it to the server. The server then takes this specific ID to fetch data for the user from database. Therefore, each session does not directly store data to the server, but instead to our cache managed by Redis.<br/><br/>
  When the user visit the website before the corresponding token expires, the project extends the token's expiration time by adding a new one to the database.<br/><br/>
  This will help in future scaling and expansions to keep data consistency.<br/>
  
* To keep the databases easy to maintain and ensure database performance, separate tables for different sales events from the regular product table.<br/>

* After implementing the basic product sales functionalities, use JMeter to conduct load test and measure the system's performance; use custom variables to simulate real-world users. As for Redis, use [redis-benchmark](https://redis.io/topics/benchmarks) for testing.<br/>
  JMeter Linux command:
```sh
$ sh jmeter.sh -n -t XXX.jmx -l result.jtl 
```
<br/>
  Redis-benchmark Linux command:
```sh
$ redis-benchmark -h HOST -p PORT -c CONNECTION -n REQUEST
$ redis-benchmark -h HOST -p PORT -d DATA
```
<br/>

  **Redis Load Test Result:** about 65k QPS for 100 connections and 100,000 total requests. <br/>
  **JMeter Load Test Result:** only around 1.2k QPS for 5000 concurrent threads * 10 iterations :( <br/>
  This shows that the bottleneck for the system is at the MySQL database.  *top* command: monitor system resource usage & storage. <br/>
  
  
* Now moving to optimizations: first, utilize  caching methods to lower the pressure of database. More specifically, apply **Page Caching, URL Caching and Object Caching** (listed in order of granularity level)<br/><br/>
  For HTML template page caching, the caching period should be relatively short: page caching mainly aims to deal with large concurrent requests in an extreme short period, therefore storing the HTML templates in cache for a long time might result in data inconsistency.<br/><br/>
  On the other hand, Object Caching focuses on a single user, and its cache should exist permanently rather than expiring fast. The only case to make changes to the cache is when the user updates his/her password.<br/><br/>
  After applying these caching methods, the service's QPS increased to 30k.<br/>
  
* To further optimize the system's performance, make the **dynamic HTML pages static**. HTML pages can be stored in the user's browser, therefore decreasing the queries to the database. Modern language and tools, such as AngularJS and Vue.js are all implemented in this manner. <br/><br/>
  The client would ask the server whether data have been updated since a specific time, and upon receiving a 304 code indicating no update, it will directly fetch the cache from the browser. <br/> <br/>
  However, this still requires some communication between the server and client. We can completely get rid of this by manually configure our program such that the browser will directly ask its cache without querying the server in a specific time period we allocated.<br/>
  
* During load test, products are being **oversold**. That is, inside the database, remaining stock in warehouse of the products became negative. <br/><br/>
  To deal with this problem, check the stock count in database to make sure it is larger than 0 everytime a purchase is made. (When the database is updated by a thread, it will be automatically locked, so race conditions won't happen and can be ignored)<br/>


* Caching improved the system's performance in some degree. However, for an extreme number of concurrent queries, this is not enough on its own. More methods are  needed to further optimize the system by reducing queries to database. This can be done with **Redis** and **asynchronous programming**. <br/><br/>


* **Nginx horizontal scaling：** when the scale of the system increases, expand the system with [Nginx](https://www.nginx.com).<br/><br/>
  With the help of previous optimizations, Nginx would provide a powerful tool to expand the system while ensuring performance. <br/><br/>
  As the system grows even larger, apply (LVS)[http://www.linuxvirtualserver.org] for further scaling.<br/>
  
  <br/><br/>


  
## Optimization Methods
### HTML Page Caching, URL Caching & Object Caching
  1. Fetch cache. If HTML templates exist in cache, return.<br/>
  2. If data does not exist in cache, add them to cache.<br/>
  3. Manually render the HTML templates to the client.<br/>
  
  * Page caching and URL caching are all temporary, that they only exist in cache for a short period of time because the HTML files might be updating frequently. Object caching are however permanent, as the cache is assigned to store every single user's data. 
  

### Staticizing dynamic HTML pages
  Store the HTML pages inside the user's browsers, therefore reducing the queries to the database.<br/>
  Implement this inside the HTML files with AJAX, and config inside pom.xml<br/>
  Modern language and tools, such as AngularJS & Vue.js are all implemented in this mannar. <br/>
 
  
### Optimization over static resources
  1. Compress CSS and JavaScript files to reduce throughput ([webpack](https://webpack.js.org))<br/>
  2. Combine multiple JavaScript and CSS files into a single file to reduce connections([Tengine](https://tengine.taobao.org))<br/>
  3. Content Delivery Network caching([CDN](https://en.wikipedia.org/wiki/Content_delivery_network)) 
  
 
### Optimizing with asynchronous programming
  We can further reduce the number of queries to MySQL database using Redis and asynchronous programming (Message Queue with RabbitMQ).<br/>
  1. When the system is initialized, load the stock count of the products into Redis.<br/>
  2. When an order is placed, reduce stock count in Redis; if there's no stock left, return. In this case, when the stock count stored in Redis is less than 0, all following requests will be directly returned without further querying to database.<br/>
  3. Server queries to enter the queue, return to the user that he/she is in the waiting queue.<br/>
  4. Server queries to exit the queue, generate order and decrease stock count asynchronously.<br/>
  5. Server polls the status of the order.
 
### Database Sharding
<br/><br/>

### Optimization on Security
  1. Hide the product sales event interface. Every time there's a new sales event, generate a random URL from server rather than keeping a static one.<br/>
  2. During login time, ask the user to enter a verification code. While preventing boot/robot, this will also extend the period for a user to log in and query for the database, therefore spliting the concurrent queries and reducing the pressure on server side. <br/>  
  3. Given the QPS for the system, limit the time for the user to query for the server (e.g. login and send verification code) within a specific time period to prevent potential attacks. This can be realized with an interceptor reading on cache data. 
  
<br/><br/>


  


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

<br/><br/>



## Reference
* imooc course: https://coding.imooc.com/class/168.html
* Github Repo: https://github.com/qiurunze123/miaosha
