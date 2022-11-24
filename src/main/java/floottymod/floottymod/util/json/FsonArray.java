package floottymod.floottymod.util.json;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class FsonArray {
    private final JsonArray json;

    public FsonArray(JsonArray json) {
        this.json = Objects.requireNonNull(json);
    }

    public boolean getBoolean(int index) throws JsonException {
        try {
            return JsonUtils.getAsBoolean(json.get(index));
        } catch(JsonException e) {
            throw new JsonException("Boolean at [" + index + "] not found.", e);
        }
    }

    public int getInt(int index) throws JsonException {
        try {
            return JsonUtils.getAsInt(json.get(index));
        } catch(JsonException e) {
            throw new JsonException("Number at [" + index + "] not found.", e);
        }
    }

    public long getLong(int index) throws JsonException {
        try {
            return JsonUtils.getAsLong(json.get(index));
        } catch(JsonException e) {
            throw new JsonException("Number at [" + index + "] not found.", e);
        }
    }

    public String getString(int index) throws JsonException {
        try {
            return JsonUtils.getAsString(json.get(index));
        } catch(JsonException e) {
            throw new JsonException("String at [" + index + "] not found.", e);
        }
    }

    public FsonArray getArray(int index) throws JsonException {
        try {
            return JsonUtils.getAsArray(json.get(index));
        } catch(JsonException e) {
            throw new JsonException("Array at [" + index + "] not found.", e);
        }
    }

    public FsonObject getObject(int index) throws JsonException {
        try {
            return JsonUtils.getAsObject(json.get(index));
        } catch(JsonException e) {
            throw new JsonException("Object at [" + index + "] not found.", e);
        }
    }

    public JsonElement getElement(int index) {
        return json.get(index);
    }

    public ArrayList<String> getAllStrings() {
        return StreamSupport.stream(json.spliterator(), false)
                .filter(JsonUtils::isString).map(JsonElement::getAsString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<FsonObject> getAllObjects() {
        return StreamSupport.stream(json.spliterator(), false)
                .filter(JsonElement::isJsonObject).map(JsonElement::getAsJsonObject)
                .map(FsonObject::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public JsonArray toJsonArray() {
        return json;
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
