package org.example.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTest {

    @Test
    void toJsonAndFromJsonWorks() {
        Map<String, String> map = Map.of("key", "value");
        String json = JsonUtil.toJson(map);
        Map result = JsonUtil.fromJson(json, Map.class);

        assertEquals("value", result.get("key"));
    }

    @Test
    void emptyJsonProducesEmptyMap() {
        Map result = JsonUtil.fromJson("{}", Map.class);
        assertTrue(result.isEmpty());
    }
}
