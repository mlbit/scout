package com.weldbit.scout.storage.model;

import lombok.Data;

@Data
public class IndexFile {
    private byte[] hashIdx;
    private long position;
}
