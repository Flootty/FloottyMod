package floottymod.floottymod.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Objects;

public class FsonObject {
    private final JsonObject json;

    public FsonObject(JsonObject json)  {
        this.json = Objects.requireNonNull(json);
    }

    public boolean getBoolean(String key) throws JsonException  {
        try {
            return JsonUtils.getAsBoolean(json.get(key));

        } catch(JsonException e) {
            throw new JsonException("Boolean \"" + key + "\" not found.", e);
        }
    }

    public int getInt(String key) throws JsonException {
        try {
            return JsonUtils.getAsInt(json.get(key));

        } catch(JsonException e) {
            throw new JsonException("Number \"" + key + "\" not found.", e);
        }
    }

    public long getLong(String key) throws JsonException {
        try {
            return JsonUtils.getAsLong(json.get(key));

        } catch(JsonException e) {
            throw new JsonException("Number \"" + key + "\" not found.", e);
        }
    }

    public String getString(String key) throws JsonException {
        try {
            return JsonUtils.getAsString(json.get(key));

        } catch(JsonException e) {
            throw new JsonException("String \"" + key + "\" not found.", e);
        }
    }

    public FsonArray getArray(String key) throws JsonException {
        try {
            return JsonUtils.getAsArray(json.get(key));

        } catch(JsonException e) {
            throw new JsonException("Array \"" + key + "\" not found.", e);
        }
    }

    public FsonObject getObject(String key) throws JsonException {
        try {
            return JsonUtils.getAsObject(json.get(key));

        } catch(JsonException e) {
            throw new JsonException("Object \"" + key + "\" not found.", e);
        }
    }

    public JsonElement getElement(String key) {
        return json.get(key);
    }

    public LinkedHashMap<String, String> getAllStrings() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        for(Entry<String, JsonElement> entry : json.entrySet()) {
            JsonElement value = entry.getValue();
            if(!JsonUtils.isString(value)) continue;

            map.put(entry.getKey(), value.getAsString());
        }
        return map;
    }

    public LinkedHashMap<String, Number> getAllNumbers() {
        LinkedHashMap<String, Number> map = new LinkedHashMap<>();

        for(Entry<String, JsonElement> entry : json.entrySet()) {
            JsonElement value = entry.getValue();
            if(!JsonUtils.isNumber(value)) continue;

            map.put(entry.getKey(), value.getAsNumber());
        }
        return map;
    }

    public LinkedHashMap<String, JsonObject> getAllJsonObjects() {
        LinkedHashMap<String, JsonObject> map = new LinkedHashMap<>();

        for(Entry<String, JsonElement> entry : json.entrySet()) {
            JsonElement value = entry.getValue();
            if(!value.isJsonObject()) continue;

            map.put(entry.getKey(), value.getAsJsonObject());
        }
        return map;
    }

    public boolean has(String memberName) {
        return json.has(memberName);
    }

    public JsonObject toJsonObject() {
        return json;
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
