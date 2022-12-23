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
