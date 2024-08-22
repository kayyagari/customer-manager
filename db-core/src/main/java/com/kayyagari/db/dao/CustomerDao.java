package com.kayyagari.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.repos.CustomerCrudRepository;

/**
 * This class exists purely to demonstrate the DAO pattern, other than that this class doesn't do much.
 */
@Component
public class CustomerDao {
	
	@Autowired
	private CustomerCrudRepository crudRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerDao.class);

	public Customer createCustomer(Customer customer) {
		LOG.debug("inside createCustomer");
		Customer savedCustomer = crudRepo.save(customer);
		LOG.debug("{}", savedCustomer);
		return savedCustomer;
	}
	
	public Customer updateCustomer(Customer incoming, Customer existing) {
		BeanUtils.copyProperties(incoming, existing);
		return crudRepo.save(existing);
	}
	
	public void deleteCustomer(Long id) {
		crudRepo.deleteById(id);
	}
}
