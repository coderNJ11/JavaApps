package utils;

import java.util.Map;

public class NestedObjectParser {
    public static Object findAnyKey(Map<String, Object> obj, String key) {
        if (obj.containsKey(key)) {
            return obj.get(key);
        }
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Object result = findAnyKey((Map<String, Object>) entry.getValue(), key); // Recursively search the nested object
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
