package com.smiley98.robot_path_planner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtils {
    private static final File sApplicationDirectory = new File(Environment.getExternalStorageDirectory() + "/Path_Planner");

    private static void createApplicationDirectory() {
        if (Environment.isExternalStorageManager() && !sApplicationDirectory.exists())
            sApplicationDirectory.mkdirs();
    }

    public static File root() { return sApplicationDirectory; }

    //Must ask permission to use external storage in Android 11.
    public static void init(MapsActivity activity, AppCompatButton saveButton, AppCompatButton loadButton) {
        ActivityResultLauncher<Intent> storagePermissionResultLauncher =
        activity.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResult result) -> {
                if (Environment.isExternalStorageManager())
                    createApplicationDirectory();
                else {
                    new AlertDialog.Builder(activity)
                        .setTitle("Error")
                        .setMessage("External storage access denied. Saving and loading has been disabled.")
                        .setPositiveButton("Ok", null)
                        .show();
                    saveButton.setEnabled(false);
                    loadButton.setEnabled(false);
                }
            }
        );

        if (!Environment.isExternalStorageManager())
            storagePermissionResultLauncher.launch(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
        createApplicationDirectory();
    }

    public static File create(String path) {
        File file = new File(sApplicationDirectory, path);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String read(File file) {
        String result = null;
        try {
            result = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void write(File file, String text) {
        try {
            Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> void serialize(File file, T data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> T deserialize(File file) {
        T data = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            data = (T) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}