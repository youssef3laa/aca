package com.asset.appwork.platform;

import java.util.List;

public interface Entity<T> {
    public  T create(T data);
    public T read(long id);
    public List<T> readAll();


}
