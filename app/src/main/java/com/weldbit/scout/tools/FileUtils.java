package com.weldbit.scout.tools;

import java.io.File;

public class FileUtils {
    private FileUtils() {
        // Hide constructor.
    }

    public static String filename(String fname) {
        int pos = fname.lastIndexOf(File.separator);
        if (pos > -1)
            return fname.substring(pos + 1);
        else
            return fname;
    }
}
