package com.ihgann.yajm.mapper;

public interface MapperModifier<U, V> {

  V aToB(U source, Class<V> destination);

  U bToA(V source, Class<U> destination);
}
