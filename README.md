<!-- TOC -->
  * [AWS Set](#aws-set)
  * [Docker Set](#docker-set)
  * [linux docker 설정 추가](#linux-docker-설정-추가)
  * [Topic 전송](#topic-전송)
    * [1. 토픽 생성](#1-토픽-생성)
    * [2. 프로듀서 실행](#2-프로듀서-실행)
    * [3. 컨슈머 실행](#3-컨슈머-실행)
    * [4. 메세지 테스트](#4-메세지-테스트)
  * [3.1. 카프카 기초 다지기](#31-카프카-기초-다지기)
    * [요약?](#요약)
    * [3.1.1 리플리케이션 (replication)](#311-리플리케이션--replication-)
      * [리플리케이션 팩터](#리플리케이션-팩터)
    * [3.1.2 파티션 (partition)](#312-파티션--partition-)
    * [3.1.3 세그먼트 (segment)](#313-세그먼트--segment-)
      * [Dokcer 기준 위치](#dokcer-기준-위치)
  * [카프카의 핵심 개념](#카프카의-핵심-개념)
    * [분산 시스템](#분산-시스템)
    * [페이지 캐시](#페이지-캐시)
    * [배치 전송 쿼리](#배치-전송-쿼리)
    * [압축 전송](#압축-전송)
    * [토픽, 파티션, 오프셋](#토픽-파티션-오프셋)
    * [고가용성 보장](#고가용성-보장)
    * [주키퍼의 의존성](#주키퍼의-의존성)
  * [프로듀서의 기본 동작과 예제](#프로듀서의-기본-동작과-예제)
    * [진행 과정](#진행-과정)
    * [프로듀서의 주요 옵션](#프로듀서의-주요-옵션)
    * [예제](#예제)
<!-- TOC -->

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

<img width="506" alt="image" src="https://user-images.githubusercontent.com/22822369/210160870-d22f1099-6649-43cf-bfb3-b51b4b00225c.png">
<img width="1245" alt="image" src="https://user-images.githubusercontent.com/22822369/210160949-6240668f-85d4-4568-ae14-15d5f39cdb4d.png">

## linux docker 설정 추가

    apt install sudo
    sudo apt install yum
    sudo apt-get update && sudo apt-get dist-upgrade
    apt-get install vim-common 

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

| 구성요소                         | 역할                                                 | 예시?    |
|------------------------------|----------------------------------------------------|--------|
| 주키퍼 (ZooKeeper)              | 아파치 프로젝트 애플리케이션명 카프카의 메타데이터 관리 및 브로커의 정상상태 점검을 담당함 ||
| 카프카, 카프카 클러스터                | 아파치 프로젝트 애플리케이션 명, 여러대의 브로커를 구성한 클러스트를 의미함.        ||
| 브로커 (broker)                 | 카프카 애플리케이션이 설치된 서버 또는 노드를 말합니다.                    ||
| 프로듀서 (producer)              | 카프카로 메세지를 보내는 역할을 하는 클라이언트를 총칭                     | 프로듀서는 카프카의 토픽으로 메세지를 전송하는 역할       |
| 컨슈머 (consumer)               | 카프카에서 메시지를 꺼내가는 역할을 하는 클라이언트를 총칭                   ||
| 토픽 (Topic)                   | 카프카는 메세지 피드들을 토픽으로 구분하고, 각 토픽의 이름은 카프카 내에서 고유함     | 이메일 주소 |
| 파티션 (partition)              | 병렬 처리 및 고성능을 얻기 위해 하나의 토픽을 여러개로 나눈 것을 말함           ||
| 세그먼트 (segment)               | 프로듀서가 전송한 실제 메시지가 브로커의 로컬 디스크에 저장되는 파일을 말함         ||   
| 메시지 (message) / 레코드 (record) | 프로듀서가 브로커로 전송하거나 컨슈머가 읽어가는 데이터 조각을 말함              ||

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

## 카프카의 핵심 개념

    카프카는 높은 처리량, 빠른 응답속도, 안정성을 가지고 있다.
    why?
    1. 분산 시스템
    2. 페이지 캐시
    3. 배치 전송 쿼리
    4. 압축 전송 
    5. 토픽, 파티션, 오프셋
    6. 고가용성 보장
    7. 주키퍼의 의존성

### 분산 시스템

    분산 시스템은 성능이 높다는 장점 이외에도 하나의 서버 또는 노드에 장애가 발생할 때 다른 서버/노드가 대응하므로
    장애 대응이 탁월하며, 부하가 높은 경우 시스템 확장이 용이하다는 장점을 가지고 있다.

### 페이지 캐시

    OS는 성능을 높히기 위해 페이지 캐시를 활용합니다.   
    디스크의 직접 I/O가 줄어드므로 성능이 향상됩니다.

### 배치 전송 쿼리

    한번에 일정 건수 이상을 전송하는 방식으로 네트워크 오버헤드를 줄이고 
    장기적으로는 더욱 빠르고 효율적으로 처리 할 수 있습니다

### 압축 전송

    gzip, snappy, lz4, zstd등의 압축을 지원하여 네트워크 대역폭이나 회선 비용을 줄입니다. 
    배치 전송과 결합하여 한번에 여러 파일을 압축하여 압축 효율이 올라갑니다.
    높은 압축률 - gzip, zstd
    빠른 응답 속도 - lz4, snappy 

### 토픽, 파티션, 오프셋

    카프카는 토픽이라는 곳에 데이터를 저장합니다.
    토픽은 메일 전송시스템의 이메일 주소 정도의 개념으로 이해하면 되고.
    토픽을 병렬처리를 위해서 여러개로 쪼개면 파티션
    파티션의 메시지가 저장되는 위치를 오프셋 이라고 모르며 (64비트 정수)로 되어있습니다.
    
[Dokcer 기준 위치](#dokcer-기준-위치)

### 고가용성 보장
    리플리케이션 기능을 제공하는데 
    토픽의 파티션을 복제하여 
    카프카에서는 리더와 팔로워로 부릅니다.
    토픽 생성시 리플리케이션 팩터 수를 지정 할 수 있습니다.
    많을수록 디스크 공간을 많이 사용하게 되므로 적절한 개수의 리플리케이션을 사용하는게 좋습니다.

### 주키퍼의 의존성
    주키퍼는 하둡의 서브 프로젝트중 하나로 아파치의 탑레벨 프로젝트(2011년 승격)입니다.
    주키퍼는 여러 대의 서버를 클러스터로 구성하고
    살아 있는 노드 수가 과반수 이상 유지된다면 서비스가 가능한 구조입니다.
    따라서 반드시 홀수 로 구성해야합니다.
    

## 프로듀서의 기본 동작과 예제
    프로듀서는 카프카의 토픽으로 메세지를 전송하는 역할 
    레코드는 토픽, 파티션, 키, 벨류로 구성됨.
    프로듀서는 카프카는 특정 토픽으로 메세지를 전송하므로 
    토픽과 벨류(메시지)는 필수값이며
    특정 파티션을 지정하기 위한 파티션, 레코드를 정렬하기 위한 키는 선택값입니다.

### 진행 과정
    각 레코드들은 프로듀서의 send() 메소드를 통해 
    시리얼라이저 (serializer), 파티셔너 (partitioner)를 거치게 됩니다.
    파티션을 지정했다면 파티셔너는 아무 동작하지 않으며
    파티션을 지정하지 않았다면 라운드 로빈 방식으로 작동합니다.
    
    레코드를 파티션별로 잠시 모아두고 배치 전송을 진행하여 
    실패하면 재시도 동작을 진행하고 일정 횟수 이상 실패할 경우 최종 실패하며
    성공하면 메타데이터를 전달합니다.

### 프로듀서의 주요 옵션
    패스!
    중복 없는 전송, 정확히 한 번 전송에 대한 실습등을 할때 사용합니다.

## 예제
    Java를 이용한 프로듀서
    docker exec -it kafka /bin/bash
    kafka-topics --bootstrap-server kafka:9092 --create --topic peter-basic01 --partitions 1 --replication-factor 1

![image](https://user-images.githubusercontent.com/22822369/210247185-9a36eab2-2a81-48e3-8af2-63d58afca257.png)

### 프로듀서의 전송방법
    메시지를 보내고 확인하지 않기
    동기 전송
    콜백
    비동기 전송



# 4장. 카프카의 내부 동작 원리와 구현
    리플리케이션의 동작
    애플리케이션의 고가동성을 위해 내부적으로 리플리케이션으로 동작합니다.
    
    리플리케이션
    리더
    팔로워
    포크, 복구
    컨트롤러, 컨트롤러 동작
    로그, 로그 컴팩션

    
    

    


