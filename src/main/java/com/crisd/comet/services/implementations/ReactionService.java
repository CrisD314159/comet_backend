package com.crisd.comet.services.implementations;

import com.crisd.comet.services.interfaces.IReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionService implements IReactionService {
    @Override
    public void ReactToPost(UUID postId, UUID userReactsId) {
        
    }

    @Override
    public void RemoveReactToPost(UUID postId, UUID userReactsId) {

    }
}
