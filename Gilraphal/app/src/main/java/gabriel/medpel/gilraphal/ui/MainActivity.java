package gabriel.medpel.gilraphal.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import gabriel.medpel.gilraphal.R;
import gabriel.medpel.gilraphal.model.Item;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvItems;
    private SearchView searchView;
    private Button btnAdd;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
        rvItems = findViewById(R.id.rvItems);
        searchView = findViewById(R.id.searchView);
        btnAdd = findViewById(R.id.btnAdd);

        searchView.clearFocus();





        ItemsViewModel itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);

        itemsViewModel.getItems().observe(this, items -> {
            rvItems.setLayoutManager(new LinearLayoutManager(this));
            ItemsAdapter adapter = new ItemsAdapter(items);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
                    return true;
                }
            });

            rvItems.setAdapter(adapter);
        });

        btnAdd.setOnClickListener(view -> {

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText glassTypeBox = new EditText(this);
            glassTypeBox.setHint("סוג");
            layout.addView(glassTypeBox);

            final EditText heightBox = new EditText(this);
            heightBox.setHint("גובה");
            heightBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(heightBox);

            final TextView xText = new TextView(this);
            xText.setText("X");
            layout.addView(xText);

            final EditText widthBox = new EditText(this);
            widthBox.setHint("רוחב");
            widthBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(widthBox);

            final EditText lineBox = new EditText(this);
            lineBox.setHint("שורה");
            lineBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(lineBox);

            final EditText placeBox = new EditText(this);
            placeBox.setHint("עמדה");
            placeBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(placeBox);



            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("הוסף: ").setView(layout).setNegativeButton("בטל", (dialog, which) -> dialog.dismiss()).setPositiveButton("שמור", ((dialog, which) -> {
                String glassType = glassTypeBox.getText().toString();
                int height = Integer.parseInt(heightBox.getText().toString());
                int width = Integer.parseInt(widthBox.getText().toString());
                int line = Integer.parseInt(lineBox.getText().toString());
                int place = Integer.parseInt(placeBox.getText().toString());
                Item item = new Item(glassType,height, width, line, place);

                DatabaseReference myRef = database.getReference("items");

                myRef.child(UUID.randomUUID().toString()).setValue(item);

                Toast.makeText(this, "שמור !", Toast.LENGTH_SHORT).show();
            }));
            b.show();
        });
    }
}
