package gabriel.medpel.gilraphal.model;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemDataSource {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public void readDemo(MutableLiveData<ArrayList<Item>> callback){
        ArrayList<Item> array = new ArrayList<>();
        DatabaseReference myRef = database.getReference("items");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    array.add(item);
                }
                callback.postValue(array);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });





    }
}
