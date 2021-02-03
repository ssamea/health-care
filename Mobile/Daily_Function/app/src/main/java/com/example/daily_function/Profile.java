package com.example.daily_function;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Profile { //프로필 데이터
    public String id;
    public double weight;
    public double height;
    public String name;
    public String gender;

    public int age;

    public Profile(){
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Profile( String id,String name,double height, double weight,String gender, int age){

        this.id=id;
        this.name=name;
        this.height=height;
        this.weight=weight;
        this.gender=gender;
        this.age=age;
    }

    public String getId(){
        return id;
    }

    public double getHeight(){
        return height;
    }

    public double getWeight(){return weight;}

    public String getName(){
        return name;
    }
    public String getGender(){
        return gender;
    }

    public int getAge(){
        return age;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> Profile = new HashMap<>();
        Profile.put("id", id);
        Profile.put("name", name);
        Profile.put("height", height);
        Profile.put("weight", weight);
        Profile.put("gender", gender);
        Profile.put("age", age);
        return Profile;
    }


}
