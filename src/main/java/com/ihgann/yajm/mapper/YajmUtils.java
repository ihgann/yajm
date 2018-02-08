package com.ihgann.yajm.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class YajmUtils {

    /**
     * Mapper function to map an object `U` to Class `V`. Attempts to infer based on field names of the similar classes.
     * If similar names are not found, checks for field names with {@link Mapping} annotation from both classes to
     * determine if there should be any linking done.
     *
     * @param source      The source object.
     * @param destination The destination class.
     * @param <V>         The destination class type.
     * @return A new instance of class type `V`.
     */
    @SuppressWarnings("unchecked")
    public static <V> V map(Object source, Class<V> destination) throws IllegalAccessException, InstantiationException {
        V response = destination.newInstance();

        List<Field> destinationFields = Arrays.asList(destination.getDeclaredFields());
        List<Field> sourceFields = Arrays.asList(source.getClass().getDeclaredFields());

        destinationFields.forEach(destField -> {
            boolean destFieldOriginalAccessibility = destField.isAccessible();
            destField.setAccessible(true);

            Mapping destMappingAnnotation = destField.getAnnotation(Mapping.class);

            // If the mapping is non-null, then apply the associated modifier and set the value to the
            // response object.
            if (destMappingAnnotation != null && destMappingAnnotation.other().equals(source.getClass())) {
                String expectedFieldName = destField.getName();
                if (!destMappingAnnotation.fieldName().isEmpty()) {
                    expectedFieldName = destMappingAnnotation.fieldName();
                }

                Field sourceField = null;
                try {
                    sourceField = source.getClass().getField(expectedFieldName);
                } catch (NoSuchFieldException e) {
                    destField.setAccessible(destFieldOriginalAccessibility);
                    throw new RuntimeException(e);
                }

                Object sourceValue = null;
                try {
                    sourceValue = ((MapperModifier) destMappingAnnotation.modifier().newInstance())
                            .aToB(sourceField.get(source), destField.getType());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                try {
                    destField.set(response, sourceValue);
                } catch (IllegalAccessException e) {
                    destField.setAccessible(destFieldOriginalAccessibility);
                    throw new RuntimeException(e);
                }

                destField.setAccessible(destFieldOriginalAccessibility);
                return;
            }

            List<Field> mappedSourceFields = sourceFields.stream()
                    .filter(sourceField -> {
                        Mapping sourceMappingAnnotation = sourceField.getAnnotation(Mapping.class);
                        return sourceMappingAnnotation != null &&
                                sourceMappingAnnotation.other().equals(destination) &&
                                ((destField.getName().equals(sourceField.getName()) &&
                                        sourceMappingAnnotation.fieldName().isEmpty()) ||
                                        (destField.getName().equals(sourceMappingAnnotation.fieldName())));
                    }).collect(Collectors.toList());

            if (mappedSourceFields != null && mappedSourceFields.size() > 1) {
                destField.setAccessible(destFieldOriginalAccessibility);
                throw new RuntimeException(
                        "More than one annotated Mapping for the same field name found.");
            }

            if (mappedSourceFields != null && mappedSourceFields.size() == 1) {
                Field mappedField = mappedSourceFields.get(0);
                boolean mappedFieldOriginalAccessibility = mappedField.isAccessible();
                mappedField.setAccessible(true);

                try {
                    destField.set(response,
                            ((MapperModifier) mappedField.getAnnotation(Mapping.class).modifier().newInstance())
                                    .bToA(mappedField.get(source), destField.getType()));
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }

                mappedField.setAccessible(mappedFieldOriginalAccessibility);
                destField.setAccessible(destFieldOriginalAccessibility);
                return;
            }

            // Now, we know it won't be any annotated fields. Just search for any
            // fields that happen to have the same name.
            List<Field> sameNameFields = sourceFields.stream()
                    .filter(sourceField -> {
                        boolean p2 = sourceField.isAccessible();
                        sourceField.setAccessible(true);
                        boolean responseBool = false;

                        if (sourceField.getName().equals(destField.getName())) {
                            responseBool = true;
                        }

                        sourceField.setAccessible(p2);
                        return responseBool;
                    }).collect(Collectors.toList());

            // Not possible that this list size could be greater than 1 (because
            // of Java checks), so just checking 1 and 0 cases.
            if (sameNameFields != null && sameNameFields.size() == 1) {
                Field sameNameField = sameNameFields.get(0);
                boolean pF = sameNameField.isAccessible();
                sameNameField.setAccessible(true);

                try {
                    destField.set(response,
                            new DefaultMapperModifier()
                                    .aToB(sameNameField.get(source), destField.getType()));
                } catch (IllegalAccessException e) {
                    destField.setAccessible(destFieldOriginalAccessibility);
                    sameNameField.setAccessible(pF);
                    throw new RuntimeException(e);
                }

                sameNameField.setAccessible(pF);
            }

            destField.setAccessible(destFieldOriginalAccessibility);
        });
        return response;
    }
}