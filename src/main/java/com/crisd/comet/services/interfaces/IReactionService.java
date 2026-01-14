package com.crisd.comet.services.interfaces;

import java.util.UUID;

public interface IReactionService {
    void ReactToPost (UUID postId, UUID userReactsId);
    void RemoveReactToPost (UUID postId, UUID userReactsId);
}
