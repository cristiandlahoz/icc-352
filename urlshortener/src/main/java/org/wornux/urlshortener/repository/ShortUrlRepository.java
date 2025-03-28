package org.wornux.urlshortener.repository;

import org.wornux.urlshortener.model.ShortUrl;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static org.wornux.urlshortener.config.DatabaseConfig.getDatabase;

public class ShortUrlRepository {
    private static final MongoCollection<Document> collection = getDatabase().getCollection("short_urls");

    public static void saveShortUrl(ShortUrl shortUrl) {
        Document doc = new Document("originalUrl", shortUrl.getOriginalUrl())
                .append("shortUrl", shortUrl.getShortUrl())
                .append("createdAt", shortUrl.getCreatedAt());
        collection.insertOne(doc);
    }
}
