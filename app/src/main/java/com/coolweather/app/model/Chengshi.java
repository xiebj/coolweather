package com.coolweather.app.model;

/**
 * Created by wade on 2016/3/8.
 */
public class Chengshi {
    private int id;
    private String cityName;
    private String cityCode;
    private String areaName;
    private String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setAreaNmae(String areaNmae) {
        this.areaName = areaNmae;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getProvinceName() {
        return provinceName;
    }
}
