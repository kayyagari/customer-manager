package com.kayyagari;

import static com.kayyagari.util.CustomerGenerator.generateCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kayyagari.audit.AuditAction;
import com.kayyagari.db.dao.CustomerDao;
import com.kayyagari.db.entities.AuditLog;
import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.entities.OperationStatus;
import com.kayyagari.resources.CustomerResource;

public class AuditLogResourceTest extends TestBase {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	@InjectMocks
	private CustomerResource customerRes;

	@Test
	public void testCreateAuditLog() throws Exception {
		Customer c1 = generateCustomer();
		String c1Json = toJson(c1);
		
		MvcResult result = mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(c1Json)).andExpect(status().isCreated()).andReturn();
		Customer createdCustomer = parseResult(result, Customer.class);
		
		result = mockMvc.perform(get("/auditlog/1")).andExpect(status().isOk()).andReturn();
		AuditLog fetchedAuditLog = parseResult(result, AuditLog.class);
		assertEquals(AuditAction.CREATE.name(), fetchedAuditLog.getAction());
		assertEquals(createdCustomer.getId(), fetchedAuditLog.getEntityId());
		assertEquals(OperationStatus.SUCCESS, fetchedAuditLog.getStatus());
		
		Customer c2 = generateCustomer();
		c2.setId(createdCustomer.getId());
		String c2Json = toJson(c2);
		
		mockMvc.perform(put("/customer").contentType(MediaType.APPLICATION_JSON).content(c2Json)).andExpect(status().isOk()).andReturn();
		result = mockMvc.perform(get("/auditlog/2")).andExpect(status().isOk()).andReturn();
		fetchedAuditLog = parseResult(result, AuditLog.class);
		assertEquals(AuditAction.UPDATE.name(), fetchedAuditLog.getAction());
		assertEquals(createdCustomer.getId(), fetchedAuditLog.getEntityId());
		assertEquals(OperationStatus.SUCCESS, fetchedAuditLog.getStatus());

		mockMvc.perform(delete("/customer/" + createdCustomer.getId())).andExpect(status().isOk());
		result = mockMvc.perform(get("/auditlog/3")).andExpect(status().isOk()).andReturn();
		fetchedAuditLog = parseResult(result, AuditLog.class);
		assertEquals(AuditAction.DELETE.name(), fetchedAuditLog.getAction());
		assertEquals(createdCustomer.getId(), fetchedAuditLog.getEntityId());
		assertEquals(OperationStatus.SUCCESS, fetchedAuditLog.getStatus());
	}

	@Test
	public void testFailureAuditLogs() throws Exception {
		CustomerDao erroringCustomerDao = Mockito.mock(CustomerDao.class);
		when(erroringCustomerDao.createCustomer(any())).thenThrow(RuntimeException.class);
		when(erroringCustomerDao.updateCustomer(any(), any())).thenThrow(RuntimeException.class);
		Mockito.doThrow(RuntimeException.class).when(erroringCustomerDao).deleteCustomer(anyLong());

		customerRes._setDao(erroringCustomerDao);
		
		Customer c = generateCustomer();
		c.setId(1L); // for PUT
		String json = toJson(c);
		
		MockHttpServletRequestBuilder[] requestBuilders = {
				post("/customer").contentType(MediaType.APPLICATION_JSON).content(json),
				put("/customer").contentType(MediaType.APPLICATION_JSON).content(json),
				delete("/customer/1")
		};
		for(MockHttpServletRequestBuilder msrb : requestBuilders) {
			try {
				mockMvc.perform(msrb);
			}
			catch(Exception e) {
			}
		}

		MvcResult result = mockMvc.perform(get("/auditlog/fetch")).andExpect(status().isOk()).andReturn();
		ObjectNode page = (ObjectNode)mapper.readTree(result.getResponse().getContentAsByteArray());
		ArrayNode content = (ArrayNode)page.get("content");
		System.out.println(content);
		// default ordering is latest first 
		AuditLog delete = mapper.treeToValue(content.get(0), AuditLog.class);
		assertEquals(AuditAction.DELETE.name(), delete.getAction());
		assertEquals(OperationStatus.FAILURE, delete.getStatus());

		AuditLog update = mapper.treeToValue(content.get(1), AuditLog.class);
		assertEquals(AuditAction.UPDATE.name(), update.getAction());
		assertEquals(OperationStatus.FAILURE, update.getStatus());

		AuditLog create = mapper.treeToValue(content.get(2), AuditLog.class);
		assertEquals(AuditAction.CREATE.name(), create.getAction());
		assertEquals(OperationStatus.FAILURE, create.getStatus());

		customerRes._setDao(Mockito.mock(CustomerDao.class)); // reset
	}

	@Test
	public void testPagination() throws Exception {
		// there should be 300 auditlog records after executing this for-loop
		for(int i = 0; i < 100; i++) {
			Customer c = generateCustomer();
			String json = toJson(c);
			
			MvcResult result = mockMvc.perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andReturn();
			Customer createdCustomer = parseResult(result, Customer.class);
			json = toJson(createdCustomer);
			mockMvc.perform(put("/customer").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
			mockMvc.perform(delete("/customer/" + createdCustomer.getId())).andExpect(status().isOk());
		}
		
		MvcResult result = mockMvc.perform(get("/auditlog/fetch")).andExpect(status().isOk()).andReturn();
		ObjectNode page = (ObjectNode)mapper.readTree(result.getResponse().getContentAsByteArray());
		assertEquals(100, page.get("numberOfElements").asInt());
		assertEquals(300, page.get("totalElements").asInt());
		
		result = mockMvc.perform(get("/auditlog/fetch?pageNo=1&pageSize=10&sortField=id")).andExpect(status().isOk()).andReturn();
		page = (ObjectNode)mapper.readTree(result.getResponse().getContentAsByteArray());
		assertEquals(10, page.get("numberOfElements").asInt());
		assertEquals(30, page.get("totalPages").asInt());
	}
}
