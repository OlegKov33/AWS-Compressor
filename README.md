# AWS-Compressor
> [!IMPORTANT]
> This guide was made with the intention that the project is production-ready and will not cover multiple Docker image uploads
> In this README file, you will see how to configure a Maven Spring Boot project on AWS(Amazon Web Services) using Docker on Windows
> If something doesn't work, check your pom.xml file and compare it to mine; check that you don't already have Docker images named the same as mine.

> [!CAUTION]
> This project works on every type of file: txt, rar, png. Just to name a few. However, only plain text such as .txt and .csv get compressed. Meanwhile, others grow in size.
> This project has an index.html page, allowing anyone to use it and making it vulnerable to DoS attacks. I would advise adding [Spring Security](https://spring.io/projects/spring-security) or reducing the maximum file size [here](Compressor/src/main/resources/application.properties)

## Prerequisite
1. Docker Desktop [download here](https://www.docker.com/products/docker-desktop/)
2. [AWS Account](https://aws.amazon.com/)
3. AWS CLI [download here](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
4. (Optional) IDE of your choice [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

> [!NOTE]
> After this point, anytime you see < square brackets and text >, you need to find appropriate information to put into e.g.
> "--region < your-region >" -> "--region us-east-1"

Now that you have the prerequisites, you will set up the user and AWS sides. The user side will walk you through:
1. Getting your project ready
2. Creating a Dockerfile and Docker image
3. Setting up and using AWS CLI
4. Creating AWS ECR

AWS side will walk you through:
1. Creating an IAM role
2. Creating an EC2 service
3. Pushing our Docker Image and running it

## Setting up the user side
1. In the root of your project (where the pom.xml and mvnw file are), you should see a new folder **target** appearing after running:
```
mvn clean package
```
2. In root create a file "Dockerfile" <ins>with no extensions</ins> and add the following into it. Make sure that the eclipse-temurin version is equal to your pom.xml
```
FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```
3. Run this command; **make sure to include a full stop in the command**
```
docker build -t file-compressor:latest .
```
4. **(optional)** test your docker image with
```
docker run -p 8080:8080 file-compressor:latest
```
5. Open up your AWS account, click your username, in the dropdown select "Security credentials", scroll down and click "Create access key". Download the CSV, as you will be using it later on.
6. Open up a command line and type:
```
aws configure 
```
6. For Access Key, use the downloaded CSV file.

For the country. Either use the default **us-east-1** or your nearest available. On the AWS website, near the gear icon at the top, press the country name.
For the output format, use **json**

7. Run this command:
```
aws ecr create-repository --repository-name file-compressor --region <your-region>
```

> [!NOTE]
> You should see something like: 123456789012.dkr.ecr.us-west-1.amazonaws.com/file-compressor in your console

8. Now run the following 3 commands:
```
aws ecr get-login-password --region <your-region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com
```
9.
```
docker tag file-compressor:latest <account-id>.dkr.ecr.<region>.amazonaws.com/file-compressor:latest
```
10.
```
docker push <account-id>.dkr.ecr.<region>.amazonaws.com/file-compressor:latest
```

This is the end of your user-side setup. The points 1 - 3 set up your Maven project and Docker. Commands 4-10 were used to configure AWS and push your Dockerfile into the AWS system

## Setting up AWS side
1. Search IAM,

Go into roles and create an EC2 role

In Permissions policies, select "AmazonEC2ContainerRegistryReadOnly"

2. Search EC2, instances

Press Launch instance. Include "Allow HTTP traffic" and create a new key pair (the key pair is different from the one we downloaded).

Under Advanced details, select the IAM role you created earlier

3. Navigate back to instances and select the one you just created

Open the Security tab, scroll to "security groups", and select **sg-numbers  (launch-wizard-4)**. Edit inbound rules, add a rule, set the port range to 8080, and add 0.0.0.0/0

4. Open the EC2 instance by selecting it, then pressing "Connect," then connecting.
5. Once you are inside the terminal, run the following commands:
```
sudo dnf update -y
sudo dnf install docker -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ec2-user
```
6. Once everything is done, close the terminal and reboot the instance from the EC2 instances tab and connect like in step 4

> [!NOTE]
> Remember the region and user ID you used on your user side; you need to use them again

7. Run:
```
aws ecr get-login-password --region <your-region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com
```
8.
```
docker pull <account-id>.dkr.ecr.<region>.amazonaws.com/file-compressor:latest
```
9.
```
docker run -d -p 80:8080 --name compressor <account-id>.dkr.ecr.<region>.amazonaws.com/file-compressor:latest
```

After your last command, you should be able to visit PublicIPs: (ip address) in the format: http://yourIP or http://yourIP:80

> [!WARNING]
> **Once you have followed everything, your instance will be running and will not stop even if you turn your computer off**
> In order to stop it, you must follow the following instructions. Upon relaunching an instance, your IP also changes

## Terminating instance

1. If you just want to stop the Docker image, use:
```
sudo docker stop compressor
```
2. If you want to stop the AWS instance, navigate to EC2 instances, right-click your instance and press "Stop instance"

## Running instance
```
sudo docker start compressor
```
