package com.ybennun.contactroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ybennun.contactroom.model.Contact;
import com.ybennun.contactroom.model.ContactViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private ContactViewModel contactViewModel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, contacts -> {
            StringBuilder builder = new StringBuilder();
            for (Contact contact : contacts) {
                builder.append("-").append(contact.getName()).append("-").append(contact.getOccupation());
                Log.d("TAG", "onCreate: " + contact.getName());
            }

        });

        FloatingActionButton fab = findViewById(R.id.add_contact_fab);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            assert data != null;
            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION);

            Contact contact = new Contact(name, occupation);

            ContactViewModel.insert(contact);
        }
    }
}