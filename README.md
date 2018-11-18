<h1>MBox StorageService</h1>

<p>University Name: <a href="http://www.sjsu.edu/" rel="nofollow">http://www.sjsu.edu/</a><br>
Course: <a href="http://info.sjsu.edu/web-dbgen/catalog/courses/CMPE281.html" rel="nofollow">Cloud Technologies</a><br>
Professor: <a href="https://www.linkedin.com/in/sanjaygarje/" rel="nofollow">Sanjay Garje</a><br>
ISA: <a href="https://www.linkedin.com/in/anushri-aithal" rel="nofollow">Anushri Srinath Aithal</a><br>
Student: <a href="https://www.linkedin.com/in/muthu-kumar-sukumaran/" rel="nofollow">Muthu Kumar Sukumaran</a></p>

MBox StorageService

MBox StorageService is an AWS cloud based web application that allows its users to store, update, delete, view files. Users can upload any number of files with a size limitation of 10 MB. User can upload any type of file. Users can update the file contents which can be downloaded immediately. User can check the file details like File Name, File Description, File Upload Time and File Update Time. The users can download and delete their files whenever needed. This application basically manages all the files using AWS cloud based components. The application also provides an additional layer of security with the use of session tokens which allows an user to logged into the storage service for a certain time limit.



TECHNOLOGIES USED

**FRONT END:**

- **●●** HTML
- **●●** CSS
- **●●** ANGULAR JS
- **●●** BOOTSTRAP

**BACK END:**

- **●●** MySQL RDS
- **●●** Hybernat
- **●●** JPA
- **●●** Logback
- **●●** Actuator
- **●●** Spring Boot (Embedded Tomcat)

**IDE:IntelliJ**

**JDK Version 1.8**

FEATURES LIST

- **●●** Higher Availability of Uploaded files due to cross region replication.
- **●●** Highly Scalable Web Application with the help of auto scaling group.
- **●●** Highly Secure Web Application with the implementation of Session Tokens with a timeout of 20 minutes.
- **●●** Upload files seamlessly through Transfer Acceleration.
- **●●** Login made quicker with implementation of Social Sign ups through Google or Facebook.
- **●●** Update Files with the property of versioning.
- **●●** Admin View of all the files stored by the user.

AWS COMPONENTS

- **EC2**

The project artifacts are created in EC2 and use it as the AMI for Auto Scaling.

- **ELB**

A classic Load Balancer is used to distribute the network traffic evenly across the available number of EC2 instances.

- **Lambda**

**       ** Lambda is used to run serverless functions to invoke SNS to send email.

- **Auto Scaling Group**

The AMI created is used to scale up or down the number of instances based upon the number of users.

- **RDS (Single AZ)**

Maintains the record of user information and file metadata in a database.

- **CloudFront**

Used to provide the files more locally to the user by improving the access speed for downloading the file.

- **S3**

Storage location used to store and retrieve user files.

- **Standard Infrequent Access**

Objects in the s3 bucket are moved to Standard IA after 75 days through the Lifecycle Policy.

- **Amazon Glacier**

Objects in the s3 bucket are moved to Amazon Glacier after 365 days through the Lifecycle Policy.

- **S3 Transfer Acceleration               **

To accelerate Amazon S3 data transfer through optimized used Amazon Edge locations.

- **Route 53**

Provides a Domain name, resolves the IP Address and performs health checks to route the traffic to healthy endpoints.

- **CloudWatch**

Used to monitor metrics like health check of the application and create an alarm which would in turn trigger Lambda.

- **SNS**

Used to send email to the subscriber details mentioned in the topic.

**SOLUTION**

1.Validation is done to limit the file size to less than 10MB.

2.All the files will be stored on the S3 bucket as objects and a backup will stored on a different bucket located in another region. This is recover the data in times of disaster using Cross Replication Policy.

3.The S3 bucket has a Lifecycle Policy enabled to store the data Standard storage class to access immediately for 75 days. Then the objects in the bucket automatically moves to Standard Infrequent Access with a lesser availability but same durability after 75 days. And after one year the objects are archived in Amazon Glacier with the same durability. The objects are completely deleted at the end of two years.

4.The key feature of S3 is enabled which is Transfer acceleration. This is enabled to speed up the upload of especially large files.

