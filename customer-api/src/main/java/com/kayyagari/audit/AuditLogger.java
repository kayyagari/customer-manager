package com.kayyagari.audit;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayyagari.db.entities.AuditLog;
import com.kayyagari.db.entities.AuditableEntity;
import com.kayyagari.db.entities.OperationStatus;
import com.kayyagari.db.repos.AuditLogRepository;

/**
 * An asynchronous audit logger
 */
@Component
public class AuditLogger {
	private ExecutorService threadPool;
	
	@Autowired
	private AuditLogRepository logRepo;

	@Autowired
	private ObjectMapper mapper;
	
	private static final Logger LOG = LoggerFactory.getLogger(AuditLogger.class);

	public AuditLogger() {
		threadPool = Executors.newCachedThreadPool();
	}
	
	public void logSuccess(AuditableEntity ae, AuditAction action) {
		log(ae.getId(), ae, action, OperationStatus.SUCCESS);
	}

	public void logFailure(AuditableEntity ae, AuditAction action) {
		log(ae.getId(), ae, action, OperationStatus.FAILURE);
	}

	public void logSuccess(Long entityId, AuditAction action) {
		log(entityId, null, action, OperationStatus.SUCCESS);
	}

	public void logFailure(Long entityId, AuditAction action) {
		log(entityId, null, action, OperationStatus.FAILURE);
	}

	private void log(Long entityId, AuditableEntity ae, AuditAction action, OperationStatus os) {
		Timestamp createdOn = Timestamp.from(Instant.now()); // this should be created here for chronological ordering of events
		Runnable task = new Runnable() {
			@Override
			public void run() {
				AuditLog auditRecord = new AuditLog();
				auditRecord.setAction(action.name());
				auditRecord.setCreatedOn(createdOn);
				auditRecord.setEntityId(entityId);
				auditRecord.setStatus(os);
				
				if(ae != null) {
					try {
						String requestBody = mapper.writeValueAsString(ae);
						auditRecord.setRequest(requestBody);				
					}
					catch(Exception e) {
						LOG.warn("failed to convert AuditableEntity to JSON", e);
					}
				}
				try {
					auditRecord = logRepo.save(auditRecord);
					LOG.debug("saved audit record {}", auditRecord.getId()); // either this should be set to TRACE or remove this line in PROD env 
				}
				catch(Exception e) {
					LOG.warn("failed to save AuditLog record", e);
				}
			}
		};
		
		threadPool.submit(task);
	}
}
