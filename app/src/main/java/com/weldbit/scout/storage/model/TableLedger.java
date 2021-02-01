package com.weldbit.scout.storage.model;

import com.weldbit.scout.storage.annotations.Column;
import com.weldbit.scout.storage.annotations.PrimaryKey;
import com.weldbit.scout.storage.annotations.WeldbitData;

import lombok.Data;

@WeldbitData
@Data
public class TableLedger {
    @PrimaryKey
    private String tablename;
    @Column
    private String recordId;
    @Column
    private String filename;
    @Column
    private String position;
    private String metadata;
}
