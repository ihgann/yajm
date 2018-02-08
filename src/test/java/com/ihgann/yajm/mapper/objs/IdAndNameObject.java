package com.ihgann.yajm.mapper.objs;

public class IdAndNameObject {

    public String id;

    public String name;

    public IdAndNameObject withId(String id) {
        this.id = id;
        return this;
    }

    public IdAndNameObject withName(String name) {
        this.name = name;
        return this;
    }

}
