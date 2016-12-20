# 目的#
 
实验SpringBoot集成ConfigServer及docker使用
	
# 非Docker版 #

## 1.server端 ##
**STEP1.Gradle中引用依赖的jar**
	
	compile('org.springframework.cloud:spring-cloud-config-server')
	
**STEP2.修改application.yml**
	
	server:
      port: 8888

    spring:
      application:
        name: configServer

    //根据config-repo选择的存储方式不同，yml中的配置不同
	//1.FileSystem
	  profiles:
	    active: native
      cloud:
        config:
	      server:
            native:
              searchLocations: file:///D:/demo-java-config/config-repo/
    
	//2.git
      cloud:
	    config:
	      server:
	        git:
	          uri: https://github.com/JHerculesqz/demo-java-configserver-repo
              username: *****
              password: *****

**STEP3.在XXXApplication.java增加@EnableConfigServer**

**STEP4.运行Server端，查看http://localhost:8888/{application-name}/default**
	
	可以看到相应配置文件中的信息
	
## 2.Client端 ##

**STEP1.Gradle中引用依赖的包**
	
	compile('org.springframework.cloud:spring-cloud-starter-config')
	
**STEP2.修改application.yml**
	
	server:
      port: 7000  

**STEP3.修改bootstrap.yml**

<font color="red">注意：这里必须要将相应配置写在bootstrap中，不能全部写在application.yml中，否则读不到ConfigServer中的相应信息</font>
	
    spring:
      application:
        name: config-client1
      cloud:
        config:
          uri: http://localhost:8888  
	
**STEP4.在xxxxApplication.java中，增加@EnableAutoConfiguration**
	
**STEP5.运行Client端，查看http://localhost:7000/env**

	可以从xml文件中的ConfigService这一项获取对于此client的相应信息，包括config-repo中相应配置信息的地址，此application的名字等等

## 3.Config Repo ##

/config-repo目录下存放给各个client使用的配置文件，文件类型可以是properties或者yml。其中application.properties会对所有的client端生效，{client-name}.properties则是具体针对某个client的配置文件。config-repo有多种存储方式，此demo中使用了git和FileSystem的方式。

**方式1.git**

	在git中维护一个/config-repo的仓库，Server端中的application.yml的配置方式在ConfigServer中已示例。

**方式2.File System**

	在本地或者服务器上维护/config-repo目录，Server端中的application.yml的配置方式在ConfigServer中已示例。


## 4.代码样例说明 ##
	
	本demo中client1调用client2,client1与client2均从ConfigServer中获取自己配置文件里“name”信息，返回的消息为“name”这个配置项的具体内容；
	“刷新”按钮是为了验证ConfigServer中配置文件内容改变后，client侧通过向/refresh接口发送post请求，来实现配置信息的更新
	

# DOCKER版 #

## 1.server端 ##
**STEP0.编写build.gradle，加入如下配置**

	dependencies {
		//for docker
		classpath('se.transmode.gradle:gradle-docker:1.2')
	}

	//for docker    
	group = 'crystal'
	apply plugin: 'docker'
	
	//for docker
	task buildDocker(type: Docker, dependsOn: build) {
  		push = true
  		applicationName = jar.baseName
  		dockerfile = file('src/main/docker/Dockerfile')
  		doFirst {
    		copy {
      			from jar
      			into stageDir
    		}
  		}
	}

**STEP1.Gradle中引用依赖的jar**
同"非Docker版/1.Server端/STEP1"

**STEP2.修改application.yml**
同"非Docker版/1.Server端/STEP2"

**STEP3.在XXXApplication.java增加@EnableConfigServer**
同"非Docker版/1.Server端/STEP3"

**STEP4.编写src/main/docker/Dockerfile**	
	
	FROM frolvlad/alpine-oraclejdk8:
	VOLUME /tmp
	ADD demo-java-docker-springboot.jar app.jar
	RUN sh -c 'touch /app.jar'
	ENV JAVA_OPTS=""
	
**STEP5.编写2.build_docker.sh,编译生成image**
	
	./gradlew clean && ./gradlew build buildDocker

**STEP6.deployConfigServer.sh**
	
	docker run --name ShitConfigServer --net=crystal -p 8888:8888 -i -t jherculesqz/demo-java-config-server java -jar app.jar
	
**STEP7.查看http://localhost:8888/{application-name}/default**
同"非Docker版/1.Server端/STEP4"
	
## 2.Client端 ##
**STEP0.编写build.gradle，加入如下配置**
同"DOCKER版/1.Server端/STEP0"

**STEP1.Gradle中引用依赖的包**
同"非Docker版/2.Client端/STEP1"
	
**STEP2.修改application.yml**
同"非Docker版/2.Client端/STEP2"

**STEP3.修改bootstrap.yml**

<font color="red">注意：这里必须要将相应配置写在bootstrap中，不能全部写在application.yml中，否则读不到ConfigServer中的相应信息</font><br>
<font color="red">注意：ShitConfigServer是使用Docker network机制创建的image name,否则网络不通网络不通网络不通...</font>
	
    spring:
      application:
        name: config-client1
      cloud:
        config: 
          uri: http://ShitConfigServer:8888 
	
**STEP4.编写src/main/docker/Dockerfile**	
同"DOCKER版/1.Server端/STEP4"

**STEP5.编写2.build_docker.sh,编译生成image**
同"DOCKER版/1.Server端/STEP5"

