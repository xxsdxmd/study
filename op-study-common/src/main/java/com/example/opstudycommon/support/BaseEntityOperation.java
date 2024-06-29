package com.example.opstudycommon.support;


import com.example.opstudycommon.validator.ValidateGroup;
import com.example.opstudycommon.validator.ValidateResult;
import com.example.opstudycommon.validator.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author xxs
 * @Date 2024/6/29 22:55
 */
public abstract class BaseEntityOperation implements EntityOperation {

     static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

     /**
      * validator
      * @param t
      * @param group
      * @param <T>
      */
     public <T> void doValidator(T t, Class<? extends ValidateGroup> group) {
          Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(t, group, Default.class);
          if (!isEmpty(constraintViolationSet)) {
               List<ValidateResult> validateResults = constraintViolationSet.stream()
                       .map(cv -> new ValidateResult(cv.getPropertyPath().toString(), cv.getMessage()))
                       .collect(Collectors.toList());
               throw new ValidationException(validateResults);
          }
     }
}
