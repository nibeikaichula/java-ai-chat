package com.aichatbot.controller;

import com.aichatbot.dto.ConversationResponse;
import com.aichatbot.service.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*")
@Slf4j
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getAllConversations() {
        log.info("Fetching all conversations");
        List<ConversationResponse> conversations = conversationService.getAllConversations();
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponse> getConversation(@PathVariable Long id) {
        log.info("Fetching conversation with ID: {}", id);
        ConversationResponse conversation = conversationService.getConversationById(id);
        return ResponseEntity.ok(conversation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        log.info("Deleting conversation with ID: {}", id);
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}
