package Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.Cart_adapter;
import Config.BaseURL;
import youngershopping.tcc.AppController;
import youngershopping.tcc.LoginActivity;
import youngershopping.tcc.MainActivity;
import youngershopping.tcc.R;
import util.ConnectivityReceiver;
import util.DatabaseHandler;
import util.Session_management;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Cart_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Cart_fragment.class.getSimpleName();

    private RecyclerView rv_cart;
    private TextView tv_clear, tv_total, tv_item;
    private RelativeLayout btn_checkout;

    private DatabaseHandler db;

    private Session_management sessionManagement;

    public Cart_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.cart));

        sessionManagement = new Session_management(getActivity());
        sessionManagement.cleardatetime();


        tv_clear = (TextView) view.findViewById(R.id.tv_cart_clear);
        tv_total = (TextView) view.findViewById(R.id.tv_cart_total);
        tv_item = (TextView) view.findViewById(R.id.tv_cart_item);
        btn_checkout = (RelativeLayout) view.findViewById(R.id.btn_cart_checkout);
        rv_cart = (RecyclerView) view.findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = new DatabaseHandler(getActivity());

        ArrayList<HashMap<String, String>> map = db.getCartAll();

        Cart_adapter adapter = new Cart_adapter(getActivity(), map);
        rv_cart.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateData();

        tv_clear.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_cart_clear) {
            // showdialog
            showClearDialog();
        } else if (id == R.id.btn_cart_checkout) {

            if (ConnectivityReceiver.isConnected()) {
                makeGetLimiteRequest();
            } else {
                ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
            }

        }
    }

    // update UI
    private void updateData() {
        tv_total.setText("" + db.getTotalAmount());
        tv_item.setText("" + db.getCartCount());
        ((MainActivity) getActivity()).setCartCounter("" + db.getCartCount());
    }

    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(getResources().getString(R.string.sure_del));
        alertDialog.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                db.clearCart();
                ArrayList<HashMap<String, String>> map = db.getCartAll();
                Cart_adapter adapter = new Cart_adapter(getActivity(), map);
                rv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                updateData();

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetLimiteRequest() {


                        Double total_amount = Double.parseDouble(db.getTotalAmount());
        Bundle args = new Bundle();
        Fragment fm = new Delivery_fragment();
        fm.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                .addToBackStack(null).commit();



    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };


}
