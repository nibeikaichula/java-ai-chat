package com.aichatbot.service;

import com.aichatbot.dto.MessageRequest;
import com.aichatbot.dto.MessageResponse;
import com.aichatbot.entity.Conversation;
import com.aichatbot.entity.Message;
import com.aichatbot.repository.ConversationRepository;
import com.aichatbot.repository.MessageRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {

    private final OpenAiService openAiService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ChatService(OpenAiService openAiService,
                      ConversationRepository conversationRepository,
                      MessageRepository messageRepository) {
        this.openAiService = openAiService;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        log.info("Processing message: {}", request.getContent());

        Conversation conversation;

        // Create or get conversation
        if (request.getConversationId() != null) {
            conversation = conversationRepository.findById(request.getConversationId())
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));
        } else {
            conversation = new Conversation();
            conversation.setTitle(request.getContent().substring(0, Math.min(50, request.getContent().length())));
            conversation = conversationRepository.save(conversation);
        }

        // Save user message
        Message userMessage = new Message();
        userMessage.setConversation(conversation);
        userMessage.setRole("user");
        userMessage.setContent(request.getContent());
        messageRepository.save(userMessage);

        // Get conversation history
        List<Message> history = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());
        List<ChatMessage> chatMessages = history.stream()
                .map(msg -> new ChatMessage(msg.getRole(), msg.getContent()))
                .collect(Collectors.toList());

        // Call OpenAI API
        String aiResponse = callOpenAI(chatMessages);

        // Save AI response
        Message assistantMessage = new Message();
        assistantMessage.setConversation(conversation);
        assistantMessage.setRole("assistant");
        assistantMessage.setContent(aiResponse);
        messageRepository.save(assistantMessage);

        log.info("Message processed successfully");

        return convertToResponse(assistantMessage);
    }

    private String callOpenAI(List<ChatMessage> messages) {
        try {
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(messages)
                    .maxTokens(2048)
                    .temperature(0.7)
                    .build();

            var response = openAiService.createChatCompletion(request);
            return response.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("Error calling OpenAI API", e);
            throw new RuntimeException("Failed to get AI response: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getConversationHistory(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse convertToResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getRole(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}