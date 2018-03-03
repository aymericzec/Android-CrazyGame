package fr.upem.crazygame.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Permet de créer notre base de donnée
 */
public class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, ProviderDataGame.CONTENT_PROVIDER_DB_NAME, null, ProviderDataGame.CONTENT_PROVIDER_DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME
                    + " (" + GameCrazyGameColumns.NAME_GAME + ","
                    + GameCrazyGameColumns.GAME + " INTEGER,"
                    + GameCrazyGameColumns.GAME_WIN + " INTEGER,"
                    + GameCrazyGameColumns.GAME_LAST_PLAY + " INTEGER"
                    + ");");
        }

        // Cette méthode sert à gérer la montée de version de notre base
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME);
            onCreate(db);
        }
    }