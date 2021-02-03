package com.example.daily_function;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class firebaseFoodInput {
    String fb_client_id;
    String fb_Time;  //(아침,점심,저녁,간식)
    String input_time;
    double fb_Calcicum_mg;
    double fb_Carbon_g;
    double fb_Fat_g;
    double fb_Iron_ug;
    double fb_Total_Dietary_Fiber_g;
    double fb_Total_sugar_g;
    double fb_VItamin_C_mg;
    double fb_Vitamin_B1_mg;
    double fb_Vitamin_B2_mg;
    double fb_Kcal;
    double fb_na_mg;
    double fb_protein_g;



    public firebaseFoodInput(){

    }

    public firebaseFoodInput(String fb_client_id, String fb_Time, String input_timem,double fb_Calcicum_mg, double fb_Carbon_g, double fb_Fat_g,  double fb_Iron_ug, double fb_Total_Dietary_Fiber_g, double fb_Total_sugar_g, double fb_VItamin_C_mg, double fb_Vitamin_B1_mg, double fb_Vitamin_B2_mg,  double fb_Kcal, double fb_na_mg, double fb_protein_g) {
        this.fb_client_id = fb_client_id;
        this.fb_Time = fb_Time;
        this.input_time=input_timem;
        this.fb_Calcicum_mg = fb_Calcicum_mg;
        this.fb_Carbon_g = fb_Carbon_g;
        this.fb_Fat_g = fb_Fat_g;
       // this.fb_Foodname = fb_Foodname;
        this.fb_Iron_ug = fb_Iron_ug;
      //  this.fb_NO = fb_NO;
        //this.fb_ServingSize = fb_ServingSize;
        this.fb_Total_Dietary_Fiber_g = fb_Total_Dietary_Fiber_g;
        this.fb_Total_sugar_g = fb_Total_sugar_g;
        this.fb_VItamin_C_mg = fb_VItamin_C_mg;
        this.fb_Vitamin_B1_mg = fb_Vitamin_B1_mg;
        this.fb_Vitamin_B2_mg = fb_Vitamin_B2_mg;
    //    this.fb_company = fb_company;
        this.fb_Kcal = fb_Kcal;
        this.fb_na_mg = fb_na_mg;
        this.fb_protein_g = fb_protein_g;
    }

    public String getInput_time() {
        return input_time;
    }

    public void setInput_time(String input_time) {
        this.input_time = input_time;
    }

    public String getFb_client_id() {
        return fb_client_id;
    }

    public void setFb_client_id(String fb_client_id) {
        this.fb_client_id = fb_client_id;
    }

    public String getFb_Time() {
        return fb_Time;
    }

    public void setFb_Time(String fb_Time) {
        this.fb_Time = fb_Time;
    }

    public double getFb_Calcicum_mg() {
        return fb_Calcicum_mg;
    }

    public void setFb_Calcicum_mg(double fb_Calcicum_mg) {
        this.fb_Calcicum_mg = fb_Calcicum_mg;
    }

    public double getFb_Carbon_g() {
        return fb_Carbon_g;
    }

    public void setFb_Carbon_g(double fb_Carbon_g) {
        this.fb_Carbon_g = fb_Carbon_g;
    }

    public double getFb_Fat_g() {
        return fb_Fat_g;
    }

    public void setFb_Fat_g(double fb_Fat_g) {
        this.fb_Fat_g = fb_Fat_g;
    }

    //public String getFb_Foodname() { return fb_Foodname; }

    //public void setFb_Foodname(String fb_Foodname) {
  //      this.fb_Foodname = fb_Foodname;
   // }

    public double getFb_Iron_ug() {
        return fb_Iron_ug;
    }

    public void setFb_Iron_ug(double fb_Iron_ug) {
        this.fb_Iron_ug = fb_Iron_ug;
    }

    /*
    public int getFb_NO() {
        return fb_NO;
    }

    public void setFb_NO(int fb_NO) {
        this.fb_NO = fb_NO;
    }


    public double getFb_ServingSize() {
        return fb_ServingSize;
    }

    public void setFb_ServingSize(double fb_ServingSize) {
        this.fb_ServingSize = fb_ServingSize;
    }
   */
    public double getFb_Total_Dietary_Fiber_g() {
        return fb_Total_Dietary_Fiber_g;
    }

    public void setFb_Total_Dietary_Fiber_g(double fb_Total_Dietary_Fiber_g) {
        this.fb_Total_Dietary_Fiber_g = fb_Total_Dietary_Fiber_g;
    }

    public double getFb_Total_sugar_g() {
        return fb_Total_sugar_g;
    }

    public void setFb_Total_sugar_g(double fb_Total_sugar_g) {
        this.fb_Total_sugar_g = fb_Total_sugar_g;
    }

    public double getFb_VItamin_C_mg() {
        return fb_VItamin_C_mg;
    }

    public void setFb_VItamin_C_mg(double fb_VItamin_C_mg) {
        this.fb_VItamin_C_mg = fb_VItamin_C_mg;
    }

    public double getFb_Vitamin_B1_mg() {
        return fb_Vitamin_B1_mg;
    }

    public void setFb_Vitamin_B1_mg(double fb_Vitamin_B1_mg) {
        this.fb_Vitamin_B1_mg = fb_Vitamin_B1_mg;
    }

    public double getFb_Vitamin_B2_mg() {
        return fb_Vitamin_B2_mg;
    }

    public void setFb_Vitamin_B2_mg(double fb_Vitamin_B2_mg) {
        this.fb_Vitamin_B2_mg = fb_Vitamin_B2_mg;
    }

    /*
    public String getFb_company() {
        return fb_company;
    }

    public void setFb_company(String fb_company) {
        this.fb_company = fb_company;
    }

     */

    public double getFb_Kcal() {
        return fb_Kcal;
    }

    public void setFb_Kcal(double fb_Kcal) {
        this.fb_Kcal = fb_Kcal;
    }

    public double getFb_na_mg() {
        return fb_na_mg;
    }

    public void setFb_na_mg(double fb_na_mg) {
        this.fb_na_mg = fb_na_mg;
    }

    public double getFb_protein_g() {
        return fb_protein_g;
    }

    public void setFb_protein_g(double fb_protein_g) {
        this.fb_protein_g = fb_protein_g;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> firebaseFoodInput = new HashMap<>();
        firebaseFoodInput.put("id", fb_client_id);
        firebaseFoodInput.put("Type", fb_Time);
        firebaseFoodInput.put("input_time",input_time);
        firebaseFoodInput.put("Calcicum_mg", fb_Calcicum_mg);
        firebaseFoodInput.put("Carbon_g", fb_Carbon_g);
        firebaseFoodInput.put("Fat_g", fb_Fat_g);
       // firebaseFoodInput.put("Foodname", fb_Foodname);
        firebaseFoodInput.put("Iron_ug", fb_Iron_ug);
      //  firebaseFoodInput.put("NO", fb_NO);
     //   firebaseFoodInput.put("ServingSize", fb_ServingSize);
        firebaseFoodInput.put("Total_Dietary_Fiber_g", fb_Total_Dietary_Fiber_g);
        firebaseFoodInput.put("Total_sugar_g", fb_Total_sugar_g);
        firebaseFoodInput.put("VItamin_C_mg", fb_VItamin_C_mg);
        firebaseFoodInput.put("Vitamin_B1_mg", fb_Vitamin_B1_mg);
        firebaseFoodInput.put("Vitamin_B2_mg", fb_Vitamin_B2_mg);
    //    firebaseFoodInput.put("company", fb_company);
        firebaseFoodInput.put("Kcal", fb_Kcal);
        firebaseFoodInput.put("na_mg", fb_na_mg);
        firebaseFoodInput.put("protein_g", fb_protein_g);


        return firebaseFoodInput;
    }


}
