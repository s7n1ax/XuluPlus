/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.alpha432.oyvey.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Bind;
import me.alpha432.oyvey.features.setting.EnumConverter;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.manager.FriendManager;
import me.alpha432.oyvey.util.Util;

public class ConfigManager
implements Util {
    public ArrayList<Feature> features = new ArrayList();
    public String config = "Xuluplus/config/";

    public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
        switch (setting.getType()) {
            case "Boolean": {
                setting.setValue(element.getAsBoolean());
                return;
            }
            case "Double": {
                setting.setValue(element.getAsDouble());
                return;
            }
            case "Float": {
                setting.setValue(Float.valueOf(element.getAsFloat()));
                return;
            }
            case "Integer": {
                setting.setValue(element.getAsInt());
                return;
            }
            case "String": {
                String str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                return;
            }
            case "Bind": {
                setting.setValue(new Bind.BindConverter().doBackward(element));
                return;
            }
            case "Enum": {
                try {
                    EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue(value == null ? setting.getDefaultValue() : value);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                return;
            }
        }
        OyVey.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
    }

    private static void loadFile(JsonObject input, Feature feature) {
        for (Map.Entry entry : input.entrySet()) {
            String settingName = (String)entry.getKey();
            JsonElement element = (JsonElement)entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    OyVey.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            boolean settingFound = false;
            for (Setting setting : feature.getSettings()) {
                if (!settingName.equals(setting.getName())) continue;
                try {
                    ConfigManager.setValueFromJson(feature, setting, element);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                settingFound = true;
            }
            if (!settingFound) continue;
        }
    }

    public void loadConfig(String name) {
        List files = Arrays.stream((Object[])Objects.requireNonNull(new File("Xuluplus").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        this.config = files.contains(new File("Xuluplus/" + name + "/")) ? "Xuluplus/" + name + "/" : "Xuluplus/config/";
        OyVey.friendManager.onLoad();
        for (Feature feature : this.features) {
            try {
                this.loadSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
    }

    public boolean configExists(String name) {
        List files = Arrays.stream((Object[])Objects.requireNonNull(new File("Xuluplus").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        return files.contains(new File("Xuluplus/" + name + "/"));
    }

    public void saveConfig(String name) {
        this.config = "Xuluplus/" + name + "/";
        File path = new File(this.config);
        if (!path.exists()) {
            path.mkdir();
        }
        OyVey.friendManager.saveFriends();
        for (Feature feature : this.features) {
            try {
                this.saveSettings(feature);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.saveCurrentConfig();
    }

    public void saveCurrentConfig() {
        File currentConfig = new File("Xuluplus/currentconfig.txt");
        try {
            if (currentConfig.exists()) {
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("Xuluplus", ""));
                writer.close();
            } else {
                currentConfig.createNewFile();
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("Xuluplus", ""));
                writer.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadCurrentConfig() {
        File currentConfig = new File("Xuluplus/currentconfig.txt");
        String name = "Xuluplus";
        try {
            if (currentConfig.exists()) {
                Scanner reader = new Scanner(currentConfig);
                while (reader.hasNextLine()) {
                    name = reader.nextLine();
                }
                reader.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public void resetConfig(boolean saveConfig, String name) {
        for (Feature feature : this.features) {
            feature.reset();
        }
        if (saveConfig) {
            this.saveConfig(name);
        }
    }

    public void saveSettings(Feature feature) throws IOException {
        String featureName;
        Path outputFile;
        JsonObject object = new JsonObject();
        File directory = new File(this.config + this.getDirectory(feature));
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (!Files.exists(outputFile = Paths.get(featureName = this.config + this.getDirectory(feature) + feature.getName() + ".json", new String[0]), new LinkOption[0])) {
            Files.createFile(outputFile, new FileAttribute[0]);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson((JsonElement)this.writeSettings(feature));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile, new OpenOption[0])));
        writer.write(json);
        writer.close();
    }

    public void init() {
        this.features.addAll(OyVey.moduleManager.modules);
        this.features.add(OyVey.friendManager);
        String name = this.loadCurrentConfig();
        this.loadConfig(name);
    }

    private void loadSettings(Feature feature) throws IOException {
        String featureName = this.config + this.getDirectory(feature) + feature.getName() + ".json";
        Path featurePath = Paths.get(featureName, new String[0]);
        if (!Files.exists(featurePath, new LinkOption[0])) {
            return;
        }
        this.loadPath(featurePath, feature);
    }

    private void loadPath(Path path, Feature feature) throws IOException {
        InputStream stream = Files.newInputStream(path, new OpenOption[0]);
        try {
            ConfigManager.loadFile(new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject(), feature);
        }
        catch (IllegalStateException e) {
            OyVey.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
            ConfigManager.loadFile(new JsonObject(), feature);
        }
        stream.close();
    }

    public JsonObject writeSettings(Feature feature) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        for (Setting setting : feature.getSettings()) {
            if (setting.isEnumSetting()) {
                EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
                object.add(setting.getName(), converter.doForward((Enum)setting.getValue()));
                continue;
            }
            if (setting.isStringSetting()) {
                String str = (String)setting.getValue();
                setting.setValue(str.replace(" ", "_"));
            }
            try {
                object.add(setting.getName(), jp.parse(setting.getValueAsString()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public String getDirectory(Feature feature) {
        String directory = "";
        if (feature instanceof Module) {
            directory = directory + ((Module)feature).getCategory().getName() + "/";
        }
        return directory;
    }
}

