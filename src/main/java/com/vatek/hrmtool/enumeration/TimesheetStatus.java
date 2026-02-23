package com.vatek.hrmtool.enumeration;


public enum TimesheetStatus {
    PENDING("Pending"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted");
    private final String timesheet;
    TimesheetStatus(String timesheet){
        this.timesheet = timesheet;
    }
    public String getValue(){
        return timesheet;
    }
}
