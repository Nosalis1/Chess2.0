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
        loadDirectory(new File("C:\\Users\\Aleksa\\Desktop\\CS202\\Chess2.0\\src\\main\\resources"));
    }

    private static void loadDirectory(final File directory) {
        if (!directory.exists()) return;

        if (directory.isDirectory()) {
            for (File entry : Objects.requireNonNull(directory.listFiles())) {
                loadDirectory(entry);
            }
        } else if (directory.isFile()) {
            loadFile(directory);
        }
    }

    private static void loadFile(final File file) {
        if (file.getName().endsWith(".png")) {
            String name = file.getName().substring(0, file.getName().indexOf(".png"));
            images.put(
                    name,
                    new Image(file.getAbsolutePath())
            );
            System.out.println("Loaded " + name);
        }
    }
}