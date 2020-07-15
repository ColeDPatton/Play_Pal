package com.example.application.report;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.application.user.SwipeCompositeId;

@Entity
@Table(name = "reports")
@IdClass(ReportCompositeId.class)
public class Report {
	private String reason, description;
	@Id
	private long reporter;
	@Id
	private long reported;
	
	public Report() {}
	
	public Report(long reporter, long reported, String reason, String description) {
		setReporter(reporter);
		setReported(reported);
		setReason(reason);
		setDescription(description);
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getReporter() {
		return reporter;
	}

	public void setReporter(long reporter) {
		this.reporter = reporter;
	}

	public long getReported() {
		return reported;
	}

	public void setReported(long reported) {
		this.reported = reported;
	}
}
