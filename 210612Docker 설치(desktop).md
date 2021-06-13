### Docker 설치 (Windows / Mac)

1. Docker desktop 설치

   https://www.docker.com/products/docker-desktop

   * Docker site 회원가입도 권장(Docker Hub 사용)

2. 컨테이너 실행

   * docker run [options] IMAGE[:TAG|@DIGEST] [COMMAND] [ARG...]

     | 옵션  | 설명                                                         |
     | ----- | ------------------------------------------------------------ |
     | -d    | detached mode(백그라운드)                                    |
     | -p    | 호스트와 컨테이너의 포트를 연결(포워딩)                      |
     | -v    | 호스트와 컨테이너의 디렉토리를 연결(마운트) 공유디렉터리 개념 |
     | -e    | 컨테이너 내에서 사용할 환경변수 설정                         |
     | -name | 컨테이너 이름 설정                                           |
     | -rm   | 프로세스 종료시 컨테이너 자동 제거                           |
     | -it   | -i와 -t를 동시에 사용한 것으로 터미널 입력을 위한 옵션       |
     | -link | 컨테이너 연결 [컨테이너명:별칭]                              |

3. 우분투 설치 및 실행 명령어

   1. 우분투 받아오기 (docker hub에서 확인 가능)
      * docker pull ubuntu:16.04
   2. 설치 확인
      * docker images
   3. 우분투 실행 (옵션없이 실행시 바로 종료됨)
      * docker run ubuntu:16.04

4. mariadb 설치 및 실행

   1. 설치

      * docker pull mariadb:latest

   2. 실행

      * docker run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mariadb mariadb:latest
      * 포트가 같은경우 오류가 생길 수 있음 이런 경우 / 포트를 변경하던가 기존 실행중인 mariadb를 중지해야함

   3. 포트 변경 후 실행

      * docker run -d -p 13306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true --name mariadb mariadb:latest
      * 이름 중복으로 실행이 되지 않을 경우 기존 컨테이너 삭제 필요

   4. 컨테이너 삭제 명령어

      * docker ps -a (id 확인)

      * docker container rm id 

   5. 실행 로그 확인

      * docker logs mariadb

   6. 컨테이너 접속

      * docker exec -it mariadb /bin/bash

        > 만약, git bash를 사용하면 에러가 날 수 있음 이럴때 명령어 앞에 winpty를 붙이면 됨
        >
        > ex) winpty docker exec -it mariadb bash
        >
        > 이게 귀찮으면 gitbash 재설치해서 설치 옵션으로 두번째 옵션(Use MinTTY 말고 Use Windows default console window)로 해야함

      * mysql -uroot -p -h127.0.0.1 (접속 후  DB확인)

   7. 컨테이너 접속 종료

      * exit

   8. 실행 종료

      * docker stop mariadb

   9. 재시작

      * docker start mariadb

5. 이미지 파일 생성

   1. 프로젝트 디렉토리로 이동

      1. pom.xml 프로젝트 version명을 변경(1.0)

   2. 다음 명령어로 빌드

      * mvn clean compile package -DskipTests=true
      * 기존 내용 clean 및 compile(테스트 파일없어도 스킵)

   3. Dockerfile 생성 (target과 같은 뎁스여야함)

      ```dockerfile
      # 이미지 이름 (docker hub에서 openjdk 검색 -> 태그)
      FROM openjdk:17-ea-jdk-slim
      # 가상 디렉토리
      VOLUME /tmp
      # 도커파일과 같은 뎁스에 있어야함
      COPY target/user-service-1.0.jar UserService.jar
      ENTRYPOINT ["java","-jar","UserService.jar"]
      ```

      

   4. docker build (프로젝트 디렉토리에서)

      * docker build --tag openprime15/user-service:1.0 .
      * tag 뒤에는 docker hub id값 입력

   5. 빌드 확인

      * docker image ls

   6. 푸시

      * docker push openprime15/user-service:1.0
      * 버전명을 뒤에 입력해야함

      > image 삭제 명령어
      >
      > docker rmi [이미지명]



* 구성된 모든 설정 및 파일 제거 명령어
  * docker system prune



6. 도커 브릿지 네트워크 관련 명령어
   * 브릿지 네트워크 생성
     * docker network create --driver bridge [브릿지 이름]
   * 네트워크 생성(고정 포트 사용을 위해 게이트웨이와 서브넷마스크 설정하는 것이 좋음)
     * docker network create --gateway 172.18.0.1 --subnet 172.18.0.0/16 ecommerce-network
   * 네트워크 설정 확인
     * docker network inspect [브릿지 이름]



