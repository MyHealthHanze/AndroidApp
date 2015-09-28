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

public class API {

    private static final String BASE_URL = "https://myhealth-hanze.herokuapp.com/api/v1/";

    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;

    public static final String USER_LOGIN_POST = "user/login";
    public static final String USER_PASSWORD_PUT = "user/password";

    public static final String ECG_GET = "measurement/ecg";
    public static final String ECG_POST = "measurement/ecg";

    public static final String BLOODPRESSURE_GET = "measurement/bloodpressure";
    public static final String BLOODPRESSURE_POST = "measurement/bloodpressure";

    public static final String PULSE_GET = "measurement/pulse";
    public static final String PULSE_POST = "measurement/pulse";

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
                        if (error.networkResponse != null) {
                            presenter.onErrorResponse(error);

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
                                Toast.makeText(activity, "You don't have an internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
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
                Map<String, String> params = new HashMap<>();
                if (authenticated) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
                    String token = prefs.getString("jwt", "");
                    params.put("Authorization", "Bearer " + token);
                }
                return params;
            }
        };
        Volley.newRequestQueue(activity).add(postRequest);
    }
}
