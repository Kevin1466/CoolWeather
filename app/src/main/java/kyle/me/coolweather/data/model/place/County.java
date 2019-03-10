package kyle.me.coolweather.data.model.place;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {
    public int cityId = 0;
    @SerializedName("name")
    public String countyName;
    @SerializedName("weather_id")
    public String weatherId;
    transient int id = 0;

    public County(String countyName, String weatherId) {
        this.countyName = countyName;
        this.weatherId = weatherId;
    }
}
