package edu.eci.dosw.tdd.core.util;

import java.util.UUID;

/**
 * Utility class for generating unique identifiers.
 */
public class IdGeneratorUtil {

    private IdGeneratorUtil() {}

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
