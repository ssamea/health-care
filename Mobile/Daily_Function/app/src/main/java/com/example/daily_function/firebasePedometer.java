package com.example.daily_function;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class firebasePedometer {
    String client_id;
    String fb_Time;  //
    int steps;

    public firebasePedometer(){

    }

    public firebasePedometer(String client_id, String fb_Time, int steps) {
        this.client_id = client_id;
        this.fb_Time = fb_Time;
        this.steps = steps;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getFb_Time() {
        return fb_Time;
    }

    public void setFb_Time(String fb_Time) {
        this.fb_Time = fb_Time;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> firebaseSteps = new HashMap<>();
        firebaseSteps.put("id", client_id);
        firebaseSteps.put("Type", fb_Time);
        firebaseSteps.put("input_time", steps);

        return firebaseSteps;
    }
}
