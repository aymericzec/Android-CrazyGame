package fr.upem.crazygame.service.statistical.broadCastReceiverBoot;
<<<<<<< HEAD

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.upem.crazygame.service.statistical.ServiceStatistical;

public class BroadCastBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ServiceStatistical.class);
        context.startService(i);
=======
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

        import fr.upem.crazygame.service.statistical.ServiceStatistical;

/**
 * Created by myfou on 03/03/2018.
 */

public class BroadCastBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent monServiceIntent = new Intent(context, ServiceStatistical.class);
        context.startService(monServiceIntent);
>>>>>>> 18c0a0da8d47c175ac05513e6e3c1d697c241762
    }
}
