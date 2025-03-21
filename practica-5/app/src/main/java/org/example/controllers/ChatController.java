package org.example.controllers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.models.ChatMessage;
import org.example.services.ChatService;
import org.example.util.BaseController;
import org.example.util.Room;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ChatController extends BaseController {
  private static final Map<String, Room> rooms = new ConcurrentHashMap<>();
  private final ChatService chatService;

  public ChatController(Javalin app, ChatService chatService) {
    super(app);
    this.chatService = chatService;
  }

  @Override
  public void applyRoutes() {
    app.get("/chats", this::getChats);

    app.ws("/chats", ws -> {
      ws.onConnect(ctx -> {
        String roomName = ctx.queryParam("room");     // Sala de chat
        String userName = ctx.queryParam("user");     // Usuario remitente
        String recipient = ctx.queryParam("recipient"); // Destinatario

        // Verificar si recipient está presente
        if (recipient == null || recipient.isEmpty()) {
          ctx.send("Error: El destinatario no puede ser nulo.");
          return;
        }

        // Crear la sala si no existe
        Room room = rooms.computeIfAbsent(roomName, r -> new Room());

        // Añade el usuario a la sala
        room.addUser(userName, ctx);

        room.sendMessage("User " + userName + " joined the chat", userName);
      });

// ➤ Al recibir un mensaje

      ws.onMessage(ctx -> {
        String jsonMessage = ctx.message();

        try {
          ObjectMapper objectMapper = new ObjectMapper();
          JsonNode jsonNode = objectMapper.readTree(jsonMessage);

          String sender = jsonNode.get("sender").asText();
          String recipient = jsonNode.get("recipient").asText();
          String message = jsonNode.get("message").asText();

          System.out.println("✅ Mensaje recibido correctamente");
          System.out.println("📨 Sender: " + sender);
          System.out.println("📩 Recipient: " + recipient);
          System.out.println("💬 Message: " + message);

          // Guardar el mensaje en la base de datos
          chatService.saveMessage(sender, recipient, message);

          // Enviar el mensaje a la sala correspondiente
          Room room = rooms.get(jsonNode.get("room").asText());
          if (room != null) {
            room.sendMessage(message, sender);
          }

        } catch (Exception e) {
          System.out.println("❌ Error al procesar el mensaje JSON: " + e.getMessage());
        }
      });


      ws.onClose(ctx -> {
        String roomName = ctx.queryParam("room");
        String userName = ctx.queryParam("user");

        Room room = rooms.get(roomName);
        if (room != null) {
          room.removeUser(userName);
          room.sendMessage("User " + userName + " left the chat", userName);
        }
      });
    });
  }

  public void getChats(Context ctx) {
    ctx.render("pages/prueba_chat.html");
  }
}
