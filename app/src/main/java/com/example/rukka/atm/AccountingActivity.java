package com.example.rukka.atm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountingActivity.this, AddActivity.class));
            }
        });

        recyclerView = findViewById(R.id.accounting_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = new ExpenseHelper(this);
        Cursor cursor = helper.getReadableDatabase()
                .query("expense",
                null, null, null,
                null, null, null);
        Adapter adapter = new Adapter(cursor);
        recyclerView.setAdapter(adapter);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private final Cursor cursor;

        public Adapter(Cursor cursor) {
            this.cursor = cursor;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_accounting, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            cursor.moveToPosition(position);
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String info = cursor.getString(cursor.getColumnIndex("info"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));

            holder.textDate.setText(date);
            holder.textInfo.setText(info);
            holder.textAmount.setText(String.valueOf(amount));
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textDate;
            TextView textInfo;
            TextView textAmount;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textDate = itemView.findViewById(R.id.text_date);
                textInfo = itemView.findViewById(R.id.text_info);
                textAmount = itemView.findViewById(R.id.text_amount);
            }
        }
    }
}