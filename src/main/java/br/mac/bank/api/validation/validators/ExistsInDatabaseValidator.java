package br.mac.bank.api.validation.validators;

import br.mac.bank.api.validation.annotations.ExistsInDatabase;
import br.mac.bank.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ExistsInDatabaseValidator implements ConstraintValidator<ExistsInDatabase, Object> {

    @Autowired
    private DatabaseService databaseService;

    private String table;
    private String column;

    @Override
    public void initialize(ExistsInDatabase constraintAnnotation) {
        this.table = constraintAnnotation.table();
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return databaseService.existsInDatabase(this.table, this.column, value);
    }
}