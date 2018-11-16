package com.autobusi.home.study.models;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SubjectImageRepository extends PagingAndSortingRepository<SubjectImage, Long> {
	public List<SubjectImage> findBySubjectId(long subjectId);
}
