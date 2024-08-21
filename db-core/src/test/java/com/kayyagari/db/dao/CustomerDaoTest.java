package com.kayyagari.db.dao;

import static com.kayyagari.util.CustomerGenerator.generateCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.entities.Gender;
import com.kayyagari.db.repos.CustomerCrudRepository;

@DataJpaTest
@ContextConfiguration(classes = {SpringTestContextApp.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class CustomerDaoTest {
	@Autowired
	private CustomerDao dao;

	@Autowired
	private CustomerCrudRepository crudRepo;

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
	
	@Test
	public void testValidateInput() {
		String[] names = {null, "", "    ", RandomStringUtils.randomAlphabetic(101), "1"};
		for(String name : names) {
			Customer c = generateCustomer();
			c.setName(name);
			try {
				dao.createCustomer(c);
				fail("customer creation should fail with name " + name);
			}
			catch(Exception e) {
				assertTrue(true);
			}
		}
		
		String[] addresses = {null, "", "     ", RandomStringUtils.randomAlphabetic(201), "1", RandomStringUtils.randomAlphabetic(9)};
		for(String addr : addresses) {
			Customer c = generateCustomer();
			c.setAddress(addr);
			try {
				dao.createCustomer(c);
				fail("customer creation should fail with address " + addr);
			}
			catch(Exception e) {
				assertTrue(true);
			}
		}
	}
	
	@Test
	public void testUpdateCustomer() {
		assertTrue(crudRepo.findById(1l).isEmpty());

		Customer c1 = generateCustomer();
		c1 = dao.createCustomer(c1);

		assertFalse(crudRepo.findById(1l).isEmpty());

		Customer c2 = generateCustomer();
		BeanUtils.copyProperties(c1, c2);

		crudRepo.save(c2);
		assertEquals(c1.getName(), c2.getName());
		assertEquals(c1.getAge(), c2.getAge());
		
		crudRepo.deleteById(c1.getId());
		assertTrue(crudRepo.findById(1l).isEmpty());
	}
}
