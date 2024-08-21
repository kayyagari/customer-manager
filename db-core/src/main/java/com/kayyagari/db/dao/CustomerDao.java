package com.kayyagari.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.repos.CustomerCrudRepository;
import com.kayyagari.db.repos.CustomerPagingAndSortingRepository;

/**
 * This class exists purely to demonstrate the DAO pattern, other than that this class doesn't do much.
 */
@Component
public class CustomerDao {
	
	@Autowired
	private CustomerCrudRepository crudRepo;
	
	@Autowired
	private CustomerPagingAndSortingRepository pagingRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerDao.class);

	public Customer createCustomer(Customer customer) {
		LOG.trace("inside createCustomer");
		Customer savedCustomer = crudRepo.save(customer);
		System.out.println("creating " + savedCustomer);
		return savedCustomer;
	}
}
