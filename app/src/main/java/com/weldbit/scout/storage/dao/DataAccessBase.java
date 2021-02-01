package com.weldbit.scout.storage.dao;

public interface DataAccessBase {
    public boolean createTable();

    public String readTable();

    public void readline();
}
