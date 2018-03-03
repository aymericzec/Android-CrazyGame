package fr.upem.crazygame.provider;

import android.content.ContentValues;
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

            //Initialise les lignes
            ContentValues contentValuesMorpion = new ContentValues();
            contentValuesMorpion.put(GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.NAME_MORPION);
            contentValuesMorpion.put(GameCrazyGameColumns.GAME, 0);
            contentValuesMorpion.put(GameCrazyGameColumns.GAME_WIN, 0);
            contentValuesMorpion.put(GameCrazyGameColumns.GAME_LAST_PLAY, 0);

            db.insert(ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME, null, contentValuesMorpion);

            ContentValues contentValuesMixWord = new ContentValues();
            contentValuesMixWord.put(GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.NAME_MIXWORD);
            contentValuesMixWord.put(GameCrazyGameColumns.GAME, 0);
            contentValuesMixWord.put(GameCrazyGameColumns.GAME_WIN, 0);
            contentValuesMixWord.put(GameCrazyGameColumns.GAME_LAST_PLAY, 0);

            db.insert(ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME, null, contentValuesMixWord);
        }

        // Cette méthode sert à gérer la montée de version de notre base
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ProviderDataGame.CONTENT_PROVIDER_TABLE_NAME);
            onCreate(db);
        }
    }