package nextstep.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toDto(String request, Class<T> type) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(request, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
