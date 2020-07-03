package com.example.myneeds.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.myneeds.ListActivity;
import com.example.myneeds.R;
import com.example.myneeds.data.DatabaseHandler;
import com.example.myneeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;


    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating the view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);


        //returning the view
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        //binding the view and the data

        Item item = itemList.get(position);

        holder.itemName.setText(MessageFormat.format("Item: {0}", item.getItemName()));
        holder.itemQuantity.setText(MessageFormat.format("Quantity: {0}", item.getItemQuantity()));
        holder.itemPrice.setText(MessageFormat.format("Price: {0}", item.getItemPrice()));
        holder.itemSize.setText(MessageFormat.format("Size: {0}", item.getItemSize()));
        holder.dateAdded.setText(MessageFormat.format("Date: {0}", item.getDateAdded()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //setting the view
        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemPrice;
        public TextView itemSize;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemName = itemView.findViewById(R.id.row_item_name);
            itemQuantity = itemView.findViewById(R.id.row_item_quantity);
            itemPrice = itemView.findViewById(R.id.row_item_price);
            itemSize = itemView.findViewById(R.id.row_item_size);
            dateAdded = itemView.findViewById(R.id.row_item_date);
            editButton = itemView.findViewById(R.id.row_edit_button);
            deleteButton = itemView.findViewById(R.id.row_delete_button);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Item item = itemList.get(position);

            switch (v.getId()) {

                case R.id.row_edit_button:
                    //edit item
                    position = getAdapterPosition();
                    item = itemList.get(position);
                    updateItem(item);
                    break;

                case R.id.row_delete_button:
                    //delete item
                    position = getAdapterPosition();
                    item = itemList.get(position);
                    deleteItem(item.getId());
                    break;
            }
        }

        private void updateItem(final Item item) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            Button saveButton;

            final TextView title;
            final EditText myItem;
            final EditText itemQuantity;
            final EditText itemPrice;
            final EditText itemSize;

            myItem = view.findViewById(R.id.my_item);
            itemQuantity = view.findViewById(R.id.item_quantity);
            itemPrice = view.findViewById(R.id.item_price);
            itemSize = view.findViewById(R.id.item_size);
            title = view.findViewById(R.id.title);
            saveButton = view.findViewById(R.id.save_button);


            //final Item item = databaseHandler.getItem(id);

            myItem.setText(item.getItemName());
            itemQuantity.setText(item.getItemQuantity());
            itemPrice.setText(String.valueOf(item.getItemPrice()));
            itemSize.setText(item.getItemSize());

            title.setText(R.string.update_item);
            saveButton.setText(R.string.update_btn);


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    item.setItemName(myItem.getText().toString().trim());
                    item.setItemQuantity(itemQuantity.getText().toString().trim());
                    item.setItemPrice(Float.parseFloat(itemPrice.getText().toString().trim()));
                    item.setItemSize(itemSize.getText().toString().trim());

                    if (!myItem.getText().toString().isEmpty()
                            && !itemQuantity.getText().toString().isEmpty()
                            && !itemPrice.getText().toString().isEmpty()
                            && !itemSize.getText().toString().isEmpty()) {


                        databaseHandler.updateItem(item);

                        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_SHORT).show();
                        notifyItemChanged(getAdapterPosition(), item);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                            }
                        }, 1200);


                    } else {
                        Snackbar.make(v, "Empty fields not allowed!", Snackbar.LENGTH_SHORT).show();
                    }


                }

            });

            builder.setView(view);

            alertDialog = builder.create();
            alertDialog.show();

        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_pop, null);

            Button noButton = view.findViewById(R.id.no_button);
            Button yesButton = view.findViewById(R.id.yes_button);

            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);
                    databaseHandler.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    alertDialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        }
    }
}
