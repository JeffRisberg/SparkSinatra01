package com.incra.sparkui.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * @author Enriko Aryanto
 * @since 1/21/16.
 */
public class JsonSerde {

    /**
     * Deserialize the specified json str into an object.
     *
     * @param str the specified json str.
     * @param theClass the class of the object
     * @return an instance of theClass populated by data from str
     * @throws IOException
     */
    public static <T> T deserialize(String str, Class<T> theClass) throws IOException {
        T t = new ObjectMapper().readValue(str, theClass);
        return t;
    }

    /**
     * Deserialize the specified json str into a List. The List may contain nested List or Map.
     *
     * @param str the specified json str.
     * @return List
     * @throws IOException
     */
    public static List<Object> deserializeJsonList(String str) throws IOException {
        JsonNode jsonNode = deserialize(str, JsonNode.class);
        return (List<Object>) parseJsonNode(jsonNode);
    }

    /**
     * Deserialize the specified json str into a Map. The Map may contain nested List or Map.
     *
     * @param str the specified json str.
     * @return Map
     * @throws IOException
     */
    public static Map<String, Object> deserializeJsonMap(String str) throws IOException {
        JsonNode jsonNode =  deserialize(str, JsonNode.class);
        return (Map<String, Object>) parseJsonNode(jsonNode);
    }

    public static String serializeToString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String serializeToPrettyString(Object obj) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object parseJsonNode(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            Map<String, Object> resultMap = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> nodeItr = jsonNode.fields();
            while (nodeItr.hasNext()) {
                Map.Entry<String, JsonNode> entry = nodeItr.next();
                resultMap.put(entry.getKey(), parseJsonNode(entry.getValue()));
            }
            return resultMap;
        }
        else if (jsonNode.isArray()) {
            List<Object> resultList = new ArrayList<>();
            for (JsonNode subNode: jsonNode) {
                resultList.add(parseJsonNode(subNode));
            }
            return resultList;
        }
        else if (jsonNode.isLong()) {
            return jsonNode.asLong();
        }
        else if (jsonNode.isInt()) {
            return jsonNode.asInt();
        }
        else if (jsonNode.isDouble()) {
            return jsonNode.asDouble();
        }
        else if (jsonNode.isBoolean()) {
            return jsonNode.asBoolean();
        }
        else if (jsonNode.isNull()) {
            return null;
        }
        else {
            return jsonNode.asText();
        }
    }
}
