package com.kayyagari.audit;

/**
 * The name of the action to be logged in the audit repository. 
 * This is stored as in DB as string and not as enum so that new actions can be added without changing the schema.
 */
public enum AuditAction {
	CREATE,
	UPDATE,
	DELETE;
}