#### Docker Container 배포

* 위의 생성한 이커머스 네트워크를 기반으로 생성

1. RabbitMQ

   1. 도커 실행

      * docker run -d --name rabbitmq --network ecommerce-network -p 15672:15672 -p 5672:5672 -p 15671:15671 -p 5671:5671 -p 4369:4369  -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:management

   2. 접속확인

      http://127.0.0.1:15672/

2. Config Service

   1. 소스 빌드 설정

      1. pom.xml 버전명 변경 : 1.0

      2. Dockerfile 작성

         ```dockerfile
         FROM openjdk:17-ea-jdk-slim
         VOLUME /tmp
         # Enc key 복붙
         COPY apiEncryptionKey.jks apiEncryptionKey.jks
         COPY target/config-service-1.0.jar ConfigService.jar
         ENTRYPOINT ["java","-jar","ConfigService.jar"]
         ```

      3. apiEncryptionKey.jks 파일 위치 변경

         1. 복사 후 config-service 위치로 붙여넣기

      4. bootstrap.yml 변경 (key 파일 위치 변경)

         ```yaml
         encrypt:
           key-store:
             location: file:///apiEncryptionKey.jks
             password: test1234
             alias: apiEncryptionKey
         ```

      5. application.yml 설정 변경(클라우드 부분)

         ```yaml
           cloud:
             config:
               server:
                 native:
                   search-locations: file:///${user.home}/study/microservice/native-file-repo
                 git:
                   uri: https://github.com/openprime15/spring-cloud-config-test.git
         ```

         

      6. 빌드

         * mvn clean compile package -DskipTests=true

   2. 도커 빌드설정

      1. 도커 빌드

         * docker build -t openprime15/config-service:1.0 .

      2. 도커 실행

         * host를 rabbitmq로 추가하면 ip변경 문제없이 편하게 사용가능

         * docker run -d -p 8888:8888 --network ecommerce-network -e "spring.rabbitmq.host=rabbitmq" -e "spring.profiles.active=default" --name config-service openprime15/config-service:1.0

3. Discovery Service

   1. 소스 빌드설정

      1. pom.xml 버전명 변경 : 1.0

      2. application.yml 설정 추가

         ```yaml
         spring:
           application:
             name: discoveryservice
           cloud:
             config:
               uri: http://127.0.0.1:8888
               name: ecommerce
         ```

      3.  Dockerfile 생성

         ```dockerfile
         FROM openjdk:17-ea-11-jdk-slim
         VOLUME /tmp
         COPY /target/discoveryservice-1.0.jar DiscoveryService.jar
         ENTRYPOINT ["java","-jar","DiscoveryServie.jar"]
         ```

         

   2. 도커 빌드설정

      1. 도커 빌드
         * docker build -t openprime15/discovery-service:1.0 .
      2. 도커 실행
         * docker run -d -p 8761:8761 --network ecommerce-network -e "spring.cloud.config.uri=http://config-service:8888" --name discovery-service openprime15/discovery-service:1.0

4. Apigateway Service

   * config / eureka / rabbitMQ 설정은 docker run에서 설정값 추가하여 사용

   1. 소스 빌드설정

      1. pom.xml 버전명 변경 : 1.0

      2. Dockerfile 생성

         ```dock
         FROM openjdk:17-ea-11-jdk-slim
         VOLUME /tmp
         COPY /target/apigateway-service-1.0.jar ApigatewayService.jar
         ENTRYPOINT ["java","-jar","ApigatewayService.jar"]
         ```

         

      3. 빌드

         * mvn clean compile package -DskipTests=true

   2. 도커 빌드설정

      1. 도커 빌드
         * docker build -t openprime15/apigateway-service:1.0 .
      2. 도커 실행
         * docker run -d -p 8000:8000 --network ecommerce-network -e "spring.cloud.config.uri=http://config-service:8888" -e "spring.rabbitmq.host=rabbitmq" -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" --name apigateway-service openprime15/apigateway-service:1.0

