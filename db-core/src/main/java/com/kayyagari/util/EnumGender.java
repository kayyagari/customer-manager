package com.kayyagari.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kayyagari.db.entities.Gender;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumGenderValidator.class)
public @interface EnumGender {
	Gender[] anyOf();

	String message() default "invalid value given for the enum";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