5.All the users can access this storage service from the domain name &quot;www.mboxstorage.com&quot; registered and hosted using Route 53. Every user needs to register on the website to access the file storage service using their First Name, Last Name, Email Address and Password.

6.A registered user can login to the storage service using the registered email address and password. Otherwise, the user can use the Social Signups like Facebook and Google present at the Sign in page to get access to the storage service. This method of signing in removes the need to register as a new user.

7.For each insert, update, delete operation the data will be send to S3 bucket and RDS MYSQL instance will maintain the record of file metadata.

8.CloudFront is implemented to reduce the access time of the file for people from other regions. This is possible as the CloudFront makes the best use of Amazon&#39;s edge locations situated at different regions.

9.Whenever a user enters the storage service for the first time, a folder is created on the S3 bucket with the user&#39;s email address. This is done to separate the user&#39;s files from the rest of the user&#39;s files.

10.The Single Availability Zone RDS deployed in the application can be converted into Multi AZ RDS using two methods

- In DB Subnet Group, Create DB Subnet Group with two subnets in different AZ.


- Create Read Replicas which can only be used to read data from the database but not write.


11.Cloud watch is set to monitor the health of the application and send email notification to admin in case of any failures.

12.Multiple EC2 instances are present in multiple Availability Zones such that even if one instance fails another EC2 instance will boot up using Auto Scaling and Low latency feature between Multiple Availability Zones in a region.

13.Amazon SNS service is used to send email to user whenever an EC2 instance becomes affected.

14.Multiple Availability Zones and Regions are available for S3 bucket and EC2 instances through Cross Region replication and Auto Scaling Group respectively.

**DEMO**
<a href="https://drive.google.com/open?id=1iLoqJXC-dUqhJKlGLOIH2vWsSlu4x8vR">Demo</a><br>

**ARCHITECTURE**

![aws architecture](https://user-images.githubusercontent.com/42783963/47687745-f82fd480-db9e-11e8-9e8e-bf26862fca71.jpeg)

**PUBLIC URL**

http://mboxstorage.com


<b>SCREENSHOTS</b><br>
<br>
<b>S3</b><br>
<img width="960" alt="s3" src="https://user-images.githubusercontent.com/42783963/48667261-e8612d00-ea86-11e8-9bb7-5ef24cb2e129.png">
<br>
<img width="960" alt="s3 files" src="https://user-images.githubusercontent.com/42783963/48667310-c916cf80-ea87-11e8-957b-6e13421b410f.png">
<br>
<b>CloudFront</b><br>
<img width="960" alt="cloudfront" src="https://user-images.githubusercontent.com/42783963/48667283-59084980-ea87-11e8-99bb-f7ada3821e50.png">
<br>
<b>SNS</b><br>
<img width="960" alt="sns" src="https://user-images.githubusercontent.com/42783963/48647430-cdb48880-e9a0-11e8-8937-4ee9d6f11245.png">
<br>
<b>CloudWatch</b><br>
<img width="960" alt="cloudwatch" src="https://user-images.githubusercontent.com/42783963/48647462-e3c24900-e9a0-11e8-8558-a2047cbf8d24.png">
<br>
<b>Lambda</b><br>
<img width="960" alt="lambda" src="https://user-images.githubusercontent.com/42783963/48647506-06546200-e9a1-11e8-8da4-46503240f7b7.png">
<br>
<b>SNS Subscription Email</b><br>
<img width="960" alt="ec2 instance launch" src="https://user-images.githubusercontent.com/42783963/48647537-22f09a00-e9a1-11e8-9172-95e3b0e31f77.png">
<br>
<img width="960" alt="ec2 instance terminate" src="https://user-images.githubusercontent.com/42783963/48647558-313eb600-e9a1-11e8-8e99-bb083df88f0c.png">
<br>
<img width="960" alt="health check" src="https://user-images.githubusercontent.com/42783963/48647585-3d2a7800-e9a1-11e8-938d-90cb8b86ff17.png">
<br>
<b>Elasctic Load Balancer</b><br>
<img width="960" alt="load balancer" src="https://user-images.githubusercontent.com/42783963/48647629-592e1980-e9a1-11e8-9637-09997f98e1db.png">
<br>
<b>Auto Scaling</b><br>
<img width="960" alt="autoscaling" src="https://user-images.githubusercontent.com/42783963/48647681-87abf480-e9a1-11e8-87e4-4641fc6e1ec2.png">
<br>
