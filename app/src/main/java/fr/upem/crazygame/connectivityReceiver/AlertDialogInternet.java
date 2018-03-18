package fr.upem.crazygame.connectivityReceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import fr.upem.crazygame.R;
import fr.upem.crazygame.searchgameactivity.SearchGameActivity;


public class AlertDialogInternet extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setMessage(R.string.cantPlay);

        dialog.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AlertDialogInternet.this, SearchGameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.create();
        dialog.show();
    }
}
