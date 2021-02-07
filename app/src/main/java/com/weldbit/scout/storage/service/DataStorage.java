package com.weldbit.scout.storage.service;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import com.weldbit.scout.storage.annotations.WeldbitData;
import com.weldbit.scout.storage.model.DataHeader;
import com.weldbit.scout.tools.FileUtils;

import lombok.Data;

@Data
public class DataStorage<T> implements Closeable {

    private final String FILE_EXT = ".dad";
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
            if (writerStream != null)
                writerStream.close();
            if (readerStream != null)
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
            this.systemFilename = (weldbitdata.value().isEmpty() ? this.clazz.getSimpleName() : weldbitdata.value())
                    + FILE_EXT;
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
            var fileExist = Files.exists(path);
            writerStream = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
            if (!fileExist) {
                String pureFilename = FileUtils.filename(path.toString());
                var dataHeader = new DataHeader();
                dataHeader.setFilename(pureFilename);

                String jsonheader;
                try {
                    ByteBuffer buffer = ByteBuffer.wrap(dataHeader.getBytes());
                    while (buffer.hasRemaining()) {
                        writerStream.write(buffer);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

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

    public boolean insert() throws IOException {
        if (this.objModel == null) {
            System.out.print("Model object can't be null");
            return false;
        }
        StringBuilder fieldsInfo = new StringBuilder();
        try {
            fieldsInfo.append(AnnotationProcessor.jsonString(objModel));
            fieldsInfo.append(System.lineSeparator());

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        ByteBuffer buffer = ByteBuffer.wrap(fieldsInfo.toString().getBytes());
        System.out.println("Size :" + writerStream.size());
        writerStream.position(writerStream.size());
        while (buffer.hasRemaining()) {
            int write = writerStream.write(buffer);
            System.out.println("Number of written bytes:" + write);
        }

        System.out.println(fieldsInfo);
        return true;
    }

    public String readRecord() {
        if (this.objModel == null) {
            System.out.println("Model object can't be null");
            return null;
        }
        readerStream.

    }
}
