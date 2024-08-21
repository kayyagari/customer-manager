package com.kayyagari.util;

import java.time.LocalDate;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.entities.Gender;

/**
 * This class actually should only be visible for tests, but that requires exporting another Maven artifact with tests classifier.
 * I am skipping doing that for this exercise. 
 */
public class CustomerGenerator {
	public static Customer generateCustomer() {
		Customer c = new Customer();
		c.setAddress(RandomStringUtils.randomAlphabetic(100));
		Random rnd = new Random(System.currentTimeMillis());
		int age = rnd.nextInt(1, 50);
		int year = 2024 - age;
		int month = rnd.nextInt(1, 12);
		int day = rnd.nextInt(1, 29);
		LocalDate dob = LocalDate.of(year, month, day);

		c.setDob(dob);
		
		Gender[] genders = new Gender[]{Gender.M, Gender.F, Gender.UNKNOWN};
		c.setGender(genders[rnd.nextInt(0, 2)]);
		c.setName(RandomStringUtils.randomAlphabetic(10));
		
		return c;
	}

}
