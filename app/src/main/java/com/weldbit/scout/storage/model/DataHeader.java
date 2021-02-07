package com.weldbit.scout.storage.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.weldbit.scout.storage.annotations.Column;

import lombok.Data;

public @Data class DataHeader {
    @Column
    private byte[] id = { 77, 76, 66 };
    private byte[] version = { 118, 49 };
    private String filename;

    public byte[] getBytes() {
        var outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(id);
            outputStream.write(version);
            outputStream.write(filename.getBytes());
            outputStream.write(System.lineSeparator().getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