**STEP6.deployConfigClient.sh**
	
	//1.deployConfigClient1.sh
	docker run --name ShitConfigClient1 --net=crystal -p 7000:7000 -i -t jherculesqz/demo-java-config-client1 java -jar app.jar --server.port=7000

	//2.deployConfigClient2.sh
	docker run --name ShitConfigClient2 --net=crystal -p 8000:8000 -i -t jherculesqz/demo-java-config-client2 java -jar app.jar --server.port=8000
	
**STEP7.查看http://localhost:7000/env和http://localhost:8000/env**
同"非DOCKER版/2.Client端/STEP5"

## 3.Config Repo ##
同"非DOCKER版/3.Config Repo"

## 4.代码样例说明 ##
同"非DOCKER版/4.代码样例说明"


# Docker技术点总结#
####实现步骤(腾讯云)####
**STEP1.先把云端Linux基础配置好**
	
	1.安装SecureCRT
		-修改root密码
		-sudo -s
		-sudo passwd
	2.安装FTP
		-注意FTPServer的文件夹设置成可读写
		-chmod 777 /opt
	3.安装jdk
		-http://www.linuxidc.com/Linux/2016-05/131348.htm
		-即使有docker，我就要安装jdk，怎么滴怎么滴怎么滴?

**STEP2.Install Docker on Ubuntu**
	
	https://docs.docker.com/engine/installation/linux/ubuntulinux/
	注意"$ echo "<REPO>" | sudo tee /etc/apt/sources.list.d/docker.list"这一步

**STEP3.启动Docker Deamon**
	
	sudo service docker start
	sudo docker images
	sudo docker pull jherculesqz/demo-java-docker-springboot

####Docker的基本操作####
**1.单Docker的基本操作**

	docker run -i -t ubuntu /bin/bash
	docker run --name Monkey -i -t ubuntu /bin/bash
	docker run --name Monkey -d ubuntu /bin/sh -c "while true; do echo hello world;sleep 1;done"
	
	docker start Monkey
	docker restart Monkey
	docker attach Monkey
	docker stop Monkey
	docker rm -f Monkey
	
	docker info
	docker ps
	docker ps -a
	docker ps -l
	docker logs -f Monkey
	docker inspect Monkey
	docker top Monkey
	docker stats Monkey
	
	docker images
	docker images <image_id>
	docker rmi <image_id>
	docker rmi <image_name>
	docker history <image_id>

	docker network create crystal
	docker network rm crystal
	docker network ls
	docker network inspect crystal

**2.crud custom repository to dockerhub(未完待续,待我过了TR5)**
	
	STEP1.init for server
		docker search <image_name>:<image_version>
		docker login
	STEP2.1.create local image by commit
		#runtime_id:docker ps -a
		docker commit -m"first repo" -a"jherculesqz" <runtime_id> jherculesqz/test:ubuntu
	STEP2.2.create local image by Dockerfile
		docker build -t="jherculesqz/test:ubuntu" .
	STEP3.push to server
		docker push jherculesqz/test
	STEP4.use image
		docker pull jherculesqz/test
	
**3.custom hub(未完待续,待我过了TR5)**
	
	STEP1.init for server
		docker run -d -p 5000:5000 --restart=always --name registry registry
		docker stop registry && docker rm -v registry
	STEP2.1.create local image by commit
		#runtime_id:docker ps -a
		docker commit -m"first repo" -a"jherculesqz" <runtime_id> jherculesqz/test:ubuntu
		docker tag <image_id> localhost:5000/jherculesqz/test
	STEP2.2.create local image by Dockerfile
		docker build -t="jherculesqz/test:ubuntu" .
	STEP3.push to server
		docker push localhost:5000/jherculesqz/test
		curl http://localhost:5000/v2/_catalog
	STEP4.stop and clear repo
	STEP5.use image
		docker pull jherculesqz/test

**4.艰难的上传image到dockerHub**
	
	https://spring.io/guides/gs/spring-boot-docker/
	https://hub.docker.com/u/jherculesqz/

####Docker Image的网络互联####
<font color="red">注意：Docker Image的网络互联是关键点,否则网络不通网络不通网络不通...</font>
	
	1.新建一个的网络，默认为bridge类型，此demo中的configServer和ConfigClient均在此网络中。
	docker network create crystal
	
	2.每个container中均会维护一个host文件，在docker启动一个image时会将自己分配的IP写入到host文件中，后面则可以根据image的名字来访问此IP。
	例如，client1向client2发送post请求时，可以使用此url"http://ShitConfigClient2:8000/hello"

# reference #
	
**configserver**

	http://cloud.spring.io/spring-cloud-config/
	http://cloud.spring.io/spring-cloud-config/#quick-start
	http://tech.asimio.net/2016/04/05/Microservices-using-Spring-Boot-Jersey-Swagger-and-Docker.html
	storage system
	http://stackoverflow.com/questions/34486319/spring-cloud-config-server-wont-serve-from-local-filesystem
	真理就在此文中
	http://accordance.github.io/microservice-dojo/kata4/externalizing_configuration.html#_externalizing_configuration
	网络如何设置
	《The Docker Book》书中关于网络设置章节
	https://docs.docker.com/engine/tutorials/networkingcontainers/
	
**docker**

	http://cloud.spring.io/spring-cloud-config/#quick-start
	https://spring.io/guides/gs/spring-boot-docker
	
	dockerCompose/dockerSwarm待研究?