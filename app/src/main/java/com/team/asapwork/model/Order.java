package com.team.asapwork.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ehab on 7/1/16.
 */
public class Order {
    public String uid;
    public String category;
    public String location;
    public String details;

    public Order() {
    }

    public Order(String uid, String Category, String location, String details) {
        this.uid = uid;
        this.category = Category;
        this.details = details;
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return details;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("category", category);
        result.put("location", location);
        result.put("details", details);

        return result;
    }
}
