package org.example.services;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.model.*;
import org.example.repository.ReceiveMessageRepository;
import org.example.repository.SendMessageRepository;
import org.example.repository.TodoRepository;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Properties;

@Service
@AllArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final SendMessageRepository sendMessageRepository;
    private final ReceiveMessageRepository receiveMessageRepository;

    /**
     * User 추가
     * @param request 추가될 User 이름, 이메일
     * @return 추가된 User Entity
     */
    public UserEntity add(UserRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(request.getName());
        userEntity.setEmail(request.getEmail());
        return this.userRepository.save(userEntity);
    }


    public UserSendMessagesEntity sendMessage(MessageRequest request){
        UserSendMessagesEntity userSendMessagesEntity = new UserSendMessagesEntity();
        userSendMessagesEntity.setSend_user_id(request.getSend_user_id());
        userSendMessagesEntity.setContents(request.getContents());

        sendKafkaMessage(userSendMessagesEntity);
        return this.sendMessageRepository.save(userSendMessagesEntity);
    }

    public void sendKafkaMessage(UserSendMessagesEntity data){
        Properties props = new Properties(); //Properties 오브젝트를 시작합니다.
        props.put("bootstrap.servers", "localhost:9091,localhost:9092,localhost:9093"); //브로커 리스트를 정의합니다.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //메시지 키와 벨류에 문자열을 지정하므로 내장된 StringSerializer를 지정합니다.
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props); //Properties 오브젝트를 전달해 새 프로듀서를 생성합니다
//        Producer<String, UserSendMessagesEntity> producer2 = new KafkaProducer<>(props); //Properties 오브젝트를 전달해 새 프로듀서를 생성합니다.

        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("peter-docker01", data.getContents());
            ProducerRecord<String, UserSendMessagesEntity> record2 = new ProducerRecord<>("peter-docker01", data);//ProducerRecord 오브젝트를 생성합니다.
            RecordMetadata metadata = producer.send(record).get(); //get() 메소드를 이용해 카프카의 응답을 기다립니다. 메시지가 성공적으로 전송되지 않으면 예외가 발생하고, 에러가 없다면 RecordMetadata를 얻게 됩니다.
//            RecordMetadata metadata2 = producer2.send(record2).get(); //get() 메소드를 이용해 카프카의 응답을 기다립니다. 메시지가 성공적으로 전송되지 않으면 예외가 발생하고, 에러가 없다면 RecordMetadata를 얻게 됩니다.
            System.out.printf("Topic: %s, Partition: %d, Offset: %d, Key: %s, Received Message: %s\n", metadata.topic(), metadata.partition()
                    , metadata.offset(), record.key(), record.value());

//            System.out.printf("Topic: %s, Partition: %d, Offset: %d, Key: %s, Received Message: %s\n", metadata2.topic(), metadata2.partition()
//                    , metadata2.offset(), record2.key(), record2.value());

        } catch (Exception e){
            e.printStackTrace(); //카프카로 메시지를 보내기 전과 보내는 동안 에러가 발생하면 예외가 발생합니다.
        } finally {
            producer.close(); // 프로듀서 종료
//            producer2.close(); // 프로듀서 종료
        }
    }

    public void sendAllUserMessage(String contents){
        List<UserEntity> userEntity =  userRepository.findAll();
        for (UserEntity entity : userEntity) {
            UserReceiveMessagesEntity userReceiveMessagesEntity = new UserReceiveMessagesEntity();
            userReceiveMessagesEntity.setReceive_user_id(entity);
            userReceiveMessagesEntity.setContents(contents);
            userReceiveMessagesEntity.setIs_agree(true);
            this.receiveMessageRepository.save(userReceiveMessagesEntity);
        }
    }

    /**
     * 특정 Todo 아이템 조회
     * @param id 조회랑 아이템 아이디
     * @return 조회된 Todo 엔티티
     *          해당 아이디가 존재하지 않을 경우 ResponseStatusException 발생
     */
    public UserEntity searchById(Long id) {
        return this.userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * 전체 Todo 아이템 목록 조회
     * @return 전체 Todo 엔티티 목록
     */
    public List<UserEntity> searchAll() {
        return this.userRepository.findAll();
    }

}
