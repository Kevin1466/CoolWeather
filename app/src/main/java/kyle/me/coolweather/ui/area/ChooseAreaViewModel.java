package kyle.me.coolweather.ui.area;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import kyle.me.coolweather.data.PlaceRepository;
import kyle.me.coolweather.data.Resource;
import kyle.me.coolweather.data.model.place.City;
import kyle.me.coolweather.data.model.place.County;
import kyle.me.coolweather.data.model.place.Province;

// dependent on repository
public class ChooseAreaViewModel extends ViewModel {
    public int currentLevel;
    public Province selectedProvince;
    public City selectedCity;
    public List<Province> provinceList;
    public List<City> cityList;
    public List<County> countyList;
    public ArrayList<String> dataList = new ArrayList<>();
    private PlaceRepository mPlaceRepository;

    public ChooseAreaViewModel(PlaceRepository placeRepository) {
        mPlaceRepository = placeRepository;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Province getSelectedProvince() {
        return selectedProvince;
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public List<County> getCountyList() {
        return countyList;
    }

    public ArrayList<String> getDataList() {
        return dataList;
    }

    /**************** business logics *****************/
    public LiveData<Resource<List<Province>>> getProvinceList() {
        //get province data from repository
        return mPlaceRepository.getProvinceList();
    }

    public LiveData<Resource<List<City>>> getCityList(int provinceId) {
        return mPlaceRepository.getCityList(provinceId);
    }

    public LiveData<Resource<List<County>>> getCountyList(int provinceId, int cityId) {
        return mPlaceRepository.getCountyList(provinceId, cityId);
    }
}
