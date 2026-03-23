package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorUtilTest {

    @Test
    void shouldGenerateNonNullId() {
        assertNotNull(IdGeneratorUtil.generateId());
    }

    @Test
    void shouldGenerateNonBlankId() {
        assertFalse(IdGeneratorUtil.generateId().isBlank());
    }

    @Test
    void shouldGenerateUniqueIds() {
        String id1 = IdGeneratorUtil.generateId();
        String id2 = IdGeneratorUtil.generateId();
        assertNotEquals(id1, id2);
    }

}