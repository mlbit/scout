package com.weldbit.scout.storage.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.weldbit.scout.logging.Log;
import com.weldbit.scout.storage.model.TableStructure;
import com.weldbit.scout.tools.FileUtils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * DataManagement service has the functionality to handle the following: 1.
 * During initialization of the application 1.1 Load the tables metadata
 */
public class DataManagement {
    private Map<String, TableStructure> tableDDL;

    public DataManagement(String resourceFilename) {
        tableDDL = new HashMap<>();
        Yaml yaml = new Yaml(new Constructor(TableStructure.class));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceFilename);

        for (Object object : yaml.loadAll(inputStream)) {
            if (object instanceof TableStructure) {
                String tablename = ((TableStructure) object).getTablename();
                tableDDL.put(tablename, ((TableStructure) object));
            }
        }

        tableDDL.forEach((k, v) -> {
            System.out.println("Key :" + k);
            System.out.println("Value :" + v);
        });

    }

    /**
     * Create table files and keys that are defined in the DDL 
     */
    private void createTablesAndKeys() {
        
        tableDDL.forEach(key, value) -> {
            // Create table file if NOT exists
            createDataTable(key,value);
            // Create index file if NOT exists
        }
    }

    /**
     * Create the file system if needed and set to for storing
     * 
     * @param tableName
     * @param tableStructure
     * @throws IOException
     */
    private void createTable(String tableName, TableStructure tableStructure) {

        String filename = FileUtils.tableFilename(tableName);
        Path pathData = Paths.get(filename);

        // Create the file if not exist.
        try (SeekableByteChannel writerStream = Files.newByteChannel(pathData,
                EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {
            Log.log("File created " + filename);
        } catch (IOException e) {
            Log.log(e);
        }
    }

    private void createSearchFiles(String tableName, TableStructure tableStructure) {

    }

    private void createUniqueFiles(String tableName, TableStructure tableStructure) {

    }

}
