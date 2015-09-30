package myhealth.com.myhealth.api;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import myhealth.com.myhealth.R;

/**
 * API holds all of the API endpoints and can fire off API requests
 */
public class API {

    // HTTP request methods
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;
    // API endpoints
    public static final String USER_LOGIN_POST = "user/login";
    public static final String USER_PASSWORD_PUT = "user/password";
    public static final String ECG_GET = "measurement/ecg";
    public static final String ECG_POST = "measurement/ecg";
    public static final String BLOODPRESSURE_GET = "measurement/bloodpressure";
    public static final String BLOODPRESSURE_POST = "measurement/bloodpressure";
    public static final String PULSE_GET = "measurement/pulse";
    public static final String PULSE_POST = "measurement/pulse";
    // The base url of the API
    private static final String BASE_URL = "https://myhealth-hanze.herokuapp.com/api/v1/";

    /**
     * Can perform a HTTP request to the server
     *
     * @param endpoint
     * @param method
     * @param presenter
     * @param activity
     * @param parameters
     * @param authenticated
     */
    public void request(final String endpoint, final int method, final APIInterface presenter, final AppCompatActivity activity, final Map<String, String> parameters, final boolean authenticated) {

        StringRequest postRequest = new StringRequest(method, BASE_URL + endpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            presenter.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            presenter.onErrorResponse(error);

                            handleErrorWithToast(error, activity);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                if (parameters == null) {
                    Map<String, String> params = new HashMap<>();
                    return params;
                } else {
                    return parameters;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getTokenAndPutInHashMap(parameters, activity, authenticated);
            }
        };

        // Finally, add the request to the queue
        Volley.newRequestQueue(activity).add(postRequest);
    }

    /**
     * Default error handling that get the error message and display it in a Toast
     *
     * @param error
     * @param activity
     */
    private void handleErrorWithToast(VolleyError error, AppCompatActivity activity) {
        // Default error handling implementation
        try {
            // Get response body from the VolleyError object
            String responseBody = new String(error.networkResponse.data, "utf-8");
            // Get the JSON object
            JSONObject jsonObject = new JSONObject(responseBody);
            // Get the string from the "error" object
            String errorString = jsonObject.getString("error");
            // Create a Toast message
            Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            // Handle a malformed json response
            Log.e("JSON Exception", e.toString());
        } catch (UnsupportedEncodingException e) {
            // Handle an unsupported encoding
            Log.e("UnsupportedEncodingE", e.toString());
        } catch (NullPointerException e) {
            Toast.makeText(activity, activity.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get the JWT and puts in in a HashMap, or creates an empty HashMap if user is not authenticated
     *
     * @param parameters
     * @param activity
     * @param authenticated
     * @return
     */
    private Map<String, String> getTokenAndPutInHashMap(Map<String, String> parameters, AppCompatActivity activity, boolean authenticated) {
        if (authenticated) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
            String token = prefs.getString("jwt", "");
            parameters.put("Authorization", "Bearer " + token);
        } else {
            parameters = new HashMap<>();
        }
        return parameters;
    }
}
