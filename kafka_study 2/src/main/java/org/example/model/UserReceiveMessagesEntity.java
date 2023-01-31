package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_receive_messages", catalog = "kafka")
public class UserReceiveMessagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="receive_user_id")
    private UserEntity receive_user_id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Boolean is_agree;

}
