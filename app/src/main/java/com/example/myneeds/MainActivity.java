package com.example.myneeds;

import android.content.Intent;
import android.os.Bundle;

import com.example.myneeds.data.DatabaseHandler;
import com.example.myneeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText myItem;
    private EditText itemQuantity;
    private EditText itemPrice;
    private EditText itemSize;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);

        //calling the byPassActivity() to decide which screen to show
        byPassActivity();

        //TODO: Check if the item was saved
        List<Item> items = databaseHandler.getAllItems();

        for (Item item : items) {
            Log.d("Saved Items", "onCreate: " + item.getItemName() + " " + item.getDateAdded() + " " + item.getItemQuantity());
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                createPopupDialog();

            }
        });
    }

    private void byPassActivity() {
        /*The idea is to check if there is any item present in the database, while the application loads up. If it's there, take the
         user to the ListActivity directly.*/
        if (databaseHandler.getItemCount() > 0) {

            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
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
        alertDialog = builder.create(); //creating our dialog object
        alertDialog.show(); //imp step
    }

    private void saveItem(View view) {
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
        Snackbar.make(view, "Item Saved!", Snackbar.LENGTH_SHORT).show();

        /*We can make use of the Handler class, whenever we want to delay the execution of a piece of code*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to run
                alertDialog.dismiss();

                //Todo: Move to next screen - details screen
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1200);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
