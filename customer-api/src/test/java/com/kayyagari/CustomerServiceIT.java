package com.kayyagari;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.entities.Gender;
import com.kayyagari.resources.CustomerResource;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CustomerManagerApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class CustomerServiceIT {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	@InjectMocks
	private CustomerResource customerRes;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testCreateCustomer() throws Exception {
		Customer c = generateCustomer();
		String json = toJson(c);
		
		mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isCreated());
	}
	
	private String toJson(Customer c) {
		try {
			return mapper.writeValueAsString(c);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Customer generateCustomer() {
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
