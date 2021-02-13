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
import com.weldbit.scout.storage.model.IndexFile;
import com.weldbit.scout.tools.FileUtils;

import lombok.Data;

@Data
public class DataStorage<T> implements Closeable {

    private final String FILE_EXT = ".dad";
    private final String FILE_IDX = ".idx";
    private T objModel;
    private String systemDataFilename;
    private String systemIdxFilename;

    private WeldbitData weldbitdata;
    private Class<?> clazz;
    private SeekableByteChannel writerStream;
    private SeekableByteChannel writerIndexStream;

    private SeekableByteChannel readerStream;
    private SeekableByteChannel readerIndexStream;

    private Path pathData;
    private Path pathIdx;
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
        System.out.println("Closing Stream");
        try {
            if (writerStream != null)
                writerStream.close();
            if (readerStream != null)
                readerStream.close();
            if (writerIndexStream != null)
                writerIndexStream.close();
            if (readerIndexStream != null)
                readerIndexStream.close();

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
            this.systemDataFilename = (weldbitdata.value().isEmpty() ? this.clazz.getSimpleName() : weldbitdata.value())
                    + FILE_EXT;
            pathData = Paths.get(systemDataFilename);

            // Create index file.
            this.systemIdxFilename = (weldbitdata.value().isEmpty() ? this.clazz.getSimpleName() : weldbitdata.value())
                    + FILE_IDX;
            pathIdx = Paths.get(systemIdxFilename);

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
            var pathDataExist = Files.exists(pathData);

            writerStream = Files.newByteChannel(pathData,
                    EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
            if (!pathDataExist) {
                addHeaderInfo(writerStream, pathData);
            }
            createIndexFile(pathIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add Header information to the new file that's created
     *
     * @param path - the filename
     */
    private void addHeaderInfo(SeekableByteChannel fileStream, Path path) {
        // Add header information if it's a new file.
        String pureFilename = FileUtils.filename(path.toString());
        var dataHeader = new DataHeader();
        dataHeader.setFilename(pureFilename);
        System.out.println("DataHeader : " + dataHeader.toString());

        String jsonheader;
        try {
            ByteBuffer buffer = ByteBuffer.wrap(dataHeader.getBytes());
            while (buffer.hasRemaining()) {
                fileStream.write(buffer);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Create the an index file that will be use to quickly search records.
     *
     * @param path - filename information
     */
    private void createIndexFile(Path path) {
        try {
            this.writerIndexStream = Files.newByteChannel(path,
                    EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
            addHeaderInfo(writerIndexStream, path);
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
            readerStream = Files.newByteChannel(pathData, EnumSet.of(StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean insert() throws IOException {
        if (this.objModel == null) {
            System.out.print("Model object can't be null");
            return false;
        }
        StringBuilder hashInfo = new StringBuilder();
        StringBuilder fieldsInfo = new StringBuilder();

        try {
            AnnotationProcessor<T> annotationProcessor = new AnnotationProcessor<>(objModel);
            fieldsInfo.append(annotationProcessor.jsonString());
            fieldsInfo.append(System.lineSeparator());

            hashInfo.append(annotationProcessor.primaryKeyHash());

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

        IndexFile indexFile = new IndexFile();
        indexFile.setHashIdx(hashInfo.toString());
        ByteBuffer idxBuffer = ByteBuffer.wrap(indexFile.toString().getBytes());
        // Set the position on where to start writing the data
        System.out.println("writerIndexStream : " + (writerIndexStream != null ? "YES" : "NULL"));
        long indexSize = writerIndexStream.size();
        System.out.print(("Index Size :" + indexSize));
        writerIndexStream.position(indexSize);
        while (idxBuffer.hasRemaining()) {
            int write = writerIndexStream.write(idxBuffer);
            System.out.println("Index size save:" + write);
        }
        System.out.println(indexFile);

        return true;
    }

    public String readRecord() {
        if (this.objModel == null) {
            System.out.println("Model object can't be null");
            return null;
        }
        return "none";

    }
}
