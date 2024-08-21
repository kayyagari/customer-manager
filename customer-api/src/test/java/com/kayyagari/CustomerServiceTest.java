package com.kayyagari;

import static com.kayyagari.util.CustomerGenerator.generateCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.kayyagari.db.entities.Customer;
import com.kayyagari.resources.CustomerResource;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CustomerManagerApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class CustomerServiceTest {
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
		
		MvcResult result = mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andReturn();
		Customer createdCustomer = parseResult(result);
		
		result = mockMvc.perform(get("/customer/" + createdCustomer.getId())).andExpect(status().isOk()).andReturn();
		Customer fetchedCustomer = parseResult(result);
		assertEquals(createdCustomer.getName(), fetchedCustomer.getName());
		
		mockMvc.perform(delete("/customer/" + createdCustomer.getId())).andExpect(status().isOk());
		mockMvc.perform(get("/customer/" + createdCustomer.getId())).andExpect(status().isNotFound());
	}

	@Test
	public void testInvalidInput() throws Exception {
		
		String[] names = {null, "", "    ", RandomStringUtils.randomAlphabetic(101), "1"};
		for(String name : names) {
			Customer c = generateCustomer();
			c.setName(name);
			String json = toJson(c);
			mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		}

		String[] addresses = {null, "", "     ", RandomStringUtils.randomAlphabetic(201), "1", RandomStringUtils.randomAlphabetic(9)};
		for(String addr : addresses) {
			Customer c = generateCustomer();
			c.setAddress(addr);
			String json = toJson(c);
			mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		}

		JsonNode[] dobValues = {NullNode.getInstance(), new TextNode(""), new IntNode(1), new TextNode("01-01"), new TextNode("024"), new TextNode("24-")};
		for(JsonNode dob : dobValues) {
			Customer c = generateCustomer();
			ObjectNode rootNode = (ObjectNode)mapper.valueToTree(c);
			rootNode.set("dob", dob);
			String json = rootNode.toString();
			System.out.println(json);
			mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		}
		
		JsonNode[] genders = {new TextNode("Y")};
		for(JsonNode gender : genders) {
			Customer c = generateCustomer();
			ObjectNode rootNode = (ObjectNode)mapper.valueToTree(c);
			rootNode.set("gender", gender);
			String json = rootNode.toString();
			System.out.println(json);
			mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		}
	}

	@Test
	public void testPagination() throws Exception {
		for(int i = 0; i < 40; i++) {
			Customer c = generateCustomer();
			String json = toJson(c);
			
			mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
		}
		
		MvcResult result = mockMvc.perform(get("/customer/fetch")).andExpect(status().isOk()).andReturn();
		ObjectNode page = (ObjectNode)mapper.readTree(result.getResponse().getContentAsByteArray());
		assertEquals(10, page.get("numberOfElements").asInt());
		assertEquals(40, page.get("totalElements").asInt());
		
		result = mockMvc.perform(get("/customer/fetch?pageNo=1&pageSize=20&sortField=id")).andExpect(status().isOk()).andReturn();
		page = (ObjectNode)mapper.readTree(result.getResponse().getContentAsByteArray());
		assertEquals(20, page.get("numberOfElements").asInt());
		assertEquals(2, page.get("totalPages").asInt());
	}

	private String toJson(Customer c) {
		try {
			return mapper.writeValueAsString(c);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Customer parseResult(MvcResult result) throws Exception {
		return mapper.readValue(result.getResponse().getContentAsByteArray(), Customer.class);
	}
}
