import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class CustomConfig {

    private FileConfiguration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    public CustomConfig(Plugin plugin) {
        this(plugin, "config.yml");
    }

    public CustomConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(file, false);
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public void setLocation(String path, Location loc) {
        if (config == null) reloadConfig();
        try {
            config.set(path + ".W", loc.getWorld().getName());
            config.set(path + ".X", loc.getX());
            config.set(path + ".Y", loc.getY());
            config.set(path + ".Z", loc.getZ());
            config.set(path + ".Yaw", String.valueOf(loc.getYaw()));
            config.set(path + ".Pitch", String.valueOf(loc.getPitch()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(String path) {
        if (config == null) reloadConfig();
        if (config.getString(path + ".W") == null) return null;
        if (Bukkit.getWorld(config.getString(path + ".W")) == null) return null;
        if (config.get(path + ".Yaw") == null && config.get(path + ".Pitch") == null) {
            return new Location(
                    Bukkit.getWorld(config.getString(path + ".W")),
                    config.getDouble(path + ".X"),
                    config.getDouble(path + ".Y"),
                    config.getDouble(path + ".Z")
            );
        } else {
            return new Location(
                    Bukkit.getWorld(config.getString(path + ".W")),
                    config.getDouble(path + ".X"),
                    config.getDouble(path + ".Y"),
                    config.getDouble(path + ".Z"),
                    Float.parseFloat(config.getString(path + ".Yaw")),
                    Float.parseFloat(config.getString(path + ".Pitch"))
            );
        }
    }
}
