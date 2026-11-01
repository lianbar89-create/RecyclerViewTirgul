package com.example.recyclerviewtirgul;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Item> items;
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        items = new ArrayList<Item>();
        Item item1 = new Item("aa", "aaaaaa", 1);
        items.add(item1);
        Item item2 = new Item("bb", "bbbbbb", 2);
        items.add(item2);
        Item item3 = new Item("cc", "cccccc", 3);
        items.add(item3);
        Item item4 = new Item("dd", "dddddd", 4);
        items.add(item4);
        Item item5 = new Item("ee", "eeeeee", 5);
        items.add(item5);

        recyclerView = findViewById(R.id.recycler);
        adapter = new MyAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnLogOut = findViewById(R.id.logOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("filename", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("is_remembered", false);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(this);

        ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback
                (ItemTouchHelper.UP |
                        ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT |
                                ItemTouchHelper.LEFT )
        {
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();

                Collections.swap(items, fromPos, toPos);
                adapter.notifyItemMoved(fromPos, toPos);
                return true;
            }
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    items.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    adapter.notifyItemChanged(position);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add, null);
        builder.setView(dialogView);

        AlertDialog customDialog = builder.create();
        customDialog.show();

        Button btnCancel = dialogView.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "The action was canceled", Toast.LENGTH_LONG).show();
                customDialog.dismiss();
            }
        });

        Button btnOk = dialogView.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtName = dialogView.findViewById(R.id.name);
                EditText txtDes = dialogView.findViewById(R.id.des);
                EditText txtImage = dialogView.findViewById(R.id.image);

                String strName = txtName.getText().toString();
                String strDes = txtDes.getText().toString();
                String strImage = txtImage.getText().toString();

                int imageResource = R.drawable.dog;
                Item item = new Item(strName, strDes, imageResource);
                items.add(item);

                int newPosition = items.size() - 1;
                adapter.notifyItemInserted(newPosition);
                customDialog.dismiss();
            }
        });
    }
}