package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private int id;
    private UserEntity send_user_id;
    private UserEntity receive_user_id;
    private String contents;
    private boolean is_agree;
}
