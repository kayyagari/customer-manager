package com.kayyagari.db.dao;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.entities.Gender;

@DataJpaTest
@ContextConfiguration(classes = {SpringTestContextApp.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class CustomerDaoTest {
	@Autowired
	private CustomerDao dao;

	@Test
	public void testCreateCustomer() {
		Customer c = new Customer();
		c.setAddress("city 1 manhattan");
		LocalDate dob = LocalDate.of(1990, 1, 2);
		c.setDob(dob);
		c.setGender(Gender.M);
		c.setName("elephant");
		dao.createCustomer(c);
	}
}
