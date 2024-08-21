CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    dob DATE NOT NULL,
    address VARCHAR(200),
    gender ENUM('M', 'F', 'UNKNOWN')
);

CREATE TABLE auditlog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(64) NOT NULL,
    entityid BIGINT,
    request VARCHAR(2048),
    status ENUM('SUCCESS', 'FAILURE'),
    createdon TIMESTAMP
);