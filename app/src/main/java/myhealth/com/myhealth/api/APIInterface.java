package myhealth.com.myhealth.api;

import com.android.volley.VolleyError;

/**
 * Interface that contains the methods necessary to perform an API request
 */
public interface APIInterface {

    void onResponse(String response);

    void onErrorResponse(VolleyError errorResponse);

}
