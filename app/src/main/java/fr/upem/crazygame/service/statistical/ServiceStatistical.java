package fr.upem.crazygame.service.statistical;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;

import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;


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

    public void initSocketChanel() throws IOException {

        ByteBuffer in = ByteBuffer.allocate(2048);

        try (SocketChannel sc = SocketChannel.open()) {
            ByteBuffer buffer = CharsetServer.CHARSET_UTF_8.encode(Locale.getDefault().getLanguage());
            in.putInt(buffer.limit());
            in.put(buffer);
            in.putInt(4200);
            in.flip();
            sc.write(in);
        }
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            isExecuting = true;

            try {
                initSocketChanel();
            } catch (IOException e) {
                onDestroy();
            }

            ProviderDataGame providerDataGame = new ProviderDataGame();
            String [] columns = {GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.GAME_LAST_PLAY};

            do {
                try {
                    Thread.sleep(10000);
                    Cursor cursor = providerDataGame.query(ProviderDataGame.CONTENT_URI, columns, null, null, null);

                    //Parser le curseur

                } catch (InterruptedException e) {
                    isExecuting = false;
                }

            }while(isExecuting);
        }
    });
}
