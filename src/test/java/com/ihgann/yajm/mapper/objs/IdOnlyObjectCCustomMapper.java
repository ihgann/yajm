package com.ihgann.yajm.mapper.objs;

import com.ihgann.yajm.mapper.Mapping;
import com.ihgann.yajm.mapper.modifiers.IdOnlyObjectIdAndIdOnlyObjectCCustomMapperIdc;

public class IdOnlyObjectCCustomMapper {

    @Mapping(other = IdOnlyObject.class, fieldName = "id",
            modifier = IdOnlyObjectIdAndIdOnlyObjectCCustomMapperIdc.class)
    public String idc;

    public String getIdc() {
        return this.idc;
    }
}
