package fr.upem.crazygame.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.upem.crazygame.R;

/**
 * Created by dagama on 21/02/18.
 */

public class CustomListChat extends ArrayAdapter<String> {

        private final Activity context;
        private final List<String> messages;
        private final List<String> users;
        private final List<String> dates;

        public CustomListChat(Activity context, List<String> messages, List<String> users, List<String> dates) {
            super(context, R.layout.row_layout_chat, messages);

            this.context=context;
            this.messages=messages;
            this.users=users;
            this.dates=dates;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.row_layout_chat, null,true);

            /*TextView user = (TextView) rowView.findViewById(R.id.user);
            user.setText(users.get(position));

            TextView date = (TextView) rowView.findViewById(R.id.date);
            date.setText(dates.get(position));

            TextView message = (TextView) rowView.findViewById(R.id.message);
            message.setText(messages.get(position));*/

            return rowView;
        };
    }