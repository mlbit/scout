package com.weldbit.scout.storage.model;

import lombok.Data;

public @Data class DataHeader {
    private byte[] id = { 77, 76, 66 };
    private byte[] version = { 118, 49 };
    private String filename;
}
