package com.chenbang.intellij.plugin.api.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private static final int BUFFER_SIZE = 16 * 1024;

    public static void zip(String srcPathname, OutputStream out, boolean includeSelf, Charset charset) {
        File srcFile = new File(srcPathname);
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out, charset);
            if (srcFile.isDirectory()) {
                if (includeSelf) {
                    doZip(zos, srcFile, srcFile.getName() + File.separator);
                } else {
                    File[] files = srcFile.listFiles();
                    for (File file : files) {
                        if (file.isDirectory()) {
                            doZip(zos, file, file.getName() + File.separator);
                        } else {
                            doZip(zos, file, "");
                        }
                    }
                }
            } else {
                doZip(zos, srcFile, "");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public static void zip(String srcPathname, String zipPathname, boolean includeSelf, Charset charset) {
        File srcFile = new File(srcPathname);
        File zipFile = new File(zipPathname);
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile), charset);
            if (srcFile.isDirectory()) {
                if (includeSelf) {
                    doZip(zos, srcFile, srcFile.getName() + File.separator);
                } else {
                    File[] files = srcFile.listFiles();
                    for (File file : files) {
                        if (file.isDirectory()) {
                            doZip(zos, file, file.getName() + File.separator);
                        } else {
                            doZip(zos, file, "");
                        }
                    }
                }
            } else {
                doZip(zos, srcFile, "");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private static void doZip(ZipOutputStream zos, File srcFile, String baseDir) {
        if (srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    doZip(zos, file, baseDir + file.getName() + File.separator);
                } else {
                    doZip(zos, file, baseDir);
                }
            }
        } else {
            byte[] buffer = new byte[BUFFER_SIZE];
            InputStream in = null;
            try {
                in = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(baseDir + srcFile.getName()));
                int n;
                while ((n = in.read(buffer)) != -1) {
                    zos.write(buffer, 0, n);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }
    }

    private ZipUtils() {
    }
}
