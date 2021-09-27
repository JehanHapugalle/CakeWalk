package com.example.thecakewalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thecakewalk.Models.CartAddedItems;
import com.example.thecakewalk.Models.DeliverAddedItems;
import com.example.thecakewalk.Models.DeliveryItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomerHome extends AppCompatActivity {

    Button button;
    ListView listView;
    List<DeliveryItems> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        listView = (ListView) findViewById(R.id.listviewdel);
        button = (Button) findViewById(R.id.gotodelItms);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerHome.this, CartItemList.class);
                startActivity(intent);
            }
        });

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("AllProducts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    DeliveryItems deliveryItems = studentDatasnap.getValue(DeliveryItems.class);
                    user.add(deliveryItems);
                }

                MyAdapter adapter = new MyAdapter(CustomerHome.this, R.layout.custom_cart_items, (ArrayList<DeliveryItems>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        ImageView imageView;
        TextView COL1;
        TextView COL2;
        Button button;
    }

    class MyAdapter extends ArrayAdapter<DeliveryItems> {
        LayoutInflater inflater;
        Context myContext;
        List<DeliveryItems> user;


        public MyAdapter(Context context, int resource, ArrayList<DeliveryItems> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final DeliveryList.ViewHolder holder;
            if (view == null) {
                holder = new DeliveryList.ViewHolder();
                view = inflater.inflate(R.layout.custom_cart_items, null);

                holder.COL1 = (TextView) view.findViewById(R.id.item_name);
                holder.COL2 = (TextView) view.findViewById(R.id.item_price);
                holder.imageView = (ImageView) view.findViewById(R.id.item_image);
                holder.button = (Button) view.findViewById(R.id.delivernow);


                view.setTag(holder);
            } else {

                holder = (DeliveryList.ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getName());
            holder.COL2.setText(user.get(position).getPrice());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = inflater.inflate(R.layout.custom_cart_item_user, null);
                    dialogBuilder.setView(view);

                    final TextView textView1 = (TextView) view.findViewById(R.id.item_namee);
                    final TextView textView2 = (TextView) view.findViewById(R.id.item_pricee);
                    final TextView textView3 = (TextView) view.findViewById(R.id.item_desdvriptionn);
                    final ImageView imageView1 = (ImageView) view.findViewById(R.id.item_imagee);
                    final EditText editText6 = (EditText) view.findViewById(R.id.itemitemcuqty);
                    final Button buttonAdd = (Button) view.findViewById(R.id.itemuordernow);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AllProducts").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String image = (String) snapshot.child("image").getValue();
                            String description = (String) snapshot.child("description").getValue();

                            textView1.setText(name);
                            textView2.setText(price);
                            textView3.setText(description);
                            Picasso.get().load(image).into(imageView1);

                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CartItems");

                                    final String qty = editText6.getText().toString();

                                    String image = snapshot.child("image").getValue().toString();

                               if (qty.isEmpty()) {
                                        editText6.setError("Quantity is required");
                                    }else {

                                        String name = textView1.getText().toString();
                                        Integer price = Integer.valueOf(textView2.getText().toString());
                                        Integer qtyy = Integer.valueOf(editText6.getText().toString());

                                        String total = String.valueOf(price * qtyy);

                                        CartAddedItems cartAddedItems = new CartAddedItems(id,name,total,qty,image);
                                        reference.child(id).setValue(cartAddedItems);

                                        Toast.makeText(CustomerHome.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }

            });

            return view;

        }
    }
}