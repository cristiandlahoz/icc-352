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

    // ➤ Guardar un mensaje
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


    // ➤ Obtener historial de chat entre dos usuarios
    public List<ChatMessage> findBySenderAndRecipientOrRecipientAndSender(String user1, String user2) {
        String jpql = """
            SELECT m FROM ChatMessage m
            WHERE (m.sender = :user1 AND m.recipient = :user2)
            OR (m.sender = :user2 AND m.recipient = :user1)
            ORDER BY m.timestamp ASC
        """;
        TypedQuery<ChatMessage> query = entityManager.createQuery(jpql, ChatMessage.class);
        query.setParameter("user1", user1);
        query.setParameter("user2", user2);
        return query.getResultList();
    }

    public List<ChatMessage> findAll() {
        String jpql = "SELECT m FROM ChatMessage m ORDER BY m.timestamp ASC";
        return entityManager.createQuery(jpql, ChatMessage.class).getResultList();
    }


    // ➤ Obtener el último mensaje entre dos usuarios
    public Optional<ChatMessage> findTopBySenderAndRecipientOrRecipientAndSenderOrderByTimestampDesc(String user1, String user2) {
        String jpql = """
            SELECT m FROM ChatMessage m
            WHERE (m.sender = :user1 AND m.recipient = :user2)
            OR (m.sender = :user2 AND m.recipient = :user1)
            ORDER BY m.timestamp DESC
        """;
        TypedQuery<ChatMessage> query = entityManager.createQuery(jpql, ChatMessage.class);
        query.setParameter("user1", user1);
        query.setParameter("user2", user2);
        query.setMaxResults(1);

        return query.getResultStream().findFirst();
    }


}
