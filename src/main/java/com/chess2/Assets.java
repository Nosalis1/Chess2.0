package com.chess2;

import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Assets {
    private static final Map<String, Image> images = new HashMap<>();

    public static Image getImage(final String name) {
        return images.get(name);
    }


    public static void initialize() {
        Console.log(Console.INFO, "Loading assets...");
        double timestamp = System.currentTimeMillis();
        loadDirectory(new File(Objects.requireNonNull(Game.class.getResource("/Assets/")).getPath()));
        Console.log(Console.INFO, "All assets loaded!");
        Console.log(Console.INFO, "Loading exec time: " + (System.currentTimeMillis() - timestamp) + "ms");
    }

    private static void loadDirectory(final File directory) {
        if (!directory.exists()) return;

        if (directory.isDirectory()) {
            Console.log(Console.INFO, "Loading directory: " + directory.getName());
            for (File entry : Objects.requireNonNull(directory.listFiles())) {
                loadDirectory(entry);
            }
        } else if (directory.isFile()) {
            loadFile(directory);
        }
    }

    private static void loadFile(final File file) {
        if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
            Console.log(Console.INFO, "Loading file: " + file.getName());
            String name = file.getName().substring(0, file.getName().endsWith(".png") ? file.getName().indexOf(".png") : file.getName().indexOf(".jpg"));
            images.put(
                    name,
                    new Image(file.getAbsolutePath())
            );
            Console.log(Console.INFO, "File loaded: " + name);
        }
    }
}