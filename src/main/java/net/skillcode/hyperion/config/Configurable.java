package net.skillcode.hyperion.config;

import lombok.Getter;
import net.skillcode.hyperion.utils.Pair;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class Configurable implements ConfigData {

    private File file;
    private YamlConfiguration yamlConfiguration;

    /**
     * This method must be called in all inheriting classes
     */
    public void init() {
        file = new File(filePath());
        checkFile();
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        initFile();
    }

    public void saveConfig() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @Nullable Object get(final @NotNull String path) {
        return yamlConfiguration.get(path);
    }

    @Override
    public String getString(final @NotNull String path) {
        return Optional.ofNullable(yamlConfiguration.getString(path)).orElse("null").replace("&", "§");
    }

    @Override
    public boolean getBoolean(final @NotNull String path) {
        return yamlConfiguration.getBoolean(path);
    }

    @Override
    public int getInt(final @NotNull String path) {
        return yamlConfiguration.getInt(path);
    }

    @Override
    public double getDouble(final @NotNull String path) {
        return yamlConfiguration.getDouble(path);
    }

    @Override
    public long getLong(final @NotNull String path) {
        return yamlConfiguration.getLong(path);
    }

    @Override
    public ArrayList<String> getArrayList(final @NotNull String path) {
        return new ArrayList<>((Collection<? extends String>) yamlConfiguration.getList(path));
    }

    private void checkFile() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initFile() {
        final List<Pair<String, Object>> list = new ArrayList<>();
        configure(list);
        list.forEach(pair -> {
            if (yamlConfiguration.get(pair.getKey()) == null) {
                yamlConfiguration.set(pair.getKey(), pair.getValue());
            }
        });
        saveConfig();
    }

    //e.g /plugins/plugin/config.yml
    protected abstract String filePath();

    //<path, object>
    protected abstract void configure(final @NotNull List<Pair<String, Object>> list);

}