5. Maria DB

   1. 기존 mariadb 데이터 저장위치 확인(Windows 기준)

      * mariadb 접속해서 다음 명령어로 검색
      * show variables like 'datadir';

   2. Dockerfile 생성 (COPY 경로 확인하여 DB위치와 맞게 매핑할것)

      ```dockerfile
      FROM mariadb
      ENV MYSQL_ROOT_PASSWORD mysql
      ENV MYSQL_DATABASE mydb
      COPY ./mysql_data/mysql /var/lib/mysql
      EXPOSE 3306
      ENTRYPOINT ["mysqld", "--user=root"]
      ```

   3. docker 빌드 (Dockerfile 생성한 경로에서 입력)

      * docker build -t openprime15/my-mariadb:1.0 .

   4. docker 실행

      * docker run -d -p 3306:3306 --network ecommerce-network --name mariadb openprime15/my-mariadb:1.0

      * 포트가 겹치는경우 기존 포트실행을 중지하거나 포워딩 다시 해야함

        > Mariadb 중지방법 (Windows)
        >
        > cmd에서 다음 명령어 입력
        >
        > net stop mariadb
        >
        > 재실행이 필요한 경우 net start mariadb

   5. 실행 후 확인

      * docker exec -it mariadb bash
      * (windows는 앞에 winpty 추가)
      * mysql -h127.0.0.1 -uroot -p
      * 권한 허용
      * grant all privileges on *.* to 'root'@'%' identified by 'mysql';
      * 작업 반영
      * flush privileges; 

6. Kafka

   * docker-compose로 실행
     * git clone https://github.com/wurstmeister/kafka-docker.git
     * docker-compose-single-broker.yml 수정

   1. compose 설정

      1. git clone

      2. docker-compose-single-broker.yml 수정

         ```yaml
         version: '2'
         services:
           zookeeper:
             image: wurstmeister/zookeeper
             ports:
               - "2181:2181"
             #추가
             networks: 
               my-network:
                 ipv4_address: 172.18.0.100
           kafka:
             # 변경
             image: wurstmeister/kafka
             # build: .
             ports:
               - "9092:9092"
             environment:
               #변경
               KAFKA_ADVERTISED_HOST_NAME: 172.18.0.101
               # KAFKA_ADVERTISED_HOST_NAME: 192.168.99.100
               KAFKA_CREATE_TOPICS: "test:1:1"
               KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
             volumes:
               - /var/run/docker.sock:/var/run/docker.sock
             #추가
             depends_on: 
               - zookeeper
             networks: 
               my-network:
                 ipv4_address: 172.18.0.101
         # 추가
         networks: 
           my-network:
             name: ecommerce-network
         ```

         

   2. 도커 컴포즈 실행

      * docker-compose -f docker-compose-single-broker.yml up -d

   3. 구동 확인

      * docker ps -a

7. zipkin

   1. 도커 실행

      * ```
        docker run -d -p 9411:9411 \
         --network ecommerce-network \
         --name zipkin \
         openzipkin/zipkin 
        ```

8. Prometheus + Grafana (모니터링)

   1. 프로메테우스

      1. prometheus.yml을 이용해 설정정보 추가해서 실행

         * 도커 실행시 해당 yml 경로를 맞춰서 실행

         * prometheus.yml 수정

           ```yaml
               static_configs:
               # - targets: ['localhost:9090']
               - targets: ['prometheus:9090']
             - job_name: 'user-service'
               scrape_interval: 15s
               metrics_path: '/user-service/actuator/prometheus'
               static_configs:
               # - targets: ['localhost:8000']
               - targets: ['apigateway-service:8000']
             - job_name: 'order-service'
               scrape_interval: 15s
               metrics_path: '/order-service/actuator/prometheus'
               static_configs:
               # - targets: ['localhost:8000']
               - targets: ['apigateway-service:8000']
             - job_name: 'apigateway-service'
               scrape_interval: 15s
               metrics_path: '/actuator/prometheus'
               static_configs:
               # - targets: ['localhost:8000']
               - targets: ['apigateway-service:8000']
           ```

           

      2. 도커 실행

         * ```
           docker run -d -p 9090:9090 \
            --network ecommerce-network \
            --name prometheus \
            -v C:/Users/hinkk/study/monitering/prometheus-2.27.1.windows-amd64/prometheus.yml:/etc/prometheus/prometheus.yml \
            prom/prometheus 
           ```

   2. 그라파나

      1. 도커 실행

         * ```
           docker run -d -p 3000:3000 \
            --network ecommerce-network \
            --name grafana \
            grafana/grafana 
           ```

