package com.example.thecakewalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thecakewalk.Models.ProductDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProduct extends AppCompatActivity {

    //declare variables
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button button;
    DatabaseReference ref; //firebase access class reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ref = FirebaseDatabase.getInstance().getReference("ProductDetails"); //create path to "ProductDetails" table

        //assign ID's to variables
        editText1 = (EditText)findViewById(R.id.pcode);
        editText2 = (EditText)findViewById(R.id.pname);
        editText3 = (EditText)findViewById(R.id.pprice);
        editText4 = (EditText)findViewById(R.id.pdescription);
        button = (Button)findViewById(R.id.addbtn);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //assign values to strings
                final String code = editText1.getText().toString();
                final String name = editText2.getText().toString();
                final String price = editText3.getText().toString();
                final String description = editText4.getText().toString();

                //check if data is empty
                if (code.isEmpty()) {
                    editText1.requestFocus();
                    editText1.setError("Product Code is required");
                }else if(code.length() != 4){
                    editText1.requestFocus();
                    editText1.setError("Enter correct Code format");
                }else if (name.isEmpty()) {
                    editText2.requestFocus();
                    editText2.setError("Product Name is required");
                }else if(!name.matches("[a-zA-Z ]+")) {
                        editText2.requestFocus();
                        editText2.setError("Enter only letters");
                }else if (price.isEmpty()) {
                    editText3.requestFocus();
                    editText3.setError("Product Price is required");
                } else if(price.length() > 5){
                    editText3.requestFocus();
                    editText3.setError("Product Price exceeds limit");
                }else if (description.isEmpty()) {
                    editText4.requestFocus();
                    editText4.setError("Product Description is required");
                }else {

                    ProductDetails productDetails = new ProductDetails(code,name,price,description); //set string values to model class constructor
                    ref.child(code).setValue(productDetails); //save details to referenced table using code value

                    Toast.makeText(AddProduct.this, "Successfully added", Toast.LENGTH_SHORT).show(); //display add success toast message

                    Intent intent = new Intent(AddProduct.this, ManageProduct.class); //return to ManageProduct
                    startActivity(intent);
                }
            }
        });
    }
}