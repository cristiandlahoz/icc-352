package org.example.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.interfaces.ChatRoom;

import io.javalin.websocket.WsContext;

public class Room implements ChatRoom {
  private final Map<String, WsContext> users = new ConcurrentHashMap<>();

  @Override
  public void sendMessage(String message, String username) {
    users.values().forEach(ctx -> {
      if (ctx.session.isOpen() && !ctx.queryParam("user").equals(username)) {
        ctx.send(message);
      }
    });
  }

  @Override
  public void addUser(String username, WsContext ctx) {
    if (users.size() >= 2) {
      ctx.send("Chat is full");
      ctx.session.close();
      return;
    }
    users.put(username, ctx);
  }

  @Override
  public void removeUser(String username) {
    users.remove(username);
  }

}
