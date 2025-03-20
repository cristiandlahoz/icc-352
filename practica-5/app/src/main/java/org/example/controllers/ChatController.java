package org.example.controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.util.BaseController;
import org.example.util.Room;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class ChatController extends BaseController {
  private static final Map<String, Room> rooms = new ConcurrentHashMap<>();

  public ChatController(Javalin app) {
    super(app);
  }

  @Override
  public void applyRoutes() {
    app.get("/chats", this::getChats);

    app.ws("/chats", ws -> {
      ws.onConnect(ctx -> {
        String roomName = ctx.queryParam("room");
        String userName = ctx.queryParam("user");
        Room room = rooms.computeIfAbsent(roomName, r -> new Room());
        room.addUser(userName, ctx);
        room.sendMessage("User " + userName + " joined the chat", userName);
      });
      ws.onMessage(ctx -> {
        String roomName = ctx.queryParam("room");
        String userName = ctx.queryParam("user");
        Room room = rooms.get(roomName);
        if (room != null) {
          room.sendMessage(userName + ": " + ctx.message(), userName);
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
    ctx.render("pages/chat.html");
  }
}
