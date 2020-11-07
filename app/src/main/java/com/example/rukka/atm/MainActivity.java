package com.example.rukka.atm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOGGING = 100;
    boolean logon = false;
    private List<Function> functionsList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!logon) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGGING);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // RecyclerView
        recyclerView = findViewById(R.id.content_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setupFunctions();
        recyclerView.setAdapter(new Adapter());

//        recyclerView.setAdapter(new Adapter());
        /*recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(getResources().getStringArray(R.array.functions)));*/
    }

    private void setupFunctions() {
        String[] functionsNameList = getResources().getStringArray(R.array.functions);
        functionsList = new ArrayList<>();
        functionsList.add(new Function(R.drawable.ic_account_balance, functionsNameList[0]));
        functionsList.add(new Function(R.drawable.ic_transaction_histrory, functionsNameList[1]));
        functionsList.add(new Function(R.drawable.ic_accounting, functionsNameList[2]));
        functionsList.add(new Function(R.drawable.ic_foreign_exchange, functionsNameList[3]));
        functionsList.add(new Function(R.drawable.ic_contact, functionsNameList[4]));
        functionsList.add(new Function(R.drawable.ic_exit, functionsNameList[5]));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGGING) {
            if (resultCode != RESULT_OK) {
                finish();
            } else {
                logon = true;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    private void itemClicked(View v, Function function) {
        switch (function.getItemImageResource()) {
            case R.drawable.ic_account_balance:
            case R.drawable.ic_foreign_exchange:
//                Toast.makeText(this, function.getItemName(), Toast.LENGTH_LONG).show();
                Snackbar.make(v, function.getItemName(), Snackbar.LENGTH_SHORT).show();
                break;
            case R.drawable.ic_transaction_histrory:
                startActivity(new Intent(this, TransActivity.class));
                break;
            case R.drawable.ic_accounting:
                startActivity(new Intent(this, AccountingActivity.class));
                break;
            case R.drawable.ic_contact:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.drawable.ic_exit:
            default:
                finish();
                break;
        }
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_main, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.imageItem.setImageResource(functionsList.get(position).getItemImageResource());
            holder.textItem.setText(functionsList.get(position).getItemName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked(v, functionsList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return functionsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageItem;
            TextView textItem;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageItem = itemView.findViewById(R.id.image_grid_item);
                textItem = itemView.findViewById(R.id.text_grid_item);
            }
        }

    }
}