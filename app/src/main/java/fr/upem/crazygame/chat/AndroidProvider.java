package fr.upem.crazygame.chat;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by dagama on 20/02/18.
 */

public class AndroidProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.tutos.android.content.provider.tutosandroidprovider");
    public static final String CONTENT_PROVIDER_DB_NAME = "tchat.db";
    public static final int CONTENT_PROVIDER_DB_VERSION = 1;
    public static final String CONTENT_PROVIDER_TABLE_NAME = "tchat";
    public static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.item/vnd.tutos.android.content.provider.messages";

    public DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context,AndroidProvider.CONTENT_PROVIDER_DB_NAME, null, AndroidProvider.CONTENT_PROVIDER_DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + AndroidProvider.CONTENT_PROVIDER_TABLE_NAME
                    + " (" + SharedInformation.Messages.MESSAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + SharedInformation.Messages.MESSAGES_USER + " VARCHAR(20),"
                    + SharedInformation.Messages.MESSAGES_DATE + " VARCHAR(20),"
                    + SharedInformation.Messages.MESSAGES_TEXT + " VARCHAR(255)"
                    + ");");
        }

        // Cette méthode sert à gérer la montée de version de notre base
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + AndroidProvider.CONTENT_PROVIDER_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return AndroidProvider.CONTENT_PROVIDER_MIME;
    }

    private long getId(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment != null) {
            try {
                return Long.parseLong(lastPathSegment);
            } catch (NumberFormatException e) {
                Log.e("TutosAndroidProvider", "Number Format Exception : " + e);
            }
        }
        return -1;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME, null, values);

            if (id == -1) {
                throw new RuntimeException(String.format(
                        "%s : Failed to insert [%s] for unknown reasons.","AndroidProvider", values, uri));
            } else {
                return ContentUris.withAppendedId(uri, id);
            }

        } finally {
            db.close();
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            if (id < 0)
                return db.update(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME,values, selection, selectionArgs);
            else
                return db.update(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME,
                        values, SharedInformation.Messages.MESSAGES_ID + "=" + id, null);
        } finally {
            db.close();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (id < 0)
                return db.delete(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME,
                        selection, selectionArgs);
            else
                return db.delete(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME,
                        SharedInformation.Messages.MESSAGES_ID + "=" + id, selectionArgs);
        } finally {
            db.close();
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (id < 0) {
            return  db.query(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME,
                    projection, selection, selectionArgs, null, null,
                    sortOrder);
        } else {
            return      db.query(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME,
                    projection, SharedInformation.Messages.MESSAGES_ID + "=" + id, null, null, null,
                    null);
        }
    }

    public void clearTable(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AndroidProvider.CONTENT_PROVIDER_TABLE_NAME, null,null);
    }
}
