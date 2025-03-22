package org.example.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

  @ManyToOne
  private User recipient;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private Date timestamp;

  @Column(nullable = false)
  private String room;

  public ChatMessage(String sender, User recipient, String message, String room) {
    this.sender = sender;
    this.recipient = recipient;
    this.message = message;
    this.room = room;
  }

  @PrePersist
  public void prePersist() {
    this.timestamp = new Date();
  }
}
