package dev.pardeep.healthappointment;

import java.util.Date;

/**
 * Created by Gurwinder on 14-Jun-17.
 */

public class AppointmentsListContent {
    private String doc_contact;
    private String app_id;
    private String doc_name;
    private String department;
    private Date app_date;
    public AppointmentsListContent(String doc_contact,String app_id,String doc_name,String department,Date app_date){
        this.doc_contact=doc_contact;
        this.app_id=app_id;
        this.doc_name=doc_name;
        this.department=department;
        this.app_date=app_date;
    }
    public Date getApp_date() {
        return app_date;
    }
    public String getApp_id() {
        return app_id;
    }
    public String getDoc_name() {
        return doc_name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDoc_contact() {
        return doc_contact;
    }
}
