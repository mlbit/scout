package com.weldbit.scout.storage.model;
import lombok.Data;
import com.weldbit.scout.storage.annotations.Column;

public @Data class IndexFile {
    @Column
    private String hashIdx;
    @Column
    private long position;
}
