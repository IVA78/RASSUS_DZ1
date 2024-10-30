package hr.fer.tel.rassus.server.utils;

import java.util.UUID;

public class IdentifierGenerator {
    public static String generateIdentifier() {
        // Generira jedinstveni identifikator
        UUID uuid = UUID.randomUUID();
        // Vraća ga kao String
        return uuid.toString();
    }
}
