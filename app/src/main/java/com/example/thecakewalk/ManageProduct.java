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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thecakewalk.Models.ProductDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageProduct extends AppCompatActivity {

    //declare variables
    Button button;
    ListView listView;
    List<ProductDetails> user;
    DatabaseReference ref; //firebase access class reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        button = (Button)findViewById(R.id.addProduct);
        listView = (ListView)findViewById(R.id.listview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProduct.this, AddProduct.class);
                startActivity(intent);
            }
        });

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("ProductDetails"); //create path to "ProductDetails" table

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    ProductDetails productDetails = studentDatasnap.getValue(ProductDetails.class);
                    user.add(productDetails);
                }

                MyAdapter adapter = new MyAdapter(ManageProduct.this, R.layout.custom_product_details, (ArrayList<ProductDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        Button button1;
        Button button2;
    }

    class MyAdapter extends ArrayAdapter<ProductDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<ProductDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<ProductDetails> objects) {
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
                view = inflater.inflate(R.layout.custom_product_details, null);

                holder.COL1 = (TextView) view.findViewById(R.id.ProductCode);
                holder.COL2 = (TextView) view.findViewById(R.id.ProductName);
                holder.COL3 = (TextView) view.findViewById(R.id.ProductPrice);
                holder.COL4 = (TextView) view.findViewById(R.id.ProductDescription);
                holder.button1 = (Button) view.findViewById(R.id.edit);
                holder.button2 = (Button) view.findViewById(R.id.delete);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("Code:- "+user.get(position).getId());
            holder.COL2.setText("Name:- "+user.get(position).getName());
            holder.COL3.setText("Price:- "+user.get(position).getPrice());
            holder.COL4.setText("Description:- "+user.get(position).getDescription());
            System.out.println(holder);

            holder.button2.setOnClickListener(new View.OnClickListener() { //delete product button
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this item?") //display alert dialog to confirm delete
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String idd = user.get(position).getId();
                                    FirebaseDatabase.getInstance().getReference("ProductDetails").child(idd).removeValue(); //remove all data from table under given ID
                                    //remove function not written
                                    Toast.makeText(myContext, "Item deleted successfully", Toast.LENGTH_SHORT).show(); //display delete success toast message

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel(); //dismiss confirm delete alert
                                }
                            })
                            .show();
                }
            });

            holder.button1.setOnClickListener(new View.OnClickListener() { //update product button
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_product_details, null); //display custom layout
                    dialogBuilder.setView(view1);

                    //assign ID's to the new update-variables
                    final EditText editText1 = (EditText) view1.findViewById(R.id.upcode);
                    final EditText editText2 = (EditText) view1.findViewById(R.id.upname);
                    final EditText editText3 = (EditText) view1.findViewById(R.id.upprice);
                    final EditText editText4 = (EditText) view1.findViewById(R.id.updescription);
                    final Button buttonupdate = (Button) view1.findViewById(R.id.updatebtn);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId(); // check ID in firebase
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProductDetails").child(idd); //retrieve firebase values
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //get values from table
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String description = (String) snapshot.child("description").getValue();

                            //display already existing data
                            editText1.setText(id);
                            editText2.setText(name);
                            editText3.setText(price);
                            editText4.setText(description);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    buttonupdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //assign new values
                            String name = editText2.getText().toString();
                            String price = editText3.getText().toString();
                            String description = editText4.getText().toString();

                            //check if data is empty
                            if (name.isEmpty()) {
                                editText2.setError("Product Name is required");
                            }else if (price.isEmpty()) {
                                editText3.setError("Product Price Number is required");
                            }else if (description.isEmpty()) {
                                editText4.setError("Product Description is required");
                            }else {

                                //create hashmap
                                HashMap map = new HashMap();
                                map.put("name", name);
                                map.put("price", price);
                                map.put("description", description);
                                reference.updateChildren(map); // reference and update table

                                //display update success toast message
                                Toast.makeText(ManageProduct.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss(); //dismiss update alert
                            }
                        }
                    });
                }
            });

            return view;

        }
    }
}
