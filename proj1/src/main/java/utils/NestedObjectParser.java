package utils;
import org.json.JSONObject;

public class NestedObjectParser {
    public static void main(String[] args) {
        // Example JSON object
        String jsonStr = "{\"data\":{\"id\":\"123\",\"nestedData\":{\"routeId\":\"B1\",\"deeplyNested\":{\"routeId\":\"C1\",\"otherField\":\"value\"}}}}";
        JSONObject jsonObject = new JSONObject(jsonStr);

        String keyToFind = "routeId";
        String foundValue = findAnyKey(jsonObject, keyToFind);
        if (foundValue != null) {
            System.out.println("Found value for key '" + keyToFind + "': " + foundValue);
        } else {
            System.out.println("Key '" + keyToFind + "' not found in the JSON object.");
        }
    }

    public static String findAnyKey(JSONObject obj, String key) {
        if (obj.has(key)) {
            return obj.getString(key);
        }
        for (String k : obj.keySet()) {
            if (obj.get(k) instanceof JSONObject) {
                String result = findAnyKey(obj.getJSONObject(k), key); // Recursively search the nested object
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
