package com.autobusi.home.study.models;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface SubjectRepository extends PagingAndSortingRepository<Subject, Long> {
	
	@Query(value = "select * from Subject t where t.title like :searchText or t.detail like :searchText", nativeQuery = true)
	List<Subject> searchByText(@Param("searchText") String searchText); 
}
