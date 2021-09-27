package com.example.thecakewalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartItemList extends AppCompatActivity {

    ListView listView;
    List<CartAddedItems> user;
    DatabaseReference ref;
    String idd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_list);

        listView = (ListView) findViewById(R.id.cartlist);
        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("CartItems");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    CartAddedItems cartAddedItems = studentDatasnap.getValue(CartAddedItems.class);
                    user.add(cartAddedItems);

                }

                MyAdapter adapter = new MyAdapter(CartItemList.this, R.layout.custom_cart_deails, (ArrayList<CartAddedItems>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        ImageView imageView1;
        Button button1;
        Button button2;
        Button button3;
        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
    }

    class MyAdapter extends ArrayAdapter<CartAddedItems> {
        LayoutInflater inflater;
        Context myContext;
        List<CartAddedItems> user;


        public MyAdapter(Context context, int resource, ArrayList<CartAddedItems> objects) {
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
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_cart_deails, null);

                holder.COL1 = (TextView) view.findViewById(R.id.cartitem_idd);
                holder.COL2 = (TextView) view.findViewById(R.id.cartitem_Namee);
                holder.COL3 = (TextView) view.findViewById(R.id.cartitem_Pricce);
                holder.COL4 = (TextView) view.findViewById(R.id.cartitem_Uqty);
                holder.imageView1 = (ImageView) view.findViewById(R.id.cartitem_imageee);
                holder.button1 = (Button) view.findViewById(R.id.cartitemedit);
                holder.button2 = (Button) view.findViewById(R.id.cartitemdelete);
                holder.button3 = (Button) view.findViewById(R.id.confirm);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getId());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getPrice());
            holder.COL4.setText(user.get(position).getQty());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView1);
            System.out.println(holder);

            idd = user.get(position).getId();

            holder.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_confirm, null);
                    dialogBuilder.setView(view1);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            });

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String idd = user.get(position).getId();
                                    FirebaseDatabase.getInstance().getReference("CartItems").child(idd).removeValue();
                                    //remove function not written
                                    Toast.makeText(myContext, "Item deleted successfully", Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_cart_item, null);
                    dialogBuilder.setView(view1);
                    final TextView textView1 = (TextView) view1.findViewById(R.id.updateCartitem_idd);
                    final TextView textView2 = (TextView) view1.findViewById(R.id.updateCartitem_namee);
                    final TextView textView3 = (TextView) view1.findViewById(R.id.updateCartitem_pricee);
                    final ImageView imageView1 = (ImageView) view1.findViewById(R.id.updateCartitem_imagee);
                    final EditText editText1 = (EditText) view1.findViewById(R.id.updateitemcuqty);
                    final Button buttonupdate = (Button) view1.findViewById(R.id.itemucartnow);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CartItems").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String qty = (String) snapshot.child("qty").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            editText1.setText(qty);
                            textView1.setText(id);
                            textView2.setText(name);
                            textView3.setText(price);
                            Picasso.get().load(image).into(imageView1);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    buttonupdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String qty = editText1.getText().toString();

                           if (qty.isEmpty()) {
                                editText1.setError("Quantity is required");
                            } else {

                                HashMap map = new HashMap();
                                map.put("qty", qty);
                                reference.updateChildren(map);

                                Toast.makeText(CartItemList.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            return view;

        }
    }
}