package me.decce.kerria;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("ReadWriteStringCanBeUsed") // For Java8 compatibility
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
    public boolean fastLightTextureUpload = false;
    public boolean cache = true;
    public int bufferSize = Constants.DEFAULT_BUFFER_SIZE;

    private static KerriaConfig createDefault() {
        var config = new KerriaConfig();
        config.validate();
        return config;
    }

    private static KerriaConfig load() {
        try {
            if (!Files.exists(PATH)) return createDefault();
            String json = new String(Files.readAllBytes(PATH), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            KerriaConfig config = gson.fromJson(json, KerriaConfig.class);
            config.validate();
            return config;
        } catch (IOException | JsonSyntaxException e) {
            Kerria.LOGGER.error("Failed to read configuration!", e);
        }
        return createDefault();
    }

    public static void reload() {
        Kerria.config = load();
    }

    public static void reset() {
        Kerria.config = createDefault();
        Kerria.getConfig().save();
    }

    public void validate() {
        if (bufferSize < Constants.MINIMUM_BUFFER_SIZE) {
            Kerria.LOGGER.warn("Specified buffer size {} is too small, clamped to {}", bufferSize, Constants.MINIMUM_BUFFER_SIZE);
            bufferSize = Constants.MINIMUM_BUFFER_SIZE;
        }
        if (bufferSize > Constants.MAXIMUM_BUFFER_SIZE) {
            Kerria.LOGGER.warn("Specified buffer size {} is too large, clamped to {}", bufferSize, Constants.MAXIMUM_BUFFER_SIZE);
            bufferSize = Constants.MAXIMUM_BUFFER_SIZE;
        }
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        try {
            Files.write(PATH, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            Kerria.LOGGER.error("Failed to save config file!", e);
        }
    }
}
