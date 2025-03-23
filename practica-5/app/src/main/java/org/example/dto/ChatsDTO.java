package org.example.dto;

import java.util.Date;

public record ChatsDTO(String chatName, String room, String message, boolean heWroteIt, Date timestamp) {
}
