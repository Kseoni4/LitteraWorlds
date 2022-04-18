package org.litteraworlds.services.annotations;

import java.lang.annotation.Annotation;

public class MappingAnnotationProcessing implements Mapping{
    @Override
    public String value() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
