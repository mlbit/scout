package com.weldbit.scout.storage.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface PrimaryKey {

}
