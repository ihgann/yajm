package com.ihgann.yajm.mapper.modifiers;

import com.ihgann.yajm.mapper.MapperModifier;

public class IdOnlyObjectIdAndIdOnlyObjectCCustomMapperIdc implements MapperModifier<String, String> {

    @Override
    public String aToB(String source, Class<String> destination) {
        return source.toUpperCase();
    }

    @Override
    public String bToA(String source, Class<String> destination) {
        return source.toLowerCase();
    }
}
