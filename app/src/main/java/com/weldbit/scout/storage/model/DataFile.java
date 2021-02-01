package com.weldbit.scout.storage.model;

import lombok.Data;

public @Data class DataFile<T> {
    private DataHeader header;
    private T[] records;
    private DataFooter footer;
}
