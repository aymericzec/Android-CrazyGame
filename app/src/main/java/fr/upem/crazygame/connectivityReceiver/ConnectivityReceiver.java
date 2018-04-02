package fr.upem.crazygame.connectivityReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

/**
 * Permet de se déclencher lorsqu'il n'y a plus de wifi ou de donnée d'itinérance
 */

public class ConnectivityReceiver extends BroadcastReceiver{

    private IntentFilter intentFilter;

    public IntentFilter getIntentFilter(){
        intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return intentFilter;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Bundle extras = intent.getExtras();
        NetworkInfo info = (NetworkInfo) extras.getParcelable("networkInfo");
        NetworkInfo.State state = info.getState();

        if (state != NetworkInfo.State.CONNECTED) {
            Intent i = new Intent(context.getApplicationContext(), AlertDialogInternet.class);
            context.startActivity(i);
        }
    }
}
