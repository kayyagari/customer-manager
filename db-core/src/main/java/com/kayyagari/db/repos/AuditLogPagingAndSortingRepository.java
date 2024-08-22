package com.kayyagari.db.repos;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.kayyagari.db.entities.AuditLog;

public interface AuditLogPagingAndSortingRepository extends PagingAndSortingRepository<AuditLog, Long>{

}
