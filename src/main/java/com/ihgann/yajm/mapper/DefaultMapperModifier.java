package com.ihgann.yajm.mapper;

public class DefaultMapperModifier implements MapperModifier {

    @Override
    public Object aToB(Object source, Class destination) {
        return destination.cast(source);
    }

    @Override
    public Object bToA(Object source, Class destination) {
        return destination.cast(source);
    }

}
