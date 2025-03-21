package org.example.services;

import org.example.models.ChatMessage;
import org.example.repository.ChatRepository;

import java.util.List;
import java.util.Optional;

public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatMessage saveMessage(String sender, String recipient, String message) {
        if (recipient == null || recipient.isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede ser nulo.");
        }

        ChatMessage chatMessage = new ChatMessage(sender, recipient, message);
        return chatRepository.save(chatMessage);
    }

    public List<ChatMessage> getUserChatHistory(String username) {
        return chatRepository.getUserChatHistory(username);
    }



}
