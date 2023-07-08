package com.universe.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {GroupExistsValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupExists {
    String message() default "Group does not exists!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
