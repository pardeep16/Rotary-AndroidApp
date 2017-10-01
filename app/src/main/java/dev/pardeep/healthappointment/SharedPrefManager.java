package dev.pardeep.healthappointment;

/**
 * Created by pardeep on 19-06-2017.
 */
public class SharedPrefManager {

    private static String saveOtpSession="otpmsg";

    private static String saveprofile="profile_save";
    private static String tempAppointmentProf="appointment_profile";

    private static String appointmentPatientProf="patient_profile";

    public static String getTempAppointmentProf() {
        return tempAppointmentProf;
    }

    public static String getSaveOtpSession() {
        return saveOtpSession;
    }

    public static String getSaveprofile() {
        return saveprofile;
    }

    public static String getAppointmentPatientProf() {
        return appointmentPatientProf;
    }
}
