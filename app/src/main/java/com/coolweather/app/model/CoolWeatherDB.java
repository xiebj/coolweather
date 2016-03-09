package com.coolweather.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade on 2016/3/6.
 */
public class CoolWeatherDB {
    /*
    数据库名
     */
    public static final String DB_NAME = "cool_weather";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将Province实例存储到数据库
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        String[] t = new String[]{"province_or_city", "SUM(city_id)"};
        Cursor cursor = db.query("city", t, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("city_id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_or_city")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("city_id")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将City实例存储到数据库
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("city", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将County实例存储到数据库
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }

    /**
     * 从数据库读取某市下所有的县信息
     */
    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void savechengshi(Chengshi city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_or_county_zh", city.getCityName());
            values.put("city_id", city.getCityCode());
            db.insert("city", null, values);
        }
    }

    /**
     * 从数据库读取中国所有的城市，并与搜索栏中的匹配并筛选出来
     */
    public List<Chengshi> searchcity(String key) {
        List<Chengshi> list = new ArrayList<Chengshi>();
        //Cursor cursor = db.query("city", null, "city_or_county_zh = ?", new String[]{key}, null, null, null);
        String sql = "select * from city where city_or_county_zh = ? or area = ?";
        String[] selectionArgs = new String[]{key, key};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                Chengshi city = new Chengshi();
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_or_county_zh")));
                city.setAreaNmae(cursor.getString(cursor.getColumnIndex("area")));
                city.setProvinceName(cursor.getString(cursor.getColumnIndex("province_or_city")));
                String temp = cursor.getString(cursor.getColumnIndex("city_id"));
                city.setCityCode(temp.substring(2, temp.length()));
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
