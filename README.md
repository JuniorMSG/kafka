# kafka
kafka Study


## 
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
git clone https://github.com/onlybooks/kafka2

<img width="506" alt="image" src="https://user-images.githubusercontent.com/22822369/210160870-d22f1099-6649-43cf-bfb3-b51b4b00225c.png">
