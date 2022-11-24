package floottymod.floottymod.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.hack.HackList;
import floottymod.floottymod.util.json.FsonObject;
import floottymod.floottymod.util.json.JsonException;
import floottymod.floottymod.util.json.JsonUtils;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SettingsFile {
    private final Path path;
    private final Map<String, Hack> hacksWithSettings;
    private boolean disableSaving;

    public SettingsFile(Path path, HackList hackList) {
        this.path = path;
        hacksWithSettings = createHackMap(hackList);
    }

    private Map<String, Hack> createHackMap(HackList hackList) {
        LinkedHashMap<String, Hack> map = new LinkedHashMap<>();
        for(Hack hack : hackList.getAllHacks()) if(!hack.getSettings().isEmpty()) map.put(hack.getName(), hack);
        return Collections.unmodifiableMap(map);
    }

    public void load() {
        try {
            FsonObject fson = JsonUtils.parseFileToObject(path);
            loadSettings(fson);
        } catch(NoSuchFileException e) {

        } catch(IOException | JsonException e) {
            System.out.println("Couldn't load " + path.getFileName());
            e.printStackTrace();
        }

        save();
    }

    private void loadSettings(FsonObject fson) {
        try {
            disableSaving = true;

            for(Entry<String, JsonObject> e : fson.getAllJsonObjects().entrySet()) {
                Hack hack = hacksWithSettings.get(e.getKey());
                if(hack == null) continue;
                loadSettings(hack, e.getValue());
            }
        } finally {
            disableSaving = false;
        }
    }

    private void loadSettings(Hack hack, JsonObject json) {
        Map<String, Setting> settings = hack.getSettings();

        for(Entry<String, JsonElement> e : json.entrySet()) {
            String key = e.getKey().toLowerCase();
            if(!settings.containsKey(key)) continue;
            settings.get(key).fromJson(e.getValue());
        }
    }

    public void save() {
        if(disableSaving) return;

        JsonObject json = createJson();

        try {
            JsonUtils.toJson(json, path);
        } catch(IOException | JsonException e) {
            System.out.println("Couldn't save " + path.getFileName());
            e.printStackTrace();
        }
    }

    private JsonObject createJson() {
        JsonObject json = new JsonObject();

        for(Hack hack : hacksWithSettings.values()) {
            Collection<Setting> settings = hack.getSettings().values();

            JsonObject jsonSettings = new JsonObject();
            settings.forEach(s -> jsonSettings.add(s.getName(), s.toJson()));

            json.add(hack.getName(), jsonSettings);
        }

        return json;
    }
}
