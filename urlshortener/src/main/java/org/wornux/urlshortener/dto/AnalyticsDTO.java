package org.wornux.urlshortener.dto;

public record AnalyticsDTO(
    String shortUrl, String accessDate, String browser, String ipAdress, String os) {}
