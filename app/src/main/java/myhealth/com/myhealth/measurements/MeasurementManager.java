package myhealth.com.myhealth.measurements;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.VolleyError;

import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myhealth.com.myhealth.api.API;
import myhealth.com.myhealth.api.APIInterface;

/**
 * Created by Sander on 27-9-2015.
 * Manage incoming measurements. Store them locally and send them to the webservice.
 */
public class MeasurementManager extends SQLiteOpenHelper implements APIInterface {
    // Database information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Measurement.db";

    // Table creation parts
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // Table create statements
    private static final String SQL_CREATE_ECG =
            "CREATE TABLE " + MeasurementContract.ECG.TABLE_NAME + " (" +
                    MeasurementContract.ECG._ID + " INTEGER PRIMARY KEY," +
                    MeasurementContract.ECG.COLUMN_NAME_ONLINE_ID + INTEGER_TYPE + COMMA_SEP +
                    MeasurementContract.ECG.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    MeasurementContract.ECG.COLUMN_NAME_VALUES + TEXT_TYPE + COMMA_SEP +
                    MeasurementContract.ECG.COLUMN_NAME_DATE + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_BP =
            "CREATE TABLE " + MeasurementContract.BP.TABLE_NAME + " (" +
                    MeasurementContract.BP._ID + " INTEGER PRIMARY KEY," +
                    MeasurementContract.BP.COLUMN_NAME_ONLINE_ID + INTEGER_TYPE + COMMA_SEP +
                    MeasurementContract.BP.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    MeasurementContract.BP.COLUMN_NAME_VALUES + TEXT_TYPE + COMMA_SEP +
                    MeasurementContract.BP.COLUMN_NAME_DATE + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_BPM =
            "CREATE TABLE " + MeasurementContract.BPM.TABLE_NAME + " (" +
                    MeasurementContract.BPM._ID + " INTEGER PRIMARY KEY," +
                    MeasurementContract.BPM.COLUMN_NAME_ONLINE_ID + INTEGER_TYPE + COMMA_SEP +
                    MeasurementContract.BPM.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    MeasurementContract.BPM.COLUMN_NAME_BPM + INTEGER_TYPE + COMMA_SEP +
                    MeasurementContract.BPM.COLUMN_NAME_DATE + TEXT_TYPE +
                    " )";

    // Table delete statements
    private static final String SQL_DELETE_ECG =
            "DROP TABLE IF EXISTS " + MeasurementContract.ECG.TABLE_NAME;
    private static final String SQL_DELETE_BP =
            "DROP TABLE IF EXISTS " + MeasurementContract.BP.TABLE_NAME;
    private static final String SQL_DELETE_BPM =
            "DROP TABLE IF EXISTS " + MeasurementContract.BPM.TABLE_NAME;

    // The current id for the measurement
    private int currentId;
    // The context
    private Context context;
    // The activity
    private AppCompatActivity activity;
    // The API
    private API api;
    // The measurements to locally update after a response
    private Map<Integer, JSONObject> measurements;


