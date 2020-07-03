package com.example.myneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myneeds.data.DatabaseHandler;
import com.example.myneeds.model.Item;
import com.example.myneeds.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;

    private Button saveButton;
    private EditText myItem;
    private EditText itemQuantity;
    private EditText itemPrice;
    private EditText itemSize;

    private FloatingActionButton fab;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);

        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        //getting all items from db
        itemList = databaseHandler.getAllItems();

        for (Item item : itemList) {
            Log.d("Activity List", "onCreate: " + item.getItemName());

        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
            }
        });

    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this); //instantiating the Dialog Builder
        View view = getLayoutInflater().inflate(R.layout.popup, null);//inflating the popup layout into the view object
        //As the views below doesn't belong to the main activity, we cannot instantiate it inside onCreate()
        myItem = view.findViewById(R.id.my_item); //getting references of all the views of the Alert Dialog
        itemQuantity = view.findViewById(R.id.item_quantity);
        itemPrice = view.findViewById(R.id.item_price);
        itemSize = view.findViewById(R.id.item_size);

        saveButton = view.findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*As we have to show a snackbar upon saving the item, and the snackbar needs to be attached to a view object, we will pass the view
                  object to it.*/
                if (!myItem.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemPrice.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty())
                    saveItem(v);
                else {
                    Snackbar.make(v, "Empty fields not allowed!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view); //finally setting our view

        /*The actual Alert Dialog will be created only by using the Dialog class*/
        dialog = builder.create(); //creating our dialog object
        dialog.show(); //imp step
    }

    private void saveItem(View v) {
        //Todo: Save each item into database

        Item item = new Item();

        //Getting values from the views
        String itemName = myItem.getText().toString().trim();
        String quantity = itemQuantity.getText().toString().trim();
        float price = Float.parseFloat(itemPrice.getText().toString().trim());
        String size = itemSize.getText().toString().trim();

        //Setting values for the item object
        item.setItemName(itemName);
        item.setItemQuantity(quantity);
        item.setItemPrice(price);
        item.setItemSize(size);

        //passing item object to the addItem() of the DatabaseHandler class
        databaseHandler.addItem(item);
        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_SHORT).show();

        /*We can make use of the Handler class, whenever we want to delay the execution of a piece of code*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to run
                dialog.dismiss();

                //Todo: Move to next screen - details screen
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();/*as we are being redirected to the same activity here, we must kill the prev activity by calling finish()
                or else, we will end  up with many activities behind*/

            }
        }, 1200);
    }
}
