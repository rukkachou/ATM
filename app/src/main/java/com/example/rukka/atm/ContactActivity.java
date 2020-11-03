package com.example.rukka.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_CONTACTS = 8;
    private static final String TAG = ContactActivity.class.getSimpleName();
    List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            showContact();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_upload) {
            uploadContacts();
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadContacts() {
        String name = getSharedPreferences("atm", MODE_PRIVATE)
                .getString("ACCOUNT", null);
        if (name != null) {
            FirebaseDatabase.getInstance().getReference("users")
                    .child(name)
                    .child("contacts")
                    .setValue(contactList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContact();
            } else {
                Toast.makeText(this, "Do not get the permission: " + permissions[0], Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showContact() {
        Cursor contactsCursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (contactsCursor.moveToNext()) {
            int id = contactsCursor.getInt(
                    contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = contactsCursor.getString(
                    contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Contact contact = new Contact(id, name);
            Log.d(TAG, "showContact: " + name);
            if (contactsCursor.getInt(
                    contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 1) {
                Cursor commonCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{String.valueOf(id)},
                        null);
                while (commonCursor.moveToNext()) {
                    String phoneNumber = commonCursor.getString(
                            commonCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.getPhones().add(phoneNumber);
                    Log.d(TAG, "showContact: \t" + phoneNumber);
                }
            }
            contactList.add(contact);
        }
        RecyclerView recyclerView = findViewById(R.id.contact_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new Adapter());
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Contact contact = contactList.get(position);
            holder.name.setText(contact.getName());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < contact.getPhones().size(); i++) {
                String phone = contact.getPhones().get(i);
                stringBuilder.append(phone);
                if (i != contact.getPhones().size()-1) {
                    stringBuilder.append(" / ");
                }
            }
            holder.phone.setText(stringBuilder);
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView name;
            private final TextView phone;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.text_name_item);
                phone = itemView.findViewById(R.id.text_phone_item);
            }
        }
    }
}