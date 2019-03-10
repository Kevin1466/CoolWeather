package kyle.me.coolweather.data.model.place;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {
    public int provinceId = 0;
    @SerializedName("name")
    public String cityName;
    @SerializedName("id")
    public int cityCode;
    transient int id = 0;

    public City(String cityName, int cityCode) {
        this.cityName = cityName;
        this.cityCode = cityCode;
    }
}
