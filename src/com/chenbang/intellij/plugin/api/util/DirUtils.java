package com.chenbang.intellij.plugin.api.util;

import java.io.File;

public final class DirUtils {
    public static void mkdir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private DirUtils() {
    }
}
