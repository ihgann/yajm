package com.ihgann.yajm.mapper.objs;

import com.ihgann.yajm.mapper.Mapping;

public class IdbAndNamebObject {

  @Mapping(other = IdAndNameObject.class, fieldName = "id")
  public String idb;

  @Mapping(other = IdAndNameObject.class, fieldName = "name")
  public String nameb;

  public IdbAndNamebObject withIdb(String idb) {
    this.idb = idb;
    return this;
  }

  public IdbAndNamebObject withNameb(String nameb) {
    this.nameb = nameb;
    return this;
  }
}
