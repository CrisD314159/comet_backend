package com.crisd.comet.model;

import com.crisd.comet.model.enums.FriendRequestState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private User requester;
    @ManyToOne
    private User recipient;
    private LocalDateTime dateRequested;
    @Column(length = 100)
    private String message;
    private FriendRequestState state;

}
