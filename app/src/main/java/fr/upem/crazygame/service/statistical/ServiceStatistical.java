package fr.upem.crazygame.service.statistical;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;

import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;
import fr.upem.crazygame.searchgameactivity.SearchGameSocketManager;


public class ServiceStatistical extends Service {

    private static boolean isExecuting = false;
    private static int test = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (!isExecuting) {
            Toast.makeText(this, "Service started " + test++, Toast.LENGTH_LONG).show();
            thread.start();
            isExecuting = true;
            super.onCreate();
        }
    }

    @Override
    public void onDestroy() {
        if(thread != null) {
            thread.isInterrupted();
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        isExecuting = false;
        super.onDestroy();
    }

    public SocketChannel initSocketChanel() throws IOException {

        final SocketChannel sc = SocketChannel.open();
        InetSocketAddress serverAddress =  new InetSocketAddress("90.3.251.211", 1002);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final InetSocketAddress serverAddressTmp = serverAddress;
        sc.connect(serverAddressTmp);

        ByteBuffer in = ByteBuffer.allocate(2048);
        ByteBuffer buffer = CharsetServer.CHARSET_UTF_8.encode(Locale.getDefault().getLanguage());
        in.putInt(buffer.limit());
        in.put(buffer);
        in.putInt(4200);
        in.flip();
        sc.write(in);
        return sc;
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            isExecuting = true;
            SocketChannel sc;
            try {
                sc = initSocketChanel();
//                Handler handler = new Handler(Looper.getMainLooper());
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getBaseContext(), "Un tour de boucle " + test, Toast.LENGTH_LONG).show();
//                    }
//                });

                String [] columns = {GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.GAME_LAST_PLAY};
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                do {
                    try {
                        Thread.sleep(10000);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getBaseContext(), "Un tour de boucle " + test, Toast.LENGTH_LONG).show();
//                            }
//                        });

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

                            byteBuffer.putInt(400);
                            byteBuffer.flip();
                            sc.write(byteBuffer);
                            byteBuffer.clear();

                            ContentValues mUpdateValues = new ContentValues();
                            mUpdateValues.put(GameCrazyGameColumns.GAME_LAST_PLAY, 0);
                            getContentResolver().update(ProviderDataGame.CONTENT_URI, mUpdateValues, null, null);
                        }

                    } catch (InterruptedException e) {
                        isExecuting = false;
                        onDestroy();
                        return;
                    }
                }while(isExecuting);
            } catch (IOException e) {
                onDestroy();
            }
        }
    });
}
