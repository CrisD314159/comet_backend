package com.crisd.comet.model;

import com.crisd.comet.model.enums.FriendRequestState;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Friendship implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private User requester;
    @ManyToOne
    private User recipient;
    private LocalDateTime dateAccepted;
    private boolean isBlocked;
}
