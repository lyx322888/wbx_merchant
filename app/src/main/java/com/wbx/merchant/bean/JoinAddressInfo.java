package com.wbx.merchant.bean;

import java.util.List;

/**
 * Created by wushenghui on 2018/1/24.
 */

public class JoinAddressInfo {
    private int province_id;
    private String name;
    private String first_letter;
    private List<City> city;


    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }



    public  static class City{
        private int city_id;
        private String name;
        private String first_letter;
        private List<Area> area;

        public List<Area> getArea() {
            return area;
        }

        public void setArea(List<Area> area) {
            this.area = area;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirst_letter() {
            return first_letter;
        }

        public void setFirst_letter(String first_letter) {
            this.first_letter = first_letter;
        }
    }
    public  static class Area{
        private int area_id;
        private String area_name;
        private String first_letter;

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getFirst_letter() {
            return first_letter;
        }

        public void setFirst_letter(String first_letter) {
            this.first_letter = first_letter;
        }

        public int getArea_id() {
            return area_id;
        }

        public void setArea_id(int area_id) {
            this.area_id = area_id;
        }
    }
}
