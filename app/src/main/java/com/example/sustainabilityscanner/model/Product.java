package com.example.sustainabilityscanner.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("product_name")
    private String name;

    @SerializedName("ecoscore_grade")
    private String sustainabilityInfo;

    // ✅ Default Constructor (Required for Retrofit)
    public Product() {}

    // ✅ Constructor to Assign Values
    public Product(String name, String sustainabilityInfo) {
        this.name = name;
        this.sustainabilityInfo = sustainabilityInfo;
    }

    // ✅ Getter Methods
    public String getName() {
        return (name != null) ? name : "Unknown Product";
    }

    public String getSustainabilityInfo() {
        return (sustainabilityInfo != null) ? sustainabilityInfo.toUpperCase() : "No sustainability data";
    }

    // ✅ Setter Methods (Needed for Retrofit)
    public void setName(String name) {
        this.name = name;
    }

    public void setSustainabilityInfo(String sustainabilityInfo) {
        this.sustainabilityInfo = sustainabilityInfo;
    }
}
