package org.example.repository;

import org.example.model.UserReceiveMessagesEntity;
import org.example.model.UserSendMessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiveMessageRepository extends JpaRepository<UserReceiveMessagesEntity, Long> {
}
