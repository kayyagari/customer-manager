package com.kayyagari.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kayyagari.db.dao.CustomerDao;
import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.repos.CustomerCrudRepository;
import com.kayyagari.db.repos.CustomerPagingAndSortingRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/customer")
public class CustomerResource {
	@Autowired
	private CustomerDao dao;

	@Autowired
	private CustomerCrudRepository crudRepo;

	@Autowired
	private CustomerPagingAndSortingRepository pagerRepo;

	private static final Logger LOG = LoggerFactory.getLogger(CustomerResource.class);

	@PostMapping
	public ResponseEntity<Customer> createCustomer(@RequestBody @Valid Customer incoming) {
		LOG.debug("creating a new customer");
		Customer createdCustomer = dao.createCustomer(incoming);
		return new ResponseEntity<Customer>(createdCustomer, HttpStatus.CREATED);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") @NotNull Long id) {
		Optional<Customer> customer = crudRepo.findById(id);
		
		ResponseEntity<Customer> resp = null;
		if(customer.isPresent()) {
			resp = ResponseEntity.ok(customer.get());
		}
		else {
			LOG.debug("no customer found with the given ID {}", id);
			resp = ResponseEntity.notFound().build();
		}
		
		return resp;
	}
	
	@PutMapping
	public ResponseEntity<Customer> updateCustomer(@RequestBody @Valid Customer incoming) {
		Optional<Customer> existingCustomer = crudRepo.findById(incoming.getId());
		if(existingCustomer.isEmpty()) {
			LOG.debug("no customer found with the given ID {} to update", incoming.getId());
			return ResponseEntity.notFound().build(); // a custom result type is ne
		}
		
		LOG.debug("updating customer with the given ID {}", incoming.getId());
		Customer existing = existingCustomer.get();
		BeanUtils.copyProperties(incoming, existing);
		existing = crudRepo.save(existing);

		return new ResponseEntity<Customer>(existing, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable("id") @NotNull Long id) {
		LOG.debug("deleting cutomer with the given ID {}", id);
		crudRepo.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/fetch")
	public ResponseEntity<Page<Customer>> fetch(@RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo, 
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize, 
			@RequestParam(name = "sortField", defaultValue = "name", required = false) String sortField) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortField));
		Page<Customer> page = pagerRepo.findAll(pageRequest);
//		List<Customer> lst = new ArrayList<Customer>();
//		pagerRepo.findAll(pageRequest).forEach(c -> lst.add(c));
		return ResponseEntity.ok(page);
	}
}