    /**
     * Constructor to initialize a MeasurementManager object
     *
     * @param context  The context this was made in
     * @param activity The activity this belongs to
     */
    public MeasurementManager(Context context, AppCompatActivity activity) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.activity = activity;
        currentId = 0;
        api = new API();
        measurements = new HashMap<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ECG);
        db.execSQL(SQL_CREATE_BPM);
        db.execSQL(SQL_CREATE_BP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This should probably do something. I think.
    }

    /**
     * Save measurements
     *
     * @param json The list of json measurements
     */
    public synchronized void saveMeasurements(List<String> json) {
        String userId = getUserId();
        if (userId == null) return;
        try {
            for (String e : json) {
                JSONObject jsObject = new JSONObject(e);
                if (jsObject.getString("type").equals("ecg")) {
                    handleDataSaving(MeasurementContract.ECG.COLUMN_NAME_USER_ID,
                            MeasurementContract.ECG.COLUMN_NAME_VALUES,
                            MeasurementContract.ECG.COLUMN_NAME_DATE,
                            MeasurementContract.ECG.TABLE_NAME,
                            API.ECG_POST, jsObject, userId);
                } else if (jsObject.getString("type").equals("bpm")) {
                    handleDataSaving(MeasurementContract.BPM.COLUMN_NAME_USER_ID,
                            MeasurementContract.BPM.COLUMN_NAME_BPM,
                            MeasurementContract.BPM.COLUMN_NAME_DATE,
                            MeasurementContract.BPM.TABLE_NAME,
                            API.PULSE_POST, jsObject, userId);
                } else if (jsObject.getString("type").equals("bp")) {
                    handleDataSaving(MeasurementContract.BP.COLUMN_NAME_USER_ID,
                            MeasurementContract.BP.COLUMN_NAME_VALUES,
                            MeasurementContract.BP.COLUMN_NAME_DATE,
                            MeasurementContract.BP.TABLE_NAME,
                            API.BLOODPRESSURE_POST, jsObject, userId);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the data
     *
     * @param userIdColumn The column of the user id
     * @param valuesColumn The column to save the values
     * @param dateColumn   The column to save the date
     * @param tableName    The table
     * @param APICall      API call to make
     * @param data         The data
     * @param userId       The user ID
     * @throws JSONException
     */
    private synchronized void handleDataSaving(String userIdColumn, String valuesColumn, String dateColumn, String tableName, String APICall, JSONObject data, String userId) throws JSONException {
        // Insert data
        ContentValues values = new ContentValues();
        values.put(userIdColumn, userId);
        values.put(valuesColumn, data.getString("measurementValue"));
        values.put(dateColumn, data.getString("measurementDate"));
        long id = getWritableDatabase().insert(tableName, null, values);
        Log.d("MyHealth_Bluetooth", values.toString());
        data.put("saved_id", id);

        // Prepare request
        Map<String, String> parameters = new HashMap<>();
        parameters.put("local_id", "" + currentId);
        parameters.put("measurementValue", data.getString("measurementValue"));
        parameters.put("measurementDate", data.getString("measurementDate"));
        // Store data for later
        measurements.put(currentId, data);
        currentId++;
        // Send the request
        api.request(APICall, API.POST, this, activity, parameters, true);
    }

    /**
     * Get the current user ID
     *
     * @return The user ID, or null if no token is present
     */
    private String getUserId() {
        try {
            // Get the JWT
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String token = sharedPref.getString("jwt", null);
            JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
                    .setSkipAllValidators()
                    .setDisableRequireSignature()
                    .setSkipSignatureVerification()
                    .build();
            JwtContext jwtContext = firstPassJwtConsumer.process(token);
            return jwtContext.getJwtClaims().getClaimValue("sub").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsObject = new JSONObject(response).getJSONObject("result");
            JSONObject measurement = measurements.get(jsObject.getInt("local_id"));
            if (measurement.getString("type").equals("ecg")) {
                updateTableWithOnlineId(MeasurementContract.ECG.TABLE_NAME,
                        MeasurementContract.ECG.COLUMN_NAME_ONLINE_ID,
                        MeasurementContract.ECG._ID,
                        measurement.getInt("saved_id"), jsObject.getInt("online_id"));
            } else if (measurement.getString("type").equals("bpm")) {
                updateTableWithOnlineId(MeasurementContract.BPM.TABLE_NAME,
                        MeasurementContract.BPM.COLUMN_NAME_ONLINE_ID, MeasurementContract.BPM._ID,
                        measurement.getInt("saved_id"), jsObject.getInt("online_id"));
            } else if (measurement.getString("type").equals("bp")) {
                updateTableWithOnlineId(MeasurementContract.BP.TABLE_NAME,
                        MeasurementContract.BP.COLUMN_NAME_ONLINE_ID, MeasurementContract.BP._ID,
                        measurement.getInt("saved_id"), jsObject.getInt("online_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a measurement with the online id
     *
     * @param tableName      The name of the table
     * @param columnToUpdate The name of the column
     * @param columnToSearch The column to search
     * @param savedId        The id to search for
     * @param onlineId       The online id
     */
    private void updateTableWithOnlineId(String tableName, String columnToUpdate, String columnToSearch, int savedId, int onlineId) {
        ContentValues values = new ContentValues();
        values.put(columnToUpdate, onlineId);
        String selection = columnToSearch + " LIKE ? ";
        String[] selectionArgs = {String.valueOf(savedId)};
        getWritableDatabase().update(tableName, values, selection, selectionArgs);
    }

    @Override
    public void onErrorResponse(VolleyError errorResponse) {
        Log.d("MyHealth", "Error :( " + errorResponse.getMessage());
    }
}
