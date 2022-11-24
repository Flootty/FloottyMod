package floottymod.floottymod.hack;

import com.google.gson.JsonArray;
import floottymod.floottymod.util.json.FsonArray;
import floottymod.floottymod.util.json.JsonException;
import floottymod.floottymod.util.json.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class EnabledHacksFile {
    private final Path path;
    private boolean disableSaving;

    public EnabledHacksFile(Path path) {
        this.path = path;
    }

    public void load(HackList hackList) {
        try {
            FsonArray fson = JsonUtils.parseFileToArray(path);
            enableHacks(hackList, fson);
        } catch(NoSuchFileException e) {
            // The file doesn't exist yet. No problem, we'll create it later.

        } catch(IOException | JsonException e) {
            System.out.println("Couldn't load " + path.getFileName());
            e.printStackTrace();
        }

        save(hackList);
    }

    private void enableHacks(HackList hackList, FsonArray wson) {
        try {
            disableSaving = true;

            for(Hack hack : hackList.getAllHacks()) hack.setEnabled(false);

            for(String name : wson.getAllStrings()) {
                Hack hack = hackList.getHackByName(name);
                if(hack == null || !hack.isStateSaved()) continue;

                hack.setEnabled(true);
            }

        }finally {
            disableSaving = false;
        }
    }

    public void save(HackList hackList) {
        if(disableSaving) return;

        JsonArray json = createJson(hackList);

        try {
            JsonUtils.toJson(json, path);
        } catch(IOException | JsonException e) {
            System.out.println("Couldn't save " + path.getFileName());
            e.printStackTrace();
        }
    }

    private JsonArray createJson(HackList hackList) {
        Stream<Hack> enabledHacks = hackList.getAllHacks().stream().filter(Hack::isEnabled).filter(Hack::isStateSaved);

        JsonArray json = new JsonArray();
        enabledHacks.map(Hack::getName).forEach(name -> json.add(name));

        return json;
    }
}
