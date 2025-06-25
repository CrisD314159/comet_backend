package com.crisd.comet.services.implementations;

import com.crisd.comet.services.interfaces.IChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService implements IChatService {
}
