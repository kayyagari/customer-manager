package com.kayyagari.db.repos;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.kayyagari.db.entities.Customer;

public interface CustomerPagingAndSortingRepository extends PagingAndSortingRepository<Customer, Long>{

}
