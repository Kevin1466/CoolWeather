package kyle.me.coolweather.data.model.place;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    @SerializedName("name")
    public String provinceName;
    @SerializedName("id")
    public int provinceCode;
    transient int id = 0;

    public Province(String provinceName, int provinceCode) {
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
    }
}
