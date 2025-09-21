package org.gayan.dls.validation.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/21/25
 * Time: 12:50 AM
 */
@Target({
        ElementType.PARAMETER,  // for @PathVariable, @RequestParam
        ElementType.FIELD,      // for DTO/entity fields
        ElementType.METHOD,     // for return values
        ElementType.ANNOTATION_TYPE, // for composing constraints
        ElementType.CONSTRUCTOR,     // for constructor params
        ElementType.TYPE_USE          // for generic type usage, e.g. List<@ValidUUID String>
})@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface ValidUUID {
    String message() default "Invalid UUID format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
