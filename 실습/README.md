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

    docker exec -it kafka /bin/bash
    kafka-console-consumer --bootstrap-server kafka:9092 --topic peter-docker01

### 4. 메세지 테스트

<img width="1359" alt="image" src="https://user-images.githubusercontent.com/22822369/210161172-251fc373-86d0-4337-972f-20c4b2785e95.png">




## cluster Setting

<img width="1256" alt="image" src="https://user-images.githubusercontent.com/22822369/212974072-56366de1-cf33-4d6a-b2db-2cfbadff675a.png">

## 예제 3-2 동기전송
    docker exec -it kafka1 /bin/bash
    docker exec -it kafka2 /bin/bash
    docker exec -it kafka3 /bin/bash

    kafka-console-consumer --bootstrap-server kafka1:9091 --topic peter-docker01
    kafka-console-consumer --bootstrap-server kafka2:9092 --topic peter-docker01
    kafka-console-consumer --bootstrap-server kafka3:9093 --topic peter-docker01