9. User Service

   * 먼저 config(git) 파일 변경

     * commerce.yml 의 gateway ip 변경 (docker inspect apigateway-service로 확인)

       ```yaml
       gateway:
         # ip: 127.0.0.1
         ip: 172.18.0.5
       ```

       

   1. 소스 빌드설정

      1. pom.xml 버전명 변경(1.0)

      2. Dockerfile 작성

         ```dockerfile
         # 이미지 이름 (docker hub에서 openjdk 검색 -> 태그)
         FROM openjdk:17-ea-jdk-slim
         # 가상 디렉토리
         VOLUME /tmp
         # 도커파일과 같은 뎁스에 있어야함
         COPY target/user-service-1.0.jar UserService.jar
         ENTRYPOINT ["java","-jar","UserService.jar"]
         ```

         

      3. 빌드 

         * mvn clean compile package -DskipTests=true

   2. 도커 빌드설정

      1. 빌드

         * docker build -t openprime15/user-service:1.0 .

      2. 배포

         * ```
           docker run -d --network ecommerce-network \
             --name user-service \
            -e "spring.cloud.config.uri=http://config-service:8888" \
            -e "spring.rabbitmq.host=rabbitmq" \
            -e "spring.zipkin.base-url=http://zipkin:9411" \
            -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" \
            -e "logging.file=/api-logs/users-ws.log" \
            openprime15/user-service:1.0
           ```

           

10. Order Service

    1. 소스 빌드설정

       1. pom.xml 버전명 변경(1.0)

       2. application.yml 의 datasource를 mariadb로 변경해야함

       3. KafkaProducerConfig의  IP 변경

          ```java
          //        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
                  properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.18.0.101:9092");
          ```

       4. Order Controller createOrder 변경

          ```java
              @PostMapping("/{userId}/orders")
              public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                               @RequestBody RequestOrder orderDetails){
          
                  log.info("Before add orders data");
                  ModelMapper mapper = new ModelMapper();
                  mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
          
          
                  OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
                  orderDto.setUserId(userId);
                  /* jpa */
                  OrderDto createOrder = orderService.createOrder(orderDto);
                  ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);
          
                  /* kafka */
          //        orderDto.setOrderId(UUID.randomUUID().toString());
          //        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());
          
                  /* send this order to the kafka */
                  // 토픽을 보냄
                  kafkaProducer.send("example-catalog-topic", orderDto);
          //        orderProducer.send("orders", orderDto);
          
          //        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);
          
                  log.info("After added orders data");
                  // 상태값 201
                  return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
          
              }
          ```

          

       5. Dockerfile 작성

          ```dockerfile
          FROM openjdk:17-ea-jdk-slim
          VOLUME /tmp
          COPY target/order-service-1.0.jar OrderService.jar
          ENTRYPOINT ["java","-jar","OrderService.jar"]
          ```

       6. 빌드

          * mvn clean compile package -DskipTests=true

    2. 도커 빌드설정

       1. 빌드

          * docker build -t openprime15/order-service:1.0 .

       2. 배포

          ```
          docker run -d --network ecommerce-network \
            --name order-service \
           -e "spring.zipkin.base-url=http://zipkin:9411" \
           -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" \
           -e "spring.datasource.url=jdbc:mariadb://mariadb:3306/mydb" \
           -e "logging.file=/api-logs/orders-ws.log" \
           openprime15/order-service:1.0
          ```

          

11. Catalog Service

    1. 소스 빌드설정

       1. pom.xml 버전 변경(1.0)

       2. Dockerfile 생성

          ```dockerfile
          FROM openjdk:17-ea-jdk-slim
          VOLUME /tmp
          COPY target/catalog-service-1.0.jar CatalogService.jar
          ENTRYPOINT ["java","-jar","CatalogService.jar"]
          ```

          

       3. KafkaConsumerConfig의  IP 변경

          ```java
          //        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
                  properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.18.0.101:9092");
          ```

          

       4. 빌드

          * mvn clean compile package -DskipTests=true

    2. 도커 빌드설정

       1. 빌드

          * docker build -t openprime15/catalog-service:1.0 .

       2. 배포

          ```
          docker run -d --network ecommerce-network \
            --name catalog-service \
           -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" \
           -e "logging.file=/api-logs/catalogs-ws.log" \
           openprime15/catalog-service:1.0
          ```

          

12. 프로필 설정방법

    1. config에서 yml 설정이 필요
    2. 또는 클래스 실행시 @Profile("dev") 등으로 설정가능
    3. 부트 실행시
       * mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=production
    4. 자바 커맨드 실행시
       * java -Dspring.profiles.active=default XXX.jar

