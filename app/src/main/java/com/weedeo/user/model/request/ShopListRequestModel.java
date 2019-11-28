package com.weedeo.user.model.request;

import java.util.List;

public class ShopListRequestModel {

    /**
     * from : 0
     * size : 20
     * search : string
     * location : {"lat":40,"lon":70}
     * id : 7864g1hh1v4h11h
     * categories : ["5db7b8f5224ca70008372052"]
     * subcategories : ["5da84d95a7b78200071f83a9"]
     */

    private int from;
    private int size;
    private String search;
    private String id;
    private LocationBean location;
    private List<String> categories;
    private List<String> subcategories;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class LocationBean {
        /**
         * lat : 40
         * lon : 70
         */

        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
