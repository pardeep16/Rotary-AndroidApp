package dev.pardeep.healthappointment;

/**
 * Created by Gurwinder on 14-Jun-17.
 */

public class AppointmentsListContent {
    private String doc_contact;
    private String app_id;
    private String doc_id;
    private String department;
    private String app_date;
    private String app_status;
    private String payment_status;
    private String app_time;

    public AppointmentsListContent(String app_id, String doc_id, String app_date, String app_status, String payment_status, String app_time) {
        this.app_id = app_id;
        this.doc_id = doc_id;
        this.app_date = app_date;
        this.app_status = app_status;
        this.payment_status = payment_status;
        this.app_time = app_time;
    }

    public String getDoc_contact() {
        return doc_contact;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public String getDepartment() {
        return department;
    }

    public String getApp_date() {
        return app_date;
    }

    public String getApp_status() {
        return app_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public String getApp_time() {
        return app_time;
    }
}
