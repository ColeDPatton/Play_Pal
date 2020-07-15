package com.example.application.report;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.user.SwipeRepository;
import com.example.application.user.User;
import com.example.application.user.UserRepository;

@Service
public class ReportService {
	@Autowired
	ReportRepository reportRepository;
	@Autowired
	UserRepository userRepository;

	public void newReport(Report report) {
		if(!userRepository.findById(report.getReporter()).isPresent() &&
				!userRepository.findById(report.getReported()).isPresent())
			return;
		reportRepository.save(report);
	//	return true;
	}

	public ArrayList<Report> getReports() {
		return reportRepository.getAllReports();
	}

}
