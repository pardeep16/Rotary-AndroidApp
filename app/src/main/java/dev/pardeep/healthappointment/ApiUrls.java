package dev.pardeep.healthappointment;

/**
 * Created by pardeep on 18-06-2017.
 */
public class ApiUrls {

    private static String domain="http://139.59.74.116:3030";
    private static String profilePatients="/api/v1/getPatientProfiles";
    private static String signInWithGmail="http://139.59.74.116:3030/api/v1/signin/gmail";
    private static String verifyOYTP="http://139.59.74.116:3030/api/v1/verify/mobileno";
    private static String sendOTp="http://139.59.74.116:3030/api/v1/sendotp/mobile";

    private static String verifyMobileNoAddInProf="http://139.59.74.116:3030/api/v1/add/phone";

    private static String getDoctorsList="http://139.59.74.116:3030/api/v1/get/doctorlist";

    private static String getTimeSlots="http://139.59.74.116:3030/api/v1/get/timeslots";

    private static String bookAppointment="http://139.59.74.116:3030/api/v1/appointment/book";

    private static String getListAppointments="http://139.59.74.116:3030/api/v1/appointment/get";

    private static String getDeptList="http://139.59.74.116:3030/api/v1/get/departmentlist";

    private static String opd_counter_socket="http://139.59.74.116:8081";

    private static String paymentRequestApi="http://139.59.74.116:3030/api/v1/get/paymentrequest";

    private static String checkPaymentStatus="/apiv1/opd/verifyPaymentStatus";

    public static String getCheckPaymentStatus() {
        return checkPaymentStatus;
    }

    public static String getPaymentRequestApi() {
        return paymentRequestApi;
    }

    public static String getOpd_counter_socket() {
        return opd_counter_socket;
    }

    public static String getGetDeptList() {
        return getDeptList;
    }

    public static String getGetListAppointments() {
        return getListAppointments;
    }

    public static String getBookAppointment() {
        return bookAppointment;
    }

    public static String getGetTimeSlots() {
        return getTimeSlots;
    }

    public static String getGetDoctorsList() {
        return getDoctorsList;
    }

    public static String getVerifyMobileNoAddInProf() {
        return verifyMobileNoAddInProf;
    }

    public static String getSendOTp() {
        return sendOTp;
    }

    public static String getVerifyOYTP() {
        return verifyOYTP;
    }

    public static String getSignInWithGmail() {
        return signInWithGmail;
    }

    public static String getDomain() {
        return domain;
    }

    public static String getProfilePatients() {
        return profilePatients;
    }
}
