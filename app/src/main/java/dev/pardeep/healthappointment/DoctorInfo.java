package dev.pardeep.healthappointment;

/**
 * Created by pardeep on 26-06-2017.
 */
public class DoctorInfo {
    private String doctor_id;
    private String doctor_name;

    public DoctorInfo(String doctor_id, String doctor_name) {
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }
}
