package com.kayyagari.db.repos;

import org.springframework.data.repository.CrudRepository;

import com.kayyagari.db.entities.Customer;

public interface CustomerCrudRepository extends CrudRepository<Customer, Long> {

}
