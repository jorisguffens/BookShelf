package com.gufli.bookshelf.bukkit.worlds;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BukkitWorldData {

    protected String worldname;
    protected BukkitGeneratorType generatorType;
    protected HashMap<String, String> data = new HashMap<>();

    public BukkitWorldData(String world){
        this.worldname = world;

        File file = new File(Bukkit.getServer().getWorldContainer(), worldname + "/.worlddata");
        if ( !file.exists() ) return;

        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for ( String line : lines ) {
            String[] split = line.split("=");
            if ( split.length >= 2 ) {
                data.put(split[0].trim(), split[1].trim());
            }
        }

        if ( data.containsKey("generator") ) {
            generatorType = BukkitGeneratorType.getGenerator(data.get("generator"));
        }


    }

    public String getName() {
        return this.worldname;
    }

    public String getAlias() {
        return has("alias") ? get("alias") : worldname;
    }

    public BukkitGeneratorType getGenerator() {
        return generatorType;
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }


}
