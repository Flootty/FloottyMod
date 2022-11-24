package floottymod.floottymod.util.json;

import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;


import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public enum JsonUtils {
    ;

    public static final Gson GSON = new Gson();

    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    public static com.google.gson.JsonElement parseFile(Path path) throws IOException, JsonException {
        try(BufferedReader reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader);
        } catch(JsonParseException e) {
            if(e.getCause() instanceof MalformedJsonException c) throw new JsonException(c.getMessage(), c);

            throw new JsonException(e);
        }
    }

    public static FsonArray parseFileToArray(Path path) throws IOException, JsonException {
        return getAsArray(parseFile(path));
    }

    public static FsonObject parseFileToObject(Path path) throws IOException, JsonException {
        return getAsObject(parseFile(path));
    }

    public static JsonElement parseURL(String url) throws IOException, JsonException {
        URI uri = URI.create(url);
        try(InputStream input = uri.toURL().openStream()) {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return JsonParser.parseReader(bufferedReader);

        } catch(JsonParseException e) {
            if(e.getCause() instanceof MalformedJsonException c) throw new JsonException(c.getMessage(), c);

            throw new JsonException(e);
        }
    }

    public static FsonArray parseURLToArray(String url) throws IOException, JsonException {
        return getAsArray(parseURL(url));
    }

    public static FsonObject parseURLToObject(String url) throws IOException, JsonException {
        return getAsObject(parseURL(url));
    }

    /**
     * For more complex connections where {@link #parseURL(String)} won't do.
     */
    public static JsonElement parseConnection(URLConnection connection) throws IOException, JsonException {
        try(InputStream input = connection.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return JsonParser.parseReader(bufferedReader);

        } catch(JsonParseException e) {
            if(e.getCause() instanceof MalformedJsonException c) throw new JsonException(c.getMessage(), c);

            throw new JsonException(e);
        }
    }

    /**
     * For more complex connections where {@link #parseURLToArray(String)} won't
     * do.
     */
    public static FsonArray parseConnectionToArray(URLConnection connection) throws IOException, JsonException {
        return getAsArray(parseConnection(connection));
    }

    /**
     * For more complex connections where {@link #parseURLToObject(String)}
     * won't do.
     */
    public static FsonObject parseConnectionToObject(URLConnection connection) throws IOException, JsonException {
        return getAsObject(parseConnection(connection));
    }

    public static void toJson(JsonElement json, Path path) throws IOException, JsonException {
        try(BufferedWriter writer = Files.newBufferedWriter(path)) {
            JsonUtils.PRETTY_GSON.toJson(json, writer);
        } catch(JsonParseException e) {
            throw new JsonException(e);
        }
    }

    public static boolean isBoolean(JsonElement json) {
        if(json == null || !json.isJsonPrimitive()) return false;

        JsonPrimitive primitive = json.getAsJsonPrimitive();
        return primitive.isBoolean();
    }

    public static boolean getAsBoolean(JsonElement json) throws JsonException {
        if(!isBoolean(json)) throw new JsonException("Not a boolean: " + json);

        return json.getAsBoolean();
    }

    public static boolean getAsBoolean(JsonElement json, boolean fallback) {
        if(!isBoolean(json)) return fallback;

        return json.getAsBoolean();
    }

    public static boolean isNumber(JsonElement json) {
        if(json == null || !json.isJsonPrimitive()) return false;

        JsonPrimitive primitive = json.getAsJsonPrimitive();
        return primitive.isNumber();
    }

    public static int getAsInt(JsonElement json) throws JsonException {
        if(!isNumber(json)) throw new JsonException("Not a number: " + json);

        return json.getAsInt();
    }

    public static int getAsInt(JsonElement json, int fallback) {
        if(!isNumber(json)) return fallback;

        return json.getAsInt();
    }

    public static long getAsLong(JsonElement json) throws JsonException {
        if(!isNumber(json)) throw new JsonException("Not a number: " + json);

        return json.getAsLong();
    }

    public static long getAsLong(JsonElement json, long fallback) {
        if(!isNumber(json)) return fallback;

        return json.getAsLong();
    }

    public static boolean isString(JsonElement json) {
        if(json == null || !json.isJsonPrimitive()) return false;

        JsonPrimitive primitive = json.getAsJsonPrimitive();
        return primitive.isString();
    }

    public static String getAsString(JsonElement json) throws JsonException {
        if(!isString(json)) throw new JsonException("Not a string: " + json);

        return json.getAsString();
    }

    public static String getAsString(JsonElement json, String fallback) {
        if(!isString(json)) return fallback;

        return json.getAsString();
    }

    public static FsonArray getAsArray(JsonElement json) throws JsonException {
        if(!json.isJsonArray()) throw new JsonException("Not an array: " + json);

        return new FsonArray(json.getAsJsonArray());
    }

    public static FsonObject getAsObject(JsonElement json) throws JsonException {
        if(!json.isJsonObject()) throw new JsonException("Not an object: " + json);

        return new FsonObject(json.getAsJsonObject());
    }
}
