package gabriel.medpel.gilraphal.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import gabriel.medpel.gilraphal.model.Item;
import gabriel.medpel.gilraphal.model.ItemDataSource;

public class ItemsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Item>> item;

    public ItemsViewModel() {
        item = new MutableLiveData<>();
        ItemDataSource ds = new ItemDataSource();
        ds.readDemo(item);
    }

    public LiveData<ArrayList<Item>> getItems() {
        return item;
    }

}
