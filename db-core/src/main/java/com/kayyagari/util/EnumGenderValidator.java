package com.kayyagari.util;

import com.kayyagari.db.entities.Gender;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 */
public class EnumGenderValidator implements ConstraintValidator<EnumGender, Enum<?>> {
	private Gender[] values;

	@Override
	public void initialize(EnumGender constraintAnnotation) {
		this.values = constraintAnnotation.anyOf();
	}

	@Override
	public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
		boolean matched = true;
		if(value != null && values != null) {
			for(Gender v : values) {
				matched = value.equals(v);
				if(matched) {
					break;
				}
			}
		}
		return matched;
	}
}
