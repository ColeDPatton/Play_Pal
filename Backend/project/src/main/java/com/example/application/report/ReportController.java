package com.example.application.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.application.user.User;
import com.example.application.user.UserService;
import com.example.application.user.UserController.booleanResponse;

@RestController
public class ReportController {
	@Autowired
	UserService userService;
	@Autowired
	ReportService reportService;

	@PostMapping("/users/reportUser")
	public void report(@RequestBody Report report) {
		reportService.newReport(report);
	}
	
	@PostMapping("/users/getReports")
	public List<Report> getReports(@RequestBody ArrayList<User> user) {
		User u = userService.get((user.get(0)).getId()).get();
		if(u.getUsertype()!=3)
			return null;
		return reportService.getReports();
	}
}
