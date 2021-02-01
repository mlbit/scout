package com.weldbit.scout.storage.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import javax.xml.catalog.Catalog;

public class TableLedgerDAO implements DataAccessBase {

    private String filename = "tableledger.dad"; // Decentralize Access Data

    @Override
    public boolean createTable() {

        String str = "Hello";

        var path = Paths.get(filename);
        var be = Files.exists(path);
        System.out.println("What is " + be);
        BasicFileAttributes data;
        try {
            if (be) {
                data = Files.readAttributes(path, BasicFileAttributes.class);
                System.out.println("Is a directory? " + data.isDirectory());
                System.out.println("Is a regular file? " + data.isRegularFile());
                System.out.println("Is a symbolic link? " + data.isSymbolicLink());
                System.out.println("Size (in bytes): " + data.size());
                System.out.println("Last modified: " + data.lastModifiedTime());

            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try (SeekableByteChannel outputStream = Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {

            ByteBuffer buffer = ByteBuffer.wrap("Hello".getBytes());
            System.out.println("Size :" + outputStream.size());
            outputStream.position(outputStream.size() > 0 ? outputStream.size() - 1 : outputStream.size());
            while (buffer.hasRemaining()) {
                int write = outputStream.write(buffer);
                System.out.println("Number of written bytes:" + write);
            }

            // outputStream.write(str.getBytes());
            // outputStream.close();

            // FileChannel fChannel = outputStream.getChannel();
            // System.out.println("Position :" + fChannel.position());
            // fChannel.position(3);
            // outputStream.write(str.getBytes());

            // long tailfile = fChannel.size();
            // fChannel.position(tailfile);

            // ByteBuffer buf = ByteBuffer.allocate(10);
            // buf.clear();
            // buf.put(str.getBytes());
            // while (buf.hasRemaining()) {
            // System.out.println("With value." + buf.toString());
            // fChannel.write(buf);
            // }
            // fChannel.force(true);
            // fChannel.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String readTable() {
        String dataInfo = null;
        // try (RandomAccessFile reader = new RandomAccessFile(filename, "r")) {
        // reader.seek(0);
        // byte[] bytes = new byte[5];
        // reader.read(bytes);
        // reader.close();
        // System.out.println(new String(bytes));

        // // FileChannel inChannel = reader.getChannel();
        // // ByteBuffer buf = ByteBuffer.allocate(1024);
        // // buf.clear();
        // // int bytesRead = inChannel.read(buf);

        // dataInfo = new String(bytes, StandardCharsets.UTF_8);

        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        File file = new File(filename);

        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(file.toPath());
            char singleChar;
            for (byte b : fileBytes) {
                singleChar = (char) b;
                System.out.print(singleChar);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dataInfo;
    }

    @Override
    public void readline() {
        var path = Paths.get(filename);
        try (var s = Files.lines(path)) {
            s.forEach(this::consume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void consume(String value) {
        System.out.println(value);
    }

}
