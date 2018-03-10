package fr.upem.crazygame.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;

import fr.upem.crazygame.charset.CharsetServer;


public class ProviderDataGame extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://fr.upem.crazygame.provider.crazygameprovider");
    public static final String CONTENT_PROVIDER_DB_NAME = "crazygame.db";
    public static final int CONTENT_PROVIDER_DB_VERSION = 1;
    public static final String CONTENT_PROVIDER_TABLE_NAME = "statisticalDB";
    public static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.item/vnd.upem.crazygame.provider.crazygameprovider";

    public DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        this.dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (id < 0) {
            return  db.query(ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME,
                    columns, selection, selectionArgs, null, null,
                    sortOrder);
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return ProviderDataGame.CONTENT_PROVIDER_MIME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME, null, contentValues);

            if (id == -1) {
                throw new RuntimeException(String.format(
                        "%s : Failed to insert [%s] for unknown reasons.","AndroidProvider", contentValues, uri));
            } else {
                return ContentUris.withAppendedId(uri, id);
            }

        } finally {
            db.close();
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
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
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            if (id < 0)
                return db.update(ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME, values, selection, selectionArgs);
            else
                return db.update(ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME,
                        values, GameCrazyGameColumns.NAME_GAME + "=" + id, null);
        } finally {
            db.close();
        }
    }

    public static void addGame (String nameGame, Context context) {
        String [] columns = {GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.GAME, GameCrazyGameColumns.GAME_LAST_PLAY};

        String selection = GameCrazyGameColumns.NAME_GAME + " = ?";
        String [] selectionArgs = {nameGame};
        Cursor cursor = context.getContentResolver().query(ProviderDataGame.CONTENT_URI, columns, selection, selectionArgs, null);

        if (cursor.moveToFirst()) {
            int value = cursor.getInt(cursor.getColumnIndex(GameCrazyGameColumns.GAME));
            ContentValues mUpdateValues = new ContentValues();
            mUpdateValues.put(GameCrazyGameColumns.GAME, value + 1);
            String args[] = {nameGame};
            context.getContentResolver().update(ProviderDataGame.CONTENT_URI, mUpdateValues, GameCrazyGameColumns.NAME_GAME + " = ?", args);

            int valueService = cursor.getInt(cursor.getColumnIndex(GameCrazyGameColumns.GAME_LAST_PLAY));
            ContentValues mUpdateValuesLastPlay = new ContentValues();
            mUpdateValuesLastPlay.put(GameCrazyGameColumns.GAME_LAST_PLAY, valueService + 1);
            context.getContentResolver().update(ProviderDataGame.CONTENT_URI, mUpdateValues, GameCrazyGameColumns.NAME_GAME + " = ?", args);
        }
    }

    public static void addWinGame (String nameGame, Context context) {
        String [] columns = {GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.GAME_WIN};

        String selection = GameCrazyGameColumns.NAME_GAME + " = ?";
        String [] selectionArgs = {nameGame};
        Cursor cursor = context.getContentResolver().query(ProviderDataGame.CONTENT_URI, columns, selection, selectionArgs, null);

        if (cursor.moveToFirst()) {
            int value = cursor.getInt(cursor.getColumnIndex(GameCrazyGameColumns.GAME_WIN));
            ContentValues mUpdateValues = new ContentValues();
            mUpdateValues.put(GameCrazyGameColumns.GAME_WIN, value + 1);
            String args[] = {nameGame};
            context.getContentResolver().update(ProviderDataGame.CONTENT_URI, mUpdateValues, GameCrazyGameColumns.NAME_GAME + " = ?", args);
        }
    }
}
