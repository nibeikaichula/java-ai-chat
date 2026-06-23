package com.aichatbot.controller;

import com.aichatbot.dto.MessageRequest;
import com.aichatbot.dto.MessageResponse;
import com.aichatbot.service.ChatService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        log.info("Received chat message");
        MessageResponse response = chatService.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getHistory(@PathVariable Long conversationId) {
        log.info("Fetching conversation history for ID: {}", conversationId);
        List<MessageResponse> history = chatService.getConversationHistory(conversationId);
        return ResponseEntity.ok(history);
    }
}
