package org.howtoswat.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void checkFile(File save, YamlConfiguration config, String name, List<Object> list) {
        if (!save.exists()) {
            try {
                save.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            DataUtils.saveValues(save, config, name, list);
        }
    }

    public static YamlConfiguration loadData(File save, String name, List<Object> list) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(save);
        DataUtils.checkFile(save, config, name, list);
        DataUtils.setValues(config, name, list);
        return config;
    }

    public static List<Object> getValues(YamlConfiguration config, String name) {
        return new ArrayList<>(config.getList(name));
    }

    public static void setValues(YamlConfiguration config, String name, List<Object> list) {
        for (Object value : config.getList(name)) list.add(String.valueOf(value));
    }

    public static void saveValues(File save, YamlConfiguration config, String name, List<Object> list) {
        config.set(name, list);

        try {
            config.save(save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
