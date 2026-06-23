package com.aichatbot.service;

import com.aichatbot.dto.ConversationResponse;
import com.aichatbot.dto.MessageResponse;
import com.aichatbot.entity.Conversation;
import com.aichatbot.entity.Message;
import com.aichatbot.repository.ConversationRepository;
import com.aichatbot.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationService(ConversationRepository conversationRepository,
                              MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getAllConversations() {
        return conversationRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConversationResponse getConversationById(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return convertToResponse(conversation);
    }

    @Transactional
    public void deleteConversation(Long id) {
        if (!conversationRepository.existsById(id)) {
            throw new RuntimeException("Conversation not found");
        }
        conversationRepository.deleteById(id);
        log.info("Conversation {} deleted", id);
    }

    private ConversationResponse convertToResponse(Conversation conversation) {
        List<MessageResponse> messages = conversation.getMessages().stream()
                .map(msg -> new MessageResponse(
                        msg.getId(),
                        msg.getConversation().getId(),
                        msg.getRole(),
                        msg.getContent(),
                        msg.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new ConversationResponse(
                conversation.getId(),
                conversation.getTitle(),
                messages,
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );
    }
}