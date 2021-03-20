package com.weldbit.scout.storage.model;

import java.util.List;

import lombok.Data;

@Data
public class SearchKey {
    private String name;
    private List<String> fieldnames;
}
