package com.chenbang.intellij.plugin.api.util;

import java.io.File;

public final class DirUtils {
    public static void mkdir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static boolean exists(String path){
        File file = new File(path);
        return file.exists();
    }

    private DirUtils() {
    }
}
