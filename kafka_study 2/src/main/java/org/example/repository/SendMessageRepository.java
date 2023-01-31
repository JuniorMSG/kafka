package org.example.repository;

import org.example.model.UserEntity;
import org.example.model.UserSendMessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendMessageRepository extends JpaRepository<UserSendMessagesEntity, Long> {
}
