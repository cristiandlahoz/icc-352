package org.example.services;

import org.example.dto.ChatsDTO;
import org.example.models.ChatMessage;
import org.example.models.User;
import org.example.repository.ChatRepository;

import java.util.List;
import java.util.Optional;

public class ChatService {

  private final ChatRepository chatRepository;
  private final UserService userService;

  public ChatService(ChatRepository chatRepository, UserService userService) {
    this.chatRepository = chatRepository;
    this.userService = userService;
  }

  public ChatMessage saveMessage(String sender, String recipient, String message, String room) {
    if (recipient == null || recipient.isEmpty()) {
      throw new IllegalArgumentException("El destinatario no puede ser nulo.");
    } else if (sender == null || sender.isEmpty()) {
      throw new IllegalArgumentException("El nombre de usuario no puede ser nulo.");
    } else if (message == null || message.isEmpty()) {
      throw new IllegalArgumentException("El mensaje no puede ser nulo.");
    } else if (room == null || room.isEmpty()) {
      throw new IllegalArgumentException("La sala de chat no puede ser nula.");
    } else if (sender.equals(recipient)) {
      throw new IllegalArgumentException("El remitente y el destinatario no pueden ser iguales.");
    }

    ChatMessage chatMessage = new ChatMessage(sender, recipient, message, room);
    return chatRepository.save(chatMessage);
  }

  public List<ChatMessage> getUserChatHistory(String username) {
    return chatRepository.getUserChatHistory(username);
  }

  public List<ChatsDTO> getChatsByUsername(String username) {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
    }
    return chatRepository.getChatsByUsername(username);
  }

}
