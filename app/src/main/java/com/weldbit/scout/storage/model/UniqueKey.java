package com.weldbit.scout.storage.model;

import java.util.List;

import lombok.Data;

@Data
public class UniqueKey {
    private String name; // Name of this key
    private Integer order; // Field value is the arrangement the file index to use
    private List<String> fieldnames; // List of field to use to create index data
}
