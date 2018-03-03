package fr.upem.crazygame.service.statistical;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;

import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;
import fr.upem.crazygame.searchgameactivity.SocketHandler;


public class ServiceStatistical extends Service {

    private boolean isExecuting = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        thread.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(thread != null) {
            thread.isInterrupted();
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    public SocketChannel initSocketChanel() throws IOException {

        ByteBuffer in = ByteBuffer.allocate(2048);

        try (SocketChannel sc = SocketChannel.open()) {
            ByteBuffer buffer = CharsetServer.CHARSET_UTF_8.encode(Locale.getDefault().getLanguage());
            in.putInt(buffer.limit());
            in.put(buffer);
            in.putInt(4200);
            in.flip();
            sc.write(in);
            return sc;
        }
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            isExecuting = true;
            SocketChannel sc;

            try {
                sc = initSocketChanel();
                String [] columns = {GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.GAME_LAST_PLAY};
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                do {
                    try {
                        Thread.sleep(10000);
                        Cursor cursor = getContentResolver().query(ProviderDataGame.CONTENT_URI, columns, null, null, null);

                        if (cursor.moveToFirst()) {
                            do {
                                String nameGame = cursor.getString(cursor.getColumnIndex(GameCrazyGameColumns.NAME_GAME));
                                int game = cursor.getInt(cursor.getColumnIndex(GameCrazyGameColumns.GAME_LAST_PLAY));
                                ByteBuffer b = CharsetServer.CHARSET_UTF_8.encode(nameGame);
                                byteBuffer.putInt(b.limit());
                                byteBuffer.put(b);
                                byteBuffer.putInt(game);
                            } while (cursor.moveToNext());

                            byteBuffer.flip();
                            sc.write(byteBuffer);
                            byteBuffer.clear();

                            ContentValues mUpdateValues = new ContentValues();
                            mUpdateValues.put(GameCrazyGameColumns.GAME_LAST_PLAY, 0);
                            getContentResolver().update(ProviderDataGame.CONTENT_URI, mUpdateValues, null, null);
                        }

                    } catch (InterruptedException e) {
                        isExecuting = false;
                    }
                }while(isExecuting);
            } catch (IOException e) {
                onDestroy();
            }
        }
    });
}
