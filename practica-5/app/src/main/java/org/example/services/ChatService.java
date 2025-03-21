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

    // ➤ Guardar un mensaje en la base de datos
    public ChatMessage saveMessage(String sender, String recipient, String message) {
        if (recipient == null || recipient.isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede ser nulo.");
        }

        ChatMessage chatMessage = new ChatMessage(sender, recipient, message);
        return chatRepository.save(chatMessage);
    }

    // ➤ Obtener historial completo entre dos usuarios
    public List<ChatMessage> getChatHistory(String user1, String user2) {
        return chatRepository.findBySenderAndRecipientOrRecipientAndSender(user1, user2);
    }

    // ➤ Obtener el último mensaje enviado entre dos usuarios
    public Optional<ChatMessage> getLastMessage(String user1, String user2) {
        return chatRepository.findTopBySenderAndRecipientOrRecipientAndSenderOrderByTimestampDesc(user1, user2);
    }
}
