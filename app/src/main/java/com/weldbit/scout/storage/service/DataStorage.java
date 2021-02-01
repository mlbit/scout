package com.weldbit.scout.storage.service;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import com.weldbit.scout.storage.annotations.PrimaryKey;
import com.weldbit.scout.storage.annotations.WeldbitData;

import lombok.Data;

@Data
public class DataStorage<T> implements Closeable {

    private T objModel;
    private String systemFilename;
    private WeldbitData weldbitdata;
    private Class<?> clazz;
    private SeekableByteChannel writerStream;
    private SeekableByteChannel readerStream;
    private Path path;
    private ACCESS_TYPE accessType;

    public enum ACCESS_TYPE {
        READ_WRITE, READ, WRITE
    }

    public DataStorage(T objModel) {
        this(objModel, ACCESS_TYPE.READ);
    }

    public DataStorage(T objModel, ACCESS_TYPE aType) {
        // Open file and load into memory record management
        // 1. Retrieve tablename from class Object annotable
        this.objModel = objModel;
        this.accessType = aType;
        initialize();
        switch (aType) {
            case READ:
                this.openReaderFile();
                break;
            case WRITE:
                this.openWriterFile();
                break;
            case READ_WRITE:
                this.openReaderFile();
                this.openWriterFile();
                break;
        }

    }

    public void close() {
        try {
            writerStream.close();
            readerStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean initialize() {
        if (objModel == null) {
            System.out.println("Object to initialize is null.");
            return false;
        }

        this.clazz = objModel.getClass();

        if (!clazz.isAnnotationPresent(WeldbitData.class)) {
            System.out.println("The class :" + clazz.getSimpleName() + " is not annotated with WeldbitData.");
            return false;
        } else {
            this.weldbitdata = clazz.getAnnotation(WeldbitData.class);
            // Get the system filename that store the data
            this.systemFilename = weldbitdata.value().isEmpty() ? this.clazz.getSimpleName() : weldbitdata.value();
            path = Paths.get(systemFilename);

        }

        return true;
    }

    /**
     * Create the system if it doesn't exist and open the stream for
     * updating/inserting records
     *
     */
    private void openWriterFile() {
        try {
            writerStream = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));

            ByteBuffer buffer = ByteBuffer.wrap("Hello".getBytes());
            System.out.println("Size :" + writerStream.size());
            writerStream.position(writerStream.size() > 0 ? writerStream.size() - 1 : writerStream.size());
            while (buffer.hasRemaining()) {
                int write = writerStream.write(buffer);
                System.out.println("Number of written bytes:" + write);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open an existing filename for reading stream of data
     *
     */
    private void openReaderFile() {
        try {
            readerStream = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean insert() {
        if (this.objModel == null) {
            System.out.print("Model object can't be null");
            return false;
        }

        Class<?> clazz = objModel.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                try {
                    field.setAccessible(true);
                    System.out.println("");
                    System.out.println("Annotated Primary Key field." + field.getName() + ":" + field.get(objModel));
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
