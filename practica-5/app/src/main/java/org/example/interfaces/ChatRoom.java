package org.example.interfaces;

import io.javalin.websocket.WsContext;

public interface ChatRoom {
  void sendMessage(String message, String username);

  boolean addUser(String username, WsContext ctx);

  void removeUser(String username);
}
