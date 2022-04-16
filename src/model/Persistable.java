package model;

import java.io.IOException;

public interface Persistable <T> {
    public abstract T add(T obj);
    public abstract T delete(T id);
    public abstract T get(Integer id);
    public abstract void load() throws IOException;
    public abstract void save() throws IOException;
}
