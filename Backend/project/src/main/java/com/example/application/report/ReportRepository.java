package com.example.application.report;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report,Long> {

	@Query("select r from Report r")
	ArrayList<Report> getAllReports();

}
