package gabriel.medpel.gilraphal.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

import gabriel.medpel.gilraphal.R;
import gabriel.medpel.gilraphal.model.Item;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    ArrayList<Item> items;
    ArrayList<Item> itemsCopy = new ArrayList<>();
    Context ctx;

    public ItemsAdapter(ArrayList<Item> items) {
        this.items = items;
        itemsCopy.addAll(items);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View championItem = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item,
                parent,
                false
        );
        return new ViewHolder(championItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item item = items.get(position);

        holder.tvGlassType.setText(item.getGlassType());
        holder.tvSize.setText(item.getHeight() + " X " + item.getWidth());
        holder.tvLine.setText( "שורה:  " + item.getLine());
        holder.tvPlace.setText( "עמדה:  " + item.getPlace());

        holder.btnEdit.setOnClickListener(view -> {
            LinearLayout layout = new LinearLayout(view.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);


            final EditText glassTypeBox = new EditText(view.getContext());
            glassTypeBox.setHint("סוג");
            glassTypeBox.setText(item.getGlassType());
            layout.addView(glassTypeBox);

            final EditText heightBox = new EditText(view.getContext());
            heightBox.setHint("גובה");
            heightBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            heightBox.setText(item.getHeight() + "");
            layout.addView(heightBox);

            final TextView xText = new TextView(view.getContext());
            xText.setText("X");
            layout.addView(xText);

            final EditText widthBox = new EditText(view.getContext());
            widthBox.setHint("רוחב");
            widthBox.setText(item.getWidth() + "");
            widthBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(widthBox);

            final EditText lineBox = new EditText(view.getContext());
            lineBox.setHint("שורה");
            lineBox.setText(item.getLine() + "");
            lineBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(lineBox);

            final EditText placeBox = new EditText(view.getContext());
            placeBox.setHint("עמדה");
            placeBox.setText(item.getPlace() + "");
            placeBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(placeBox);

            AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
            b.setTitle("הוסף: ").setView(layout).setNegativeButton("בטל", (dialog, which) -> dialog.dismiss()).setPositiveButton("שמור", ((dialog, which) -> {
                String glassType = glassTypeBox.getText().toString();
                int height = Integer.parseInt(heightBox.getText().toString());
                int width = Integer.parseInt(widthBox.getText().toString());
                int line = Integer.parseInt(lineBox.getText().toString());
                int place = Integer.parseInt(placeBox.getText().toString());
                Item newItem = new Item(glassType,height, width, line, place);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("items");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Item i = ds.getValue(Item.class);
                            if (i.equals(item)) {
                                ds.getRef().setValue(newItem);
                                notifyDataSetChanged();
                                Toast.makeText(view.getContext(), "שמור !", Toast.LENGTH_SHORT).show();

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }));
            b.show();
        });

        holder.btnDelete.setOnClickListener(view -> {

            AlertDialog.Builder b = new AlertDialog.Builder(holder.itemView.getContext());
            b.setTitle("אתה רוצה למחוק?").setNegativeButton("לא", (dialog, which) -> dialog.dismiss()).setPositiveButton("כן", ((dialog, which) -> {

                FirebaseDatabase.getInstance().getReference("items").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Item i = ds.getValue(Item.class);
                            if (i.equals(item)) {
                                ds.getRef().removeValue();
                                items.remove(position);
                                itemsCopy.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                break;
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                Toast.makeText(holder.itemView.getContext(), "שמור !", Toast.LENGTH_SHORT).show();
            }));
            b.show();

        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filter(String text) {

        items.clear();
        if(text.isEmpty()){
            items.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(Item item: itemsCopy){
                String str = item.getGlassType() + " " + item.getHeight() + " " + item.getWidth();
                if(str.contains(text)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvGlassType;
        TextView tvSize;
        TextView tvLine;
        TextView tvPlace;
        Button btnDelete;
        Button btnEdit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGlassType = itemView.findViewById(R.id.tvGlassType);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvLine = itemView.findViewById(R.id.tvLine);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);


        }
    }
}
