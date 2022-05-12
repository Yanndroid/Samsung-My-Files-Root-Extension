package com.samsung.android.app.networkstoragemanager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class CachedFileList {
    private static final HashMap<String, ArrayList<Bundle>> sCachedData = new HashMap<>();

    public static boolean contains(String path) {
        return sCachedData.containsKey(path);
    }

    public static ArrayList<Bundle> get(String path) {
        return sCachedData.get(path);
    }

    public static void removeFileList(String path) {
        sCachedData.remove(path);
    }

    public static void clear() {
        sCachedData.clear();
    }

    public static ArrayList<Bundle> saveFileList(String filePath, ArrayList<Bundle> fileList) {
        sCachedData.put(filePath, fileList);
        return fileList;
    }
}
