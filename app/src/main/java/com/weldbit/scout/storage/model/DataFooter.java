package com.weldbit.scout.storage.model;

import lombok.Data;

public @Data class DataFooter {
    private byte[] id = { 77, 76, 66 };
    private byte[] version = { 118, 49 };
    private long recordCount;
}
