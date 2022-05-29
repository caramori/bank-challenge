package br.mac.bank.api.validation.annotations;

import br.mac.bank.api.validation.validators.ExistsInDatabaseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExistsInDatabaseValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsInDatabase {
    String message() default "does not exist or is not supported";
    String table() default "";
    String column() default "";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}