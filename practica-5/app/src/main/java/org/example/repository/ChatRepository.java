package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.models.ChatMessage;
import org.example.util.BaseRepository;

import java.util.List;
import java.util.Optional;

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

    public List<ChatMessage> getUserChatHistory(String username) {
        String jpql = """
        SELECT m FROM ChatMessage m
        WHERE m.sender = :username OR m.recipient = :username
        ORDER BY m.timestamp ASC
    """;
        TypedQuery<ChatMessage> query = entityManager.createQuery(jpql, ChatMessage.class);
        query.setParameter("username", username);
        return query.getResultList();
    }


}
