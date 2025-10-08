package net.vxoidpvp.localization.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocaleData {

    private final Locale locale;
    private final Map<String, Object> messages;

    public LocaleData(Locale locale, JsonObject jsonObject) {
        this.locale = locale;
        this.messages = new HashMap<>();
        loadMessages(jsonObject);
    }

    private void loadMessages(JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonPrimitive()) {
                messages.put(key, value.getAsString());
            } else if (value.isJsonArray()) {
                List<String> list = new ArrayList<>();
                JsonArray array = value.getAsJsonArray();
                for (JsonElement element : array) {
                    if (element.isJsonPrimitive()) {
                        list.add(element.getAsString());
                    }
                }
                messages.put(key, list);
            }
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public String getMessage(String key) {
        Object value = messages.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<String> getMessageList(String key) {
        Object value = messages.get(key);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return null;
    }

    public boolean hasMessage(String key) {
        return messages.containsKey(key);
    }

    public Map<String, Object> getAllMessages() {
        return new HashMap<>(messages);
    }
}
