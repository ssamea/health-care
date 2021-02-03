package com.example.daily_function;

import java.io.Serializable;

public class food implements Serializable {
    private double Calcicum_mg;
    private double Carbon_g;
    private double Fat_g;
    private String Foodname;
    private double Iron_ug;
    private int NO;
    private double ServingSize;
    private double Total_Dietary_Fiber_g;
    private double Total_sugar_g;
    private double VItamin_C_mg;
    private double Vitamin_B1_mg;
    private double Vitamin_B2_mg;
    private String company;
    private double Kcal;
    private double na_mg;
    private double protein_g;
    private boolean isSelected;

    public food(){

    }


    /*
                public food(String name, String company){
                    this.name=name;
                    this.company=company;
                }
            */
    public double getCalcicum_mg() {
        return Calcicum_mg;
    }

    public void setCalcicum_mg(double calcicum_mg) {
        this.Calcicum_mg = calcicum_mg;
    }

    public double getCarbon_g() {
        return Carbon_g;
    }

    public void setCarbon_g(double carbon_g) {
        this.Carbon_g = carbon_g;
    }

    public double getFat_g() {
        return Fat_g;
    }

    public void setFat_g(double fat_g) {
        this.Fat_g = fat_g;
    }

    public String getFoodname() {
        return Foodname;
    }

    public void setFoodname(String Foodname) {
        this.Foodname = Foodname;
    }

    public double getIron_ug() {
        return Iron_ug;
    }

    public void setIron_ug(double iron_ug) {
        this.Iron_ug = iron_ug;
    }

    public int getNO() {
        return NO;
    }

    public void setNO(int NO) {
        this.NO = NO;
    }

    public double getServingSize() {
        return ServingSize;
    }

    public void setServingSize(double servingSize) {
        this.ServingSize = servingSize;
    }

    public double getTotal_Dietary_Fiber_g() {
        return Total_Dietary_Fiber_g;
    }

    public void setTotal_Dietary_Fiber_g(double total_Dietary_Fiber_g) {
        this.Total_Dietary_Fiber_g = total_Dietary_Fiber_g;
    }

    public double getTotal_sugar_g() {
        return Total_sugar_g;
    }

    public void setTotal_sugar_g(double total_sugar_g) {
        this.Total_sugar_g = total_sugar_g;
    }

    public double getVItamin_C_mg() {
        return VItamin_C_mg;
    }

    public void setVItamin_C_mg(double VItamin_C_mg) {
        this.VItamin_C_mg = VItamin_C_mg;
    }

    public double getVitamin_B1_mg() {
        return Vitamin_B1_mg;
    }

    public void setVitamin_B1_mg(double vitamin_B1_mg) {
        this.Vitamin_B1_mg = vitamin_B1_mg;
    }

    public double getVitamin_B2_mg() {
        return Vitamin_B2_mg;
    }

    public void setVitamin_B2_mg(double vitamin_B2_mg) {
        this.Vitamin_B2_mg = vitamin_B2_mg;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getKcal() {
        return Kcal;
    }

    public void setKcal(double kcal) {
        this.Kcal = kcal;
    }

    public double getNa_mg() {
        return na_mg;
    }

    public void setNa_mg(double na_mg) {
        this.na_mg = na_mg;
    }

    public double getProtein_g() {
        return protein_g;
    }

    public void setProtein_g(double protein_g) {
        this.protein_g = protein_g;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


}
