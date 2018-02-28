package fr.upem.crazygame.chat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import fr.upem.crazygame.R;

/**
 * Created by dagama on 20/02/18.
 */

public class ContentProviderExempleActivity extends Activity {
    private ListView listView;
    private CustomListChat adapter;

    private AndroidProvider tchat = new AndroidProvider();
    private List<String> messagesList = new ArrayList<>();
    private List<String> usersList = new ArrayList<>();
    private List<String> datesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //insertRecords();

        initMessageList();

        adapter = new CustomListChat(this, messagesList, usersList, datesList);
        listView=(ListView)findViewById(android.R.id.list);
        listView.setSelection(messagesList.size());
        listView.setAdapter(adapter);
    }

    public void sendMessage(View view){
        EditText edidText = (EditText)findViewById(R.id.editText);
        String message = edidText.getText().toString();
        edidText.setText("");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        Date date = new Date();
        String dateTime = dateFormat.format(date);
        Log.d("--------------", dateTime);

        String user = "Joueur 2";

        messagesList.add(message);
        usersList.add(user);
        datesList.add(dateTime);

        ContentValues contact = new ContentValues();
        contact.put(SharedInformation.Messages.MESSAGES_USER, user);
        contact.put(SharedInformation.Messages.MESSAGES_DATE, dateTime);
        contact.put(SharedInformation.Messages.MESSAGES_TEXT, message);
        getContentResolver().insert(tchat.CONTENT_URI, contact);

        adapter.notifyDataSetChanged();
        listView.setSelection(messagesList.size());
    }

    private void initMessageList() {
        String columns[] = new String[]{SharedInformation.Messages.MESSAGES_USER, SharedInformation.Messages.MESSAGES_DATE, SharedInformation.Messages.MESSAGES_TEXT};
        Uri mContacts = tchat.CONTENT_URI;
        Cursor cur = managedQuery(mContacts, columns, null, null, null);

        if (cur.moveToFirst()) {
            String name = null;
            do {
                messagesList.add(cur.getString(cur.getColumnIndex(SharedInformation.Messages.MESSAGES_TEXT)));
                usersList.add(cur.getString(cur.getColumnIndex(SharedInformation.Messages.MESSAGES_USER)));
                datesList.add(cur.getString(cur.getColumnIndex(SharedInformation.Messages.MESSAGES_DATE)));
            } while (cur.moveToNext());
        }
    }

    private void insertRecords() {
        ContentValues contact = new ContentValues();
        contact.put(SharedInformation.Messages.MESSAGES_USER, "Joueur 1");
        contact.put(SharedInformation.Messages.MESSAGES_DATE, "01/02/2018");
        contact.put(SharedInformation.Messages.MESSAGES_TEXT, "Introduction à la programmation sous Android");
        getContentResolver().insert(tchat.CONTENT_URI, contact);

        contact.clear();
        contact.put(SharedInformation.Messages.MESSAGES_USER, "Joueur 2");
        contact.put(SharedInformation.Messages.MESSAGES_DATE, "01/02/2018");
        contact.put(SharedInformation.Messages.MESSAGES_TEXT, "Introduction à la programmation sous Android");
        getContentResolver().insert(tchat.CONTENT_URI, contact);

        contact.clear();
        contact.put(SharedInformation.Messages.MESSAGES_USER, "Joueur 1");
        contact.put(SharedInformation.Messages.MESSAGES_DATE, "03/02/2018");
        contact.put(SharedInformation.Messages.MESSAGES_TEXT, "Introduction à la programmation sous Android");
        getContentResolver().insert(tchat.CONTENT_URI, contact);
    }


    public void startService(View view) {
        startService(new Intent(getBaseContext(), chatService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), chatService.class));
    }
}

