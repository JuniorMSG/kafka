## AWS Set

    sudo amazon-linux-extras install -y ansible2
    sudo yum install -y git
    git clone https://github.com/onlybooks/kafka2  
    scp -i keypair.pem keypair.pem ec2-user@000

    chmod 600 keypair.pem
    ssh-agent bash
    ssh-add keypair.pem

    cd /home/ec2-user/kafka2/chapter2/ansible_playbook

    ansible-playbook -i hosts zookeeper.yml

## Docker Set

    git clone https://github.com/onlybooks/kafka2
    cd kafka2/appendix_C/single_zk_kafka
    docker-compose up -d
    docker ps 


## linux docker 설정 추가
    apt install sudo
    sudo apt install yum
    sudo apt-get update && sudo apt-get dist-upgrade

    apt-get install vim-common 


<img width="506" alt="image" src="https://user-images.githubusercontent.com/22822369/210160870-d22f1099-6649-43cf-bfb3-b51b4b00225c.png">

<img width="1245" alt="image" src="https://user-images.githubusercontent.com/22822369/210160949-6240668f-85d4-4568-ae14-15d5f39cdb4d.png">

## Topic 전송

    카프카를 사용하려면 결국 카프카로 메세지를 주고받아야 한다.

### 1. 토픽 생성

    프로듀서가 카프카로 메시지를 전송할 때는 카프카로 전송하는 것이 아니라 카프카의 특정 토픽으로 전송합니다.

    docker exec -it kafka /bin/bash
    kafka-topics --bootstrap-server kafka:9092 --create --topic peter-docker01 --partitions 1 --replication-factor 1
    

    kafka-console-producer --bootstrap-server kafka:9092 --topic peter-docker01

<img width="927" alt="image" src="https://user-images.githubusercontent.com/22822369/210160993-34339b47-a431-43a6-9fb0-7e39cfc66169.png">

### 2. 프로듀서 실행

    docker exec -it kafka /bin/bash
    kafka-console-producer --bootstrap-server kafka:9092 --topic peter-docker01

### 3. 컨슈머 실행

    docker exec -it kafka /bin/bash\
    kafka-console-producer --bootstrap-server kafka:9092 --topic peter-docker01

### 4. 메세지 테스트

<img width="1359" alt="image" src="https://user-images.githubusercontent.com/22822369/210161172-251fc373-86d0-4337-972f-20c4b2785e95.png">

## 3.1. 카프카 기초 다지기

    카프카를 구성하는 주요 요소

| 구성요소                         | 역할                                                 |
|------------------------------|----------------------------------------------------|
| 주키퍼 (ZooKeeper)              | 아파치 프로젝트 애플리케이션명 카프카의 메타데이터 관리 및 브로커의 정상상태 점검을 담당함 |
| 카프카, 카프카 클러스터                | 아파치 프로젝트 애플리케이션 명, 여러대의 브로커를 구성한 클러스트를 의미함.        |
| 브로커 (broker)                 | 카프카 애플리케이션이 설치된 서버 또는 노드를 말합니다.                    |
| 프로듀서 (producer)              | 카프카로 메세지를 보내는 역할을 하는 클라이언트를 총칭                     |
| 컨슈머 (consumer)               | 카프카에서 메시지를 꺼내가는 역할을 하는 클라이언트를 총칭                   |
| 토픽 (Topic)                   | 카프카는 메세지 피드들을 토픽으로 구분하고, 각 토픽의 이름은 카프카 내에서 고유함     |
| 파티션 (partition)              | 병렬 처리 및 고성능을 얻기 위해 하나의 토픽을 여러개로 나눈 것을 말함           |
| 세그먼트 (segment)               | 프로듀서가 전송한 실제 메시지가 브로커의 로컬 디스크에 저장되는 파일을 말함         |   
| 메시지 (message) / 레코드 (record) | 프로듀서가 브로커로 전송하거나 컨슈머가 읽어가는 데이터 조각을 말함              |

### 요약?
    토픽으로 메세지를 전송하고
    컨슈머를 이용해 메세지를 읽는다.
    카프카에선 각 메시지들을 저장한다.
    프로듀서를 이용해 보낸 브로커로 전송된 메시지는 토픽의 파티션에 저장되고
    각 메시지는 세그먼트라는 로그 파일의 형태로 브로커의 로컬 디스크에 저장된다.
    

### 3.1.1 리플리케이션 (replication)

    replication이란 각 메시ㅣ지들을 여러개로 복제해서 카프카 클러스터 내 브로커들에게 분산시키는 동작
    하나의 브로커가 종료되더라도 카프카는 안정성을 유지 할 수 있다. But 브로커 리소스를 많이 사용하게 된다.
    --replication-factor 3 

#### 리플리케이션 팩터

* 테스트나 개발 환경 : 1
* 운영 환경 (로그성 메시지로서 약간의 유실 허용) : 2
* 운영 환경(유실 허용하지 않음) : 3

### 3.1.2 파티션 (partition)
    병렬 처리 및 고성능을 얻기 위해 하나의 토픽을 여러개로 나눈 것을 말함
    여러개로 나누면 분산 처리가 가능하고 나뉜 파티션 수 만큼 컨슈머를 연결 가능하다.
    늘린 파티션 수는 절대 줄일 수 없습니다.
    초기 토픽 생성시에는 파티션수를 2, 4로 적게 생성한 후 메세지 처리량이나 상황에 따라 늘려 나가나는 방법을 사용하는게 좋습니다.
    https://eventsizer.io

### 3.1.3 세그먼트 (segment)
    프로듀서가 전송한 실제 메시지가 브로커의 로컬 디스크에 저장되는 파일을 말함 


#### Dokcer 기준 위치
    /var/lib/kafka/data
    /var/lib/kafka/data/peter-docker01-0

<img width="511" alt="image" src="https://user-images.githubusercontent.com/22822369/210171480-670aad4d-15ab-46ad-9d25-1d498293b4d3.png">

## 3.2 카프카의 핵심 개념
    카프카는 높은 처리량, 빠른 응답속도, 안정성을 가지고 있다.
    why?
    1. 분산 시스템
    2. 페이지 캐시
    3. 배치 전송 쿼리
    4. 압축 전송 
    5. 토픽, 파티션, 오프셋
    6. 고가용성 보장
    7. 주키퍼의 의존성
    


###



