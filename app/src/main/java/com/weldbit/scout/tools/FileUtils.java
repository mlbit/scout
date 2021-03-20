package com.weldbit.scout.tools;

import java.io.File;

import com.weldbit.scout.storage.network.NetworkNodesInfo;

public class FileUtils {

    public static String DATA_FILE_EXT = ".dfc"; // The file extension of the Data File Content. Data File Content store
    // the actual data stored in the file system.
    public static String INDEX_FILE_EXT = ".ifc"; // Index File Content store the record index of the file content in
                                                  // DFC.
                                                  // This file can be regenerated from the actual content file.

    private FileUtils() {
        // Hide constructor.
    }

    /**
     * Table filename is constructed using the following criteria - Filename format
     * will be <rarFilename>_blk_<block#>.<DATA_FILE_EXT>
     * 
     * @param rawFilename
     * @return
     */
    public static String tableFilename(String rawFilename) {
        return rawFilename + "_blk_" + NetworkNodesInfo.getBlocknumber() + DATA_FILE_EXT;
    }

    /**
     * Table staging filename is constructed using the following criteria - Filename
     * format will be <rarFilename>_stg_<block#>.<DATA_FILE_EXT>
     * 
     * @param rawFilename
     * @return
     */
    public static String tableStageFilename(String rawFilename) {

    }

    public static String searchkeyFilename() {

    }

    public static String uniquekeyFilename() {

    }

    public static String filename(String fname) {
        int pos = fname.lastIndexOf(File.separator);
        if (pos > -1)
            return fname.substring(pos + 1);
        else
            return fname;
    }
}
