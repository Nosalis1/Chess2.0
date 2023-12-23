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

    private static int tabCount = 0;

    private static void addTabs() {
        for (int i = 0; i < tabCount; ++i)
            System.out.print('\t');
    }

    public static void initialize() {
        System.out.println("Loading assets...");
        tabCount++;
        loadDirectory(new File("C:\\Users\\Aleksa\\Desktop\\CS202\\Chess2.0\\src\\main\\resources"));
        tabCount = 0;
        System.out.println("All assets loaded!");
    }

    private static void loadDirectory(final File directory) {
        if (!directory.exists()) return;

        if (directory.isDirectory()) {
            addTabs();
            System.out.println("Loading directory : " + directory.getName());
            tabCount++;
            for (File entry : Objects.requireNonNull(directory.listFiles())) {
                loadDirectory(entry);
            }
        } else if (directory.isFile()) {
            loadFile(directory);
        }
    }

    private static void loadFile(final File file) {
        if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
            addTabs();
            System.out.println("Loading file : " + file.getName());

            String name = file.getName().substring(0, file.getName().endsWith(".png") ? file.getName().indexOf(".png") : file.getName().indexOf(".jpg"));
            images.put(
                    name,
                    new Image(file.getAbsolutePath())
            );

            addTabs();
            System.out.println("Loaded " + name);
        }
    }
}