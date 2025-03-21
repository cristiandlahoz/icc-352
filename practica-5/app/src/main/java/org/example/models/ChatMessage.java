package org.example.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String sender;

  @Column(nullable = false)
  private String recipient;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private Date timestamp;

  public ChatMessage(String sender, String recipient, String message) {
    this.sender = sender;
    this.recipient = recipient;
    this.message = message;
  }

  @PrePersist
  public void prePersist() {
    this.timestamp = new Date();
  }
}
