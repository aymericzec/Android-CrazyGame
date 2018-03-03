package fr.upem.crazygame.service.statistical.broadCastReceiverBoot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.upem.crazygame.service.statistical.ServiceStatistical;

public class BroadCastBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ServiceStatistical.class);
        context.startService(i);
    }
}