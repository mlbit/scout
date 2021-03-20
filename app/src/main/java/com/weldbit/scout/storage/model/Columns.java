package com.weldbit.scout.storage.model;

import lombok.Data;

@Data
public class Columns {
    private String name;
    private String type;
    private long size;
    private String defaultvalue;
    private boolean allownull = true;
}
