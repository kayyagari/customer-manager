package com.kayyagari.resources;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kayyagari.db.entities.AuditLog;
import com.kayyagari.db.repos.AuditLogPagingAndSortingRepository;
import com.kayyagari.db.repos.AuditLogRepository;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/auditlog")
public class AuditLogResource {
	@Autowired
	private AuditLogPagingAndSortingRepository auditLogPagerRepo;
	
	@Autowired
	private AuditLogRepository crudRepo;

	private static final Logger LOG = LoggerFactory.getLogger(AuditLogResource.class);

	@GetMapping("{id}")
	public ResponseEntity<AuditLog> getAuditLog(@PathVariable("id") @NotNull Long id) {
		Optional<AuditLog> al = crudRepo.findById(id);
		
		ResponseEntity<AuditLog> resp = null;
		if(al.isPresent()) {
			resp = ResponseEntity.ok(al.get());
		}
		else {
			LOG.debug("no AuditLog found with the given ID {}", id);
			resp = ResponseEntity.notFound().build();
		}
		
		return resp;
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<Page<AuditLog>> fetch(@RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo, 
			@RequestParam(name = "pageSize", defaultValue = "100", required = false) Integer pageSize, 
			@RequestParam(name = "sortField", defaultValue = "createdOn", required = false) String sortField) {
		Sort sort;
		if(sortField.equals("createdOn")) {
			sort = Sort.by(Order.desc(sortField));
		}
		else {
			sort = Sort.by(sortField);
		}

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
		Page<AuditLog> page = auditLogPagerRepo.findAll(pageRequest);
		return ResponseEntity.ok(page);
	}
	
}
