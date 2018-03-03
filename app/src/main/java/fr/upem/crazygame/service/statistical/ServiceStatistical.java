package fr.upem.crazygame.service.statistical;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;

import fr.upem.crazygame.charset.CharsetServer;


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

    public void sendStatistics() throws IOException {

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
                sendStatistics();
            } catch (IOException e) {
                onDestroy();
            }

            do {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    isExecuting = false;
                }

            }while(isExecuting);
        }
    });
}
