package com.ihgann.yajm.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mapping {

  Class other();

  String fieldName() default "";

  Class modifier() default DefaultMapperModifier.class;
}
