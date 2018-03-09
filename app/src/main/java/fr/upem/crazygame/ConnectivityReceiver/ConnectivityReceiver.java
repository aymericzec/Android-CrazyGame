package fr.upem.crazygame.ConnectivityReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import fr.upem.crazygame.R;

/**
 * Created by dagama on 09/03/18.
 */

public class ConnectivityReceiver extends BroadcastReceiver{
    IntentFilter intentFilter;
    BroadcastReceiver br;

    public IntentFilter getIntentFilter(){
        intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        NetworkInfo info = (NetworkInfo) extras.getParcelable("networkInfo");
        NetworkInfo.State state = info.getState();

        boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);

        if(isAirplaneModeOn){
            Toast.makeText(context, context.getString(R.string.cantPlay), Toast.LENGTH_LONG).show();
        }
        if (state != NetworkInfo.State.CONNECTED) {
            Toast.makeText(context, context.getString(R.string.cantPlay), Toast.LENGTH_LONG).show();
        };
    }
}
