package org.example.repository;

import java.util.Date;
import java.util.List;

import org.example.dto.ChatsDTO;
import org.example.models.ChatMessage;
import org.example.util.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;

public class ChatRepository extends BaseRepository<ChatMessage, Long> {

  public ChatRepository(EntityManager entityManager) {
    super(entityManager, ChatMessage.class);
  }

  public ChatMessage save(ChatMessage chatMessage) {
    try {
      entityManager.getTransaction().begin();
      entityManager.persist(chatMessage);
      entityManager.getTransaction().commit();
      return chatMessage;
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      e.printStackTrace();
      return null;
    }
  }

  public List<ChatMessage> getUserChatHistory(String username, String room) {
    String jpql = """
            SELECT m FROM ChatMessage m
            WHERE (m.sender = :username OR m.recipient = :username) AND m.room = :room
            ORDER BY m.timestamp
        """;
    TypedQuery<ChatMessage> query = entityManager.createQuery(jpql, ChatMessage.class);
    query.setParameter("username", username).setParameter("room", room);
    return query.getResultList();
  }

  public List<ChatsDTO> getChatsByUsername(String username) {
    String jpql = """
            SELECT
                CASE
                    WHEN m.sender = :username THEN m.recipient
                    ELSE m.sender
                END AS chatUser,
                m.room AS room,
                m.message AS message,
                CASE
                    WHEN m.sender = :username THEN true
                    ELSE false
                END AS heWroteIt,
                m.timestamp AS timestamp
            FROM ChatMessage m
            WHERE (m.sender = :username OR m.recipient = :username)
            ORDER BY m.timestamp
        """;

    List<Tuple> result = entityManager.createQuery(jpql, Tuple.class)
        .setParameter("username", username)
        .getResultList();

    return result.stream().map(tuple -> new ChatsDTO(
        tuple.get("chatUser", String.class),
        tuple.get("room", String.class),
        tuple.get("message", String.class),
        tuple.get("heWroteIt", Boolean.class),
        tuple.get("timestamp", Date.class))).toList();
  }
}
