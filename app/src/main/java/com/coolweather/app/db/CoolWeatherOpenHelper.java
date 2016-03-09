package com.coolweather.app.db;

import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wade on 2016/3/6.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final String file = "city.sql";
    /**
     * 建立一个全国所有城市的id表
     */
    public static final String CREATE_CHINA =
            "create table city ("
                    + "city_id text primary key, "
                    + "city_or_county_en text, "
                    + "city_or_county_zh text, "
                    + "area text, "
                    + "province_or_city text)";
    /**
     * Province表建表语句
     */
    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text)";
    /**
     * City表建表语句
     */
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "city_code text, "
            + "province_id integer)";
    /**
     * County表建表语句
     */
    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "city_id integer)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeAssetsSQL(db, "city.sql");
        //db.execSQL(CREATE_CHINA);
        //db.execSQL(CREATE_CITY);
        //db.execSQL(CREATE_PROVINCE);
        //db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 读取数据库文件（.sql），并执行sql语句!重点在于assets下放文件然后读取
     */
    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getClass().getClassLoader().getResourceAsStream("assets/" + file)));
            String line;
            String buffer = "";
            while ((line = in.readLine()) != null) {
                buffer += line;
                if (line.trim().endsWith(";")) {
                    db.execSQL(buffer.replace(";", ""));
                    buffer = "";
                }
            }
        } catch (IOException e) {
            Log.e("db-error", e.toString());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                Log.e("db-error", e.toString());
            }
        }
    }

}
