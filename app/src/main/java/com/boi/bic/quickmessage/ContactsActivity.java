package com.boi.bic.quickmessage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import timber.log.Timber;

public class ContactsActivity extends AppCompatActivity {

    static final int PICK_CONTACT_REQUEST = 1;
    protected ArrayList<Contact> contactList;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Timber.plant(new Timber.DebugTree());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null || !savedInstanceState.containsKey("contactList")) {
            loadData();
        }
        else {
            contactList = savedInstanceState.getParcelableArrayList("contactList");
        }

//        if(!hasContacts()) {
//            Contact contact = new Contact("Testing tester", "000-000-000");
//            contactList.add(contact);
//            Gson gson = new Gson();
//            String json = gson.toJson(contactList);
//            Timber.d("oncreate: " + json);
//
//        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                // Where to get contacts after clicking on the 'fab' button
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                //Start activity for result so it would popup the contacts select screen and able to retrieve the result for later usage
                //if not it would just be startActivity.
                startActivityForResult(intent, PICK_CONTACT_REQUEST);

            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactList);
        editor.putString("contactList", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contactList", null);
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();
        contactList = gson.fromJson(json, type);

        if (contactList == null) {
            contactList = new ArrayList<>();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("contactList", contactList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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
    //From Google documentation
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using <code><a href="/reference/android/content/CursorLoader.html">CursorLoader</a></code> to perform the query.
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                Gson gson = new Gson();
                String json = gson.toJson(contactList);
                Timber.d("Result before adding name: " + json);
                // Retrieve the phone number from the NUMBER column
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                Contact contact = new Contact(name,number);

                //not working here
                if(!contactList.contains(contact.getName())){
                    contactList.add(contact);
                }
                else {
                    Timber.d("Result: already added" );
                }
                Timber.d("Result: " + gson.toJson(contactList));

            }
        }
    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences mPref = this.getSharedPreferences("contacts", MODE_PRIVATE);
//        Editor prefsEditor = mPref.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(contactList);
//        prefsEditor.putString("MyContactList", json);
//        prefsEditor.commit();
//
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences mPref = this.getSharedPreferences("contacts", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = mPref.getString("MyContactList", "");
//        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
//        contactList = gson.fromJson(json, type);
//        Timber.d("Resume: " + json);
//
//    }

    private boolean hasContacts() {
        SharedPreferences mPref = this.getSharedPreferences("contacts", MODE_PRIVATE);
        boolean hasKey = mPref.contains("contacts");
        Timber.d("If has contacts: " + hasKey);

        return hasKey;
    }

}
