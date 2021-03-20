package com.weldbit.scout.storage.model;

import java.util.List;

import lombok.Data;

@Data
public class TableStructure {
    private String tablename;
    private List<SearchKey> searchkey;
    private List<UniqueKey> uniquekey;
    private List<Columns> columns;
}
