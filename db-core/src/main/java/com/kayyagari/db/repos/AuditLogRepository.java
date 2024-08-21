package com.kayyagari.db.repos;

import org.springframework.data.repository.CrudRepository;

import com.kayyagari.db.entities.AuditLog;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {

}
