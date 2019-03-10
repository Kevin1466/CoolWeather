package kyle.me.coolweather.ui.area;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import kyle.me.coolweather.data.PlaceRepository;

//NewInstanceFactory vs Factory?
public class ChooseAreaModelFactory extends ViewModelProvider.NewInstanceFactory {
    // dependent on repository
    private PlaceRepository mPlaceRepository;

    public ChooseAreaModelFactory(PlaceRepository placeRepository) {
        mPlaceRepository = placeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChooseAreaViewModel(mPlaceRepository);
    }


}
