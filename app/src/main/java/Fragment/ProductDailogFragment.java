package Fragment;


import android.os.Bundle;
import android.util.Log;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Home_Child_Adapter;
import Config.BaseURL;
import Model.Home_Child_model;
import Model.ProductDailog;
import util.CustomVolleyJsonRequest;
import youngershopping.tcc.AppController;
import youngershopping.tcc.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDailogFragment extends Fragment {


    public ProductDailogFragment() {
        // Required empty public constructor
    }

    String product_id;

    public static final String TAG = "ProductDailogFragment";
    ProductDailog productDailog = new ProductDailog();
    TextView tvItemName,tvRepees,tvMrp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product_details, container, false);

        if (getArguments().getString("product_id") != null) {
            product_id = getArguments().getString("product_id");
        }


        intit(view);

        getData();

        return view;
    }

    private void intit(View view) {
        tvItemName = view.findViewById(R.id.tvItemName);
        tvRepees = view.findViewById(R.id.tvRepees);
        tvMrp = view.findViewById(R.id.tvMrp);
    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("product_id", product_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.POST_PRODUCT_DETAILS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    //  Boolean status = response.getBoolean("status");

                    if (response != null && response.length() > 0) {
                        Log.d(TAG, "ghansham" + response.toString());
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            JSONObject productDailog = response;
                            Log.i(TAG, "onResponse:cccc " + productDailog);

                            JSONArray jsonArray = response.getJSONArray("data");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                tvItemName.setText(jsonObject.optString("product_name"));
                                tvRepees.setText(jsonObject.optString("price"));
                                //tvMrp.setText(jsonObject.optString("mrp"));
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
