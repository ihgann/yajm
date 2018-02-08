package com.ihgann.yajm.mapper.objs;

import com.ihgann.yajm.mapper.Mapping;

public class IdcAndNamecNoMappingsObject {

    public String idc;
    public String namec;

    public IdcAndNamecNoMappingsObject withIdc(String idc) {
        this.idc = idc;
        return this;
    }

    public IdcAndNamecNoMappingsObject withNamec(String namec) {
        this.namec = namec;
        return this;
    }
}
