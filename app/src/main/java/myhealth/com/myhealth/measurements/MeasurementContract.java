package myhealth.com.myhealth.measurements;

import android.provider.BaseColumns;

/**
 * Created by Sander on 27-9-2015.
 * A 'contract' to save information about the database
 */
public class MeasurementContract {

    public static class ECG implements BaseColumns {
        public static final String TABLE_NAME = "ecg";
        public static final String COLUMN_NAME_ONLINE_ID = "online_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_VALUES = "ecg_values";
        public static final String COLUMN_NAME_DATE = "date";
    }

    public static class BP implements BaseColumns {
        public static final String TABLE_NAME = "bp";
        public static final String COLUMN_NAME_ONLINE_ID = "online_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_VALUES = "bp_values";
        public static final String COLUMN_NAME_DATE = "date";
    }

    public static class BPM implements BaseColumns {
        public static final String TABLE_NAME = "bpm";
        public static final String COLUMN_NAME_ONLINE_ID = "online_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_BPM = "bpm";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
