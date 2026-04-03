package me.decce.kerria;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class KerriaConfig {
    private static final Path PATH;

	static {
		var directory = Paths.get("config");
		if (!Files.exists(directory)) {
			try {
				Files.createDirectory(directory);
			} catch (IOException ignored) {}
		}
		PATH = directory.resolve("kerria.json");
	}

    public boolean enabled = true;
    public boolean fastUpload = true;
    public boolean cache = true;
    public int minCacheSize = 1024;
    public int maxCacheSize = 65536 * 4;
    public int bufferSize = 4 * 1024 * 1024; // 4MB

    private static KerriaConfig createDefault() {
        return new KerriaConfig();
    }

    private static KerriaConfig load() {
        try {
            if (!Files.exists(PATH)) return createDefault();
            String json = Files.readString(PATH);
            Gson gson = new Gson();
            KerriaConfig config = gson.fromJson(json, KerriaConfig.class);
            return config;
        } catch (IOException e) {
            Kerria.LOGGER.error("Failed to read configuration!", e);
        }
        return createDefault();
    }

    public static void reload() {
        Kerria.config = load();
    }

    public static void reset() {
        Kerria.config = createDefault();
        Kerria.config.save();
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        try {
            Files.writeString(PATH, json, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            Kerria.LOGGER.error("Failed to save config file!", e);
        }
    }
}
