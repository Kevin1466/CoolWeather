package kyle.me.coolweather.data.db;

import org.litepal.LitePal;

import java.util.List;

import kyle.me.coolweather.data.model.place.City;
import kyle.me.coolweather.data.model.place.County;
import kyle.me.coolweather.data.model.place.Province;

public class PlaceDao {
    public List<Province> getProvinceList() {
        return LitePal.findAll(Province.class);
    }

    public List<City> getCityList(int provinceId) {
        return LitePal.where("provinceId = ?", String.valueOf(provinceId)).find(City.class);
    }

    public List<County> getCountyList(int cityId) {
        return LitePal.where("cityId = ?", String.valueOf(cityId)).find(County.class);
    }

    public void saveProvinceList(List<Province> provinceList) {
        if (provinceList != null && !provinceList.isEmpty()) {
            LitePal.saveAll(provinceList);
        }
    }

    public void saveCityList(List<City> cityList) {
        if (cityList != null && !cityList.isEmpty()) {
            LitePal.saveAll(cityList);
        }
    }

    public void saveCountyList(List<County> countyList) {
        if (countyList != null && !countyList.isEmpty()) {
            LitePal.saveAll(countyList);
        }
    }
}
