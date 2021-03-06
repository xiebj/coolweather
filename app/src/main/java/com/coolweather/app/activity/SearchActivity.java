package com.coolweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.app.R;
import com.coolweather.app.model.Chengshi;
import com.coolweather.app.model.CoolWeatherDB;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
    private ProgressDialog progressDialog;
    private EditText et;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();
    private Chengshi selectedcity;
    private List<Chengshi> chengshiList;
    private Button s;
    private ImageView ivDeleteText;
    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        listView = (ListView) findViewById(R.id.list_view);
        et = (EditText) findViewById(R.id.edit_text);
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                et.setText("");
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });

        s = (Button) findViewById(R.id.seach);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = et.getText().toString();
                chengshiList = coolWeatherDB.searchcity(key);
                if (chengshiList.size() > 0) {
                    dataList.clear();
                    for (Chengshi city : chengshiList) {
                        //字符串比对记得用equal不能直接用==
                        if (city.getCityName().equals(city.getAreaName())) {
                            dataList.add(city.getCityName() + "-" + city.getProvinceName());
                        } else {
                            dataList.add(city.getCityName() + "-" + city.getAreaName());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                } else {
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SearchActivity.this, "请输入正确的市或县:)", Toast.LENGTH_SHORT).show();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedcity = chengshiList.get(position);
                String cityCode = selectedcity.getCityCode();
                Intent intent = new Intent(SearchActivity.this, WeatherActivity.class);
                intent.putExtra("county_code", cityCode);
                startActivity(intent);
                finish();
            }
        });

        Log.i("ser", "oncreated");
    }

    /**
     * 搜索用户想查找的城市
     *
     * @param menu
     * @return
     */
    private void querycity() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
