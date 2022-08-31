package com.lemzeeyyy.contactmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ContactAdapter contactAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Favorite Contacts");

        // RecyclerVIew
        recyclerView = findViewById(R.id.recycler_view_contacts);
        db = new DatabaseHandler(this);

        // Contacts List
        contactArrayList.addAll(db.getAllContacts());

        contactAdapter = new ContactAdapter(this, contactArrayList,MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewContact(0,null);
            }
        });
    }

    private void addNewContact(int pos, Contact contact) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_add_contact,null);
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alerDialogBuilder.setView(view);
        TextView contactName = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText newPhoneNumber = view.findViewById(R.id.phone_num);
        contactName.setText("Add New Contact");
        alerDialogBuilder.setCancelable(true);
        alerDialogBuilder.setPositiveButton("save", (dialogInterface, i) -> {

        }).setNegativeButton("cancel",null);
        final AlertDialog alertDialog = alerDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
            if(TextUtils.isEmpty(newContact.getText().toString()) ||
                    TextUtils.isEmpty(newPhoneNumber.getText().toString())){
                Toast.makeText(MainActivity.this, "Please Enter Missing Column", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                alertDialog.dismiss();
            }
            CreateContact(new Contact(pos,newContact.getText().toString(),newPhoneNumber.getText().toString()));
        });

    }
    private void DeleteContact(Contact contact, int position) {

        contactArrayList.remove(position);
        db.deleteContact(contact);
        contactAdapter.notifyDataSetChanged();


    }



    private void CreateContact(Contact contact) {
        String name = contact.getName();
        String num = contact.getPhoneNumber();
        db.addContact(contact);
        if (contact != null) {
            contactArrayList.add(0, contact);
            contactAdapter.notifyDataSetChanged();
        }
    }


        @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}