package byakkono.debugstick;

import org.powernukkitx.Player;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.plugin.PluginBase;
import org.powernukkitx.utils.Config;
import org.powernukkitx.utils.TextFormat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * All texts are resolved once on load: bundled English, then lang/en.yml, then the language's own file.
 * Lookups after that are plain map reads.
 */
final class Messages {

    enum Key {
        ITEM_NAME("item.name"),
        ITEM_LORE("item.lore"),
        PLAYER_ONLY("message.player-only"),
        CREATIVE_ONLY("message.creative-only"),
        NO_PERMISSION("message.no-permission"),
        INVENTORY_FULL("message.inventory-full");

        private final String path;

        Key(String path) {
            this.path = path;
        }
    }

    private static final String DEFAULT_LANGUAGE = "en";
    private static final String AUTO = "auto";
    private static final List<String> BUNDLED = List.of("en", "ja");

    private final String language;
    private final Map<String, EnumMap<Key, Object>> table;

    private Messages(String language, Map<String, EnumMap<Key, Object>> table) {
        this.language = language;
        this.table = table;
    }

    static Messages load(PluginBase plugin) {
        BUNDLED.forEach(code -> plugin.saveResource("lang/" + code + ".yml"));

        EnumMap<Key, Object> english = read(bundledEnglish(plugin), new EnumMap<>(Key.class));
        Map<String, Config> files = readFolder(new File(plugin.getDataFolder(), "lang"));
        Config englishFile = files.get(DEFAULT_LANGUAGE);
        if (englishFile != null) {
            english = read(englishFile, english);
        }
        Map<String, EnumMap<Key, Object>> table = new HashMap<>();
        for (Map.Entry<String, Config> file : files.entrySet()) {
            table.put(file.getKey(), read(file.getValue(), english));
        }
        table.put(DEFAULT_LANGUAGE, english);

        String language = plugin.getConfig().getString("language", DEFAULT_LANGUAGE).toLowerCase(Locale.ROOT);
        if (!language.equals(AUTO) && !table.containsKey(language)) {
            plugin.getLogger().warning("lang/" + language + ".yml not found, using " + DEFAULT_LANGUAGE);
            language = DEFAULT_LANGUAGE;
        }
        return new Messages(language, table);
    }

    String text(CommandSender sender, Key key) {
        return (String) entries(sender).get(key);
    }

    @SuppressWarnings("unchecked")
    List<String> lines(CommandSender sender, Key key) {
        return (List<String>) entries(sender).get(key);
    }

    private EnumMap<Key, Object> entries(CommandSender sender) {
        String code = language;
        if (code.equals(AUTO)) {
            code = sender instanceof Player player && player.getLocale() != null
                    ? player.getLocale().getLanguage()
                    : DEFAULT_LANGUAGE;
        }
        EnumMap<Key, Object> entries = table.get(code);
        return entries != null ? entries : table.get(DEFAULT_LANGUAGE);
    }

    private static EnumMap<Key, Object> read(Config config, EnumMap<Key, Object> base) {
        EnumMap<Key, Object> entries = new EnumMap<>(base);
        for (Key key : Key.values()) {
            if (key == Key.ITEM_LORE) {
                List<String> lines = config.getStringList(key.path);
                if (!lines.isEmpty()) {
                    entries.put(key, lines.stream().map(Messages::color).toList());
                }
            } else {
                String text = config.getString(key.path, null);
                if (text != null) {
                    entries.put(key, color(text));
                }
            }
        }
        // a key missing everywhere still shows something instead of null
        for (Key key : Key.values()) {
            entries.putIfAbsent(key, key == Key.ITEM_LORE ? List.of() : key.path);
        }
        return entries;
    }

    private static Map<String, Config> readFolder(File folder) {
        Map<String, Config> files = new HashMap<>();
        File[] yamls = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (yamls != null) {
            for (File file : yamls) {
                String code = file.getName().substring(0, file.getName().length() - ".yml".length());
                files.put(code.toLowerCase(Locale.ROOT), new Config(file, Config.YAML));
            }
        }
        return files;
    }

    private static Config bundledEnglish(PluginBase plugin) {
        Config config = new Config(Config.YAML);
        try (InputStream in = plugin.getResource("lang/" + DEFAULT_LANGUAGE + ".yml")) {
            if (in != null) {
                config.load(in);
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Could not read the bundled messages: " + e.getMessage());
        }
        return config;
    }

    private static String color(String text) {
        return TextFormat.colorize('&', text);
    }
}
