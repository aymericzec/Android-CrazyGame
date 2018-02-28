package fr.upem.crazygame.chat;

import android.provider.BaseColumns;

/**
 * Created by dagama on 20/02/18.
 */

public class SharedInformation {

    public SharedInformation() {
    }

    public static final class Messages implements BaseColumns {
        private Messages() {}
        public static final String MESSAGES_ID = "MESSAGES";
        public static final String MESSAGES_TEXT = "MESSAGES_TEXT";
        public static final String MESSAGES_USER = "MESSAGES_USER";
        public static final String MESSAGES_DATE = "MESSAGES_DATE";
    }
}
