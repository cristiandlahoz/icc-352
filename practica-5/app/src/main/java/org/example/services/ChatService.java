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

    Optional<User> destinatario = userService.getUserByUsername(recipient);
    return destinatario.map(user -> {
      ChatMessage chatMessage = new ChatMessage(sender, user, message, room);
      System.out.println("HOLLAJLDAJLAFJLAJFLKAJFLJ: " + chatMessage.getRecipient().getUsername());
      chatRepository.save(chatMessage);
      return chatMessage;
    }).orElseThrow(() -> new IllegalArgumentException("El destinatario no existe."));
  }

  public List<ChatMessage> getUserChatHistory(String username) {
    return chatRepository.getUserChatHistory(username);
  }

  public List<ChatsDTO> getChatsByUsername(String username) {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
    } else if (userService.getUserByUsername(username).isEmpty()) {
      throw new IllegalArgumentException("El usuario no existe.");
    }
    return chatRepository.getChatsByUsername(username);
  }

}
