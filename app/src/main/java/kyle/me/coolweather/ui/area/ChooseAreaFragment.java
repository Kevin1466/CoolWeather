package kyle.me.coolweather.ui.area;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import kyle.me.coolweather.MainActivity;
import kyle.me.coolweather.R;
import kyle.me.coolweather.data.Resource;
import kyle.me.coolweather.data.model.place.City;
import kyle.me.coolweather.data.model.place.County;
import kyle.me.coolweather.data.model.place.Province;
import kyle.me.coolweather.ui.weather.WeatherActivity;
import kyle.me.coolweather.util.InjectorUtil;

public class ChooseAreaFragment extends Fragment {
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ChooseAreaViewModel areaViewModel;
    private ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;

    public ChooseAreaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);

        ViewModelProvider modelProvider = ViewModelProviders.of(this, InjectorUtil.getChooseAreaModelFactory());
        areaViewModel = modelProvider.get(ChooseAreaViewModel.class);
        areaViewModel.getProvinceList();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, areaViewModel.dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener((view, view1, posation, l) -> {
            switch (areaViewModel.getCurrentLevel()) {
                case LEVEL_PROVINCE:
                    areaViewModel.selectedProvince = areaViewModel.provinceList.get(posation);
                    queryCities();
                    break;
                case LEVEL_CITY:
                    areaViewModel.selectedCity = areaViewModel.cityList.get(posation);
                    queryCounties();
                    break;
                case LEVEL_COUNTY:
                    Activity thisActivity = getActivity();
                    String weatherId = areaViewModel.countyList.get(posation).weatherId;

                    if (thisActivity instanceof MainActivity) {
                        Intent intent = new Intent(thisActivity, WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        thisActivity.finish();
                    } else if (thisActivity instanceof WeatherActivity) {
                        WeatherActivity weatherActivity = (WeatherActivity) thisActivity;
                        weatherActivity.drawerLayout.closeDrawers();
                        weatherActivity.swipeRefresh.setRefreshing(true);
                        weatherActivity.weatherId = weatherId;
                        weatherActivity.observeWeather(weatherActivity.mWeatherViewModel.refreshWeather(weatherId, MainActivity.KEY), true);
                    }
            }
        });
        if (areaViewModel.dataList.isEmpty()) {
            queryProvinces();
        }
    }

    private <T> void handleData(LiveData<Resource<List<T>>> liveData, Action<List<T>> action) {
        liveData.observe(this, new Observer<Resource<List<T>>>() {
            @Override
            public void onChanged(Resource<List<T>> result) {
                if (result != null && result.status == Resource.LOADING) {
                    //show progress dlg
                    showProgressDialog();
                } else if (result != null && result.data != null && result.status == Resource.SUCCESS) {
                    //close progress dlg
                    closeProgressDialog();
                    areaViewModel.dataList.clear();
                    action.act(result.data);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                }
            }
        });
    }

    private void queryProvinces() {
        handleData(areaViewModel.getProvinceList(), new Action<List<Province>>() {
            @Override
            public void act(List<Province> data) {
                titleText.setText("中国");
                backButton.setVisibility(View.GONE);
                for (Province province : data) {
                    areaViewModel.dataList.add(province.provinceName);
                }
                areaViewModel.provinceList = data;
                areaViewModel.currentLevel = LEVEL_PROVINCE;
            }
        });
    }

    private void queryCities() {
        Province province = areaViewModel.selectedProvince;
        titleText.setText(province.provinceName);
        backButton.setVisibility(View.VISIBLE);
        handleData(areaViewModel.getCityList(province.provinceCode), new Action<List<City>>() {
            @Override
            public void act(List<City> list) {
                for (City city : list) {
                    areaViewModel.dataList.add(city.cityName);
                }
                areaViewModel.cityList = list;
                areaViewModel.currentLevel = LEVEL_CITY;
            }
        });
    }

    private void queryCounties() {
        City city = areaViewModel.selectedCity;
        titleText.setText(city.cityName);
        backButton.setVisibility(View.VISIBLE);
        handleData(areaViewModel.getCountyList(city.provinceId, city.cityCode), new Action<List<County>>() {
            @Override
            public void act(List<County> list) {
                for (County county : list) {
                    areaViewModel.dataList.add(county.countyName);
                }
                areaViewModel.countyList = list;
                areaViewModel.currentLevel = LEVEL_COUNTY;
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    interface Action<T> {
        void act(T data);
    }

}
