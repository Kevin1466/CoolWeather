package kyle.me.coolweather.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import kyle.me.coolweather.data.db.PlaceDao;
import kyle.me.coolweather.data.model.place.City;
import kyle.me.coolweather.data.model.place.County;
import kyle.me.coolweather.data.model.place.Province;
import kyle.me.coolweather.data.network.WeatherNetwork;
import kyle.me.coolweather.util.CoolWeatherExecutors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {
    private static PlaceRepository sPlaceRepository;
    private PlaceDao mPlaceDao;
    private WeatherNetwork mWeatherNetwork;

    public PlaceRepository(PlaceDao dao, WeatherNetwork network) {
        this.mPlaceDao = dao;
        this.mWeatherNetwork = network;
    }

    public static PlaceRepository getInstance(PlaceDao placeDao, WeatherNetwork weatherNetwork) {
        if (sPlaceRepository == null) {
            synchronized (PlaceRepository.class) {
                if (sPlaceRepository == null) {
                    sPlaceRepository = new PlaceRepository(placeDao, weatherNetwork);
                }
            }
        }
        return sPlaceRepository;
    }

    public LiveData<Resource<List<Province>>> getProvinceList() {
        MutableLiveData<Resource<List<Province>>> liveData = new MutableLiveData<>();
        liveData.setValue(null);
        CoolWeatherExecutors.diskIO.execute(() -> {
            List<Province> provinceList = mPlaceDao.getProvinceList();
            if (provinceList.isEmpty()) {
                //mWeatherNetwork
                mWeatherNetwork.fetchProvinceList(new Callback<List<Province>>() {
                    @Override
                    public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                        // TODO: 09/03/2019 save in disk
                        List<Province> provinces = response.body();
                        mPlaceDao.saveProvinceList(provinces);
                        liveData.postValue(new Resource().success(provinces));
                    }

                    @Override
                    public void onFailure(Call<List<Province>> call, Throwable t) {
                        t.printStackTrace();
                        liveData.postValue(new Resource().error(null, "load failed"));
                    }
                });
            } else {
                liveData.postValue(new Resource<List<Province>>().success(provinceList));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<City>>> getCityList(int provinceId) {
        MutableLiveData<Resource<List<City>>> liveData = new MutableLiveData<>();
        CoolWeatherExecutors.diskIO.execute(() -> {
            List<City> cityList = mPlaceDao.getCityList(provinceId);
            if (cityList.isEmpty()) {
                mWeatherNetwork.fetchCityList(provinceId, new Callback<List<City>>() {
                    @Override
                    public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                        // TODO: 09/03/2019 save in disk
                        List<City> cities = response.body();
                        for (City city : cities) {
                            city.provinceId = provinceId;
                        }
                        mPlaceDao.saveCityList(cities);
                        liveData.postValue(new Resource().success(cities));
                    }

                    @Override
                    public void onFailure(Call<List<City>> call, Throwable t) {
                        t.printStackTrace();
                        liveData.postValue(new Resource().error(null, "load failed"));
                    }
                });
            } else {
                liveData.postValue(new Resource<List<City>>().success(cityList));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<County>>> getCountyList(int provinceId, int cityId) {
        MutableLiveData<Resource<List<County>>> liveData = new MutableLiveData<>();
        // FIXME: 09/03/2019
        //liveData.setValue(new Resource<>().loading(null));
        CoolWeatherExecutors.diskIO.execute(() -> {
            List<County> countyList = mPlaceDao.getCountyList(cityId);
            if (countyList.isEmpty()) {
                mWeatherNetwork.fetchCountyList(provinceId, cityId, new Callback<List<County>>() {
                    @Override
                    public void onResponse(Call<List<County>> call, Response<List<County>> response) {
                        CoolWeatherExecutors.diskIO.execute(new Runnable() {
                            @Override
                            public void run() {
                                List<County> counties = response.body();
                                for (County c : counties) {
                                    c.cityId = cityId;
                                }
                                mPlaceDao.saveCountyList(counties);
                                liveData.postValue(new Resource().success(counties));
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<County>> call, Throwable t) {
                        t.printStackTrace();
                        liveData.postValue(new Resource().error(null, "load failed"));
                    }
                });
            } else {
                liveData.postValue(new Resource<List<County>>().success(countyList));
            }
        });
        return liveData;
    }
}
