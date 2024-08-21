package com.kayyagari.audit;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.kayyagari.db.entities.AuditLog;
import com.kayyagari.db.entities.Customer;
import com.kayyagari.db.repos.AuditLogRepository;

@Component
@Aspect
@EnableAspectJAutoProxy
public class AuditingAspect {
	@Autowired
	private AuditLogRepository auditRepo;
	
	@AfterReturning(pointcut = "execution(* com.kayyagari.*.*(..)) && args(incoming,..)")
	public void logUpsert(Customer incoming) {
		AuditLog al = new AuditLog();
		//al.setAction(null);
		//auditRepo.save(null);
		System.out.println("*********** logging upsert");
	}
}
