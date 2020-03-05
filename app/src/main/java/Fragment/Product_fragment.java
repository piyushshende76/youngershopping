package Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Home_Child_Adapter;

import Adapter.Product_adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Home_Child_model;

import Model.Product_model;
import Model.Slider_subcat_model;
import util.RecyclerTouchListener;
import util.ReplaceFragment;
import youngershopping.tcc.AppController;
import youngershopping.tcc.CustomSlider;
import youngershopping.tcc.MainActivity;
import youngershopping.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Product_fragment extends Fragment {
    private static String TAG = "Product_fragment";
    private RecyclerView rv_cat, main_cat;
    private TabLayout tab_cat;
    private List<Category_model> category_modelList = new ArrayList<>();

    private List<Slider_subcat_model> slider_subcat_models = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();
    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;
    private SliderLayout banner_slider;
    String language;
    SharedPreferences preferences;
    String getcid, fed;
    String strtext;

    private Home_Child_Adapter menu_adapter;
    private List<Home_Child_model> menu_models = new ArrayList<>();
    ArrayList<Product_model.SubCat> subCats = new ArrayList<>();

    public Product_fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        Log.i(TAG, "onCreateView: ");

//sub cat
        tab_cat = (TabLayout) view.findViewById(R.id.tab_cat);
        main_cat = (RecyclerView) view.findViewById(R.id.main_cat);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        main_cat.setLayoutManager(layoutManager);
        main_cat.setHasFixedSize(true);
        main_cat.setItemViewCacheSize(10);
        main_cat.setDrawingCacheEnabled(true);
        main_cat.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        rv_cat = (RecyclerView) view.findViewById(R.id.rv_subcategory);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 2);
        rv_cat.setLayoutManager(gridLayoutManager3);
        rv_cat.setItemAnimator(new DefaultItemAnimator());
        rv_cat.setNestedScrollingEnabled(false);


        final String getcat_id = getArguments().getString("cat_id");
        String id = getArguments().getString("id");
        String get_deal_id = getArguments().getString("cat_deal");
        String get_top_sale_id = getArguments().getString("cat_top_selling");
        String getcat_title = getArguments().getString("cat_title");
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.tv_product_name));

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {

            Log.i(TAG, "onCreateView: ");
            //Shop by Catogary
            makeGetCategoryRequest(getcat_id);

            //Deal Of The Day Products
            makedealIconProductRequest(get_deal_id);
            //Top Sale Products
            maketopsaleProductRequest(get_top_sale_id);
            makeGetSliderCategoryRequest(id);

            //Slider
            makeGetBannerSliderRequest();

        }
        // main_cat.setVisibility(view.GONE);

        tab_cat.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.white));


        tab_cat.setVisibility(View.GONE);
        tab_cat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Bundle args = new Bundle();
                Log.i(TAG, "onTabSelected: " + tab.getTag());

                //   args.putString("child_id", (String) tab.getTag());

                //     String getcat_id = cat_menu_id.get(tab.getPosition());
                if (ConnectivityReceiver.isConnected()) {
                    //           makeGetProductRequest(getcat_id);
                    makeGetsubCategoryRequest((String) tab.getTag());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        main_cat.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), main_cat, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                fed = menu_models.get(position).getProduct_id();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", fed);
                fm.setArguments(args);
                Log.i(TAG, "onItemClick: " + fed);
                makeGetProductRequest(fed);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        rv_cat.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                rv_cat,
                new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ProductDailogFragment ldf = new ProductDailogFragment();
                        Bundle args = new Bundle();
                        args.putString("product_id", product_modelList.get(position).getProduct_id());
                        args.putInt("position", position);
                        args.putSerializable("product_modelList", (Serializable) product_modelList);
                        ldf.setArguments(args);
                        getFragmentManager().beginTransaction().replace(
                                R.id.contentPanel,
                                ldf
                        ).commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        return view;
    }

    private void makeGetsubCategoryRequest(String getcat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sub_id", getcat_id);
        Log.i(TAG, "ree: " + getcat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CHILD_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    //  Boolean status = response.getBoolean("status");

                    if (response != null && response.length() > 0) {
                        Log.d(TAG, "ghansham" + response.toString());
                        Boolean status = response.getBoolean("status");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Child_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("child_category"), listType);
                            menu_adapter = new Home_Child_Adapter(menu_models);
                            main_cat.setAdapter(menu_adapter);
                            menu_adapter.notifyDataSetChanged();
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    /**
     * Method to make json object request where json response starts wtih
     */
    //Get Shop By Catogary
    private void makeGetCategoryRequest(final String parent_id) {


        makeGetProductRequest(parent_id);


        strtext = getArguments().getString("cat_id");

        Log.i(TAG, "makeGetCategoryRequest: " + BaseURL.GET_SCATEGORY_URL);


        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", strtext);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_SCATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "rahul: " + response);

                try {
                    Boolean status = response.getBoolean("status");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Category_model>>() {
                        }.getType();
                        category_modelList = gson.fromJson(response.getString("sub_category"), listType);
                        if (!category_modelList.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);
                            cat_menu_id.clear();
                            for (int i = 0; i < category_modelList.size(); i++) {
                                cat_menu_id.add(category_modelList.get(i).getId());
                                preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

                                language = preferences.getString("language", "");
                                if (language.contains("english")) {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getTitle()).setTag(category_modelList.get(i).getId()));
                                } else {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getArb_title()));

                                }
                            }
                        } else {

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    //Get Shop By Catogary Products
    private void makeGetProductRequest(String fed) {
        Log.i(TAG, "makeGetProductRequest: ");
        String tag_json_obj = "json_product_req";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("child_id", fed);
        Log.i(TAG, "makeGetProductRequest: " + fed);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("AAAAAAAAA", response.toString());

                try {
                    Boolean status = response.getBoolean("status");
                    if (status) {
                        product_modelList.clear();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        // product_modelList = gson.fromJson(response.getString("products"), listType);

                        //Log.i(TAG, "SAI RAM: "+product_modelList.get(3).getSubCat().get(1).getSizes());


                        JSONArray jsonArrayProducts = response.getJSONArray("products");

                        Log.i(TAG, "ASSASAA: " + jsonArrayProducts.length());
                        Product_model product_model = new Product_model();
                        for (int i = 0; i < jsonArrayProducts.length(); i++) {
                            JSONObject jsonObject = jsonArrayProducts.getJSONObject(i);

                            Log.i(TAG, "onResponse:ccc " + jsonObject.getString("product_name"));

                            Product_model product_model1 = new Product_model();
                            product_model1.setProduct_id(jsonObject.getString("product_id"));
                            product_model1.setProduct_name(jsonObject.getString("product_name"));
                            product_model1.setCategory_id(jsonObject.getString("category_id"));
                            product_model1.setPrice(jsonObject.getString("price"));
                            product_model1.setProduct_image(jsonObject.getString("product_image"));
                            product_model1.setStock(jsonObject.getString("stock"));
                            product_model1.setProduct_description(jsonObject.getString("product_description"));

                            JSONArray jsonArray = jsonObject.getJSONArray("sub_cat");
                            if (jsonArray.length() > 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject obj = jsonArray.getJSONObject(j);
                                    Product_model.SubCat sub = new Product_model.SubCat(
                                            obj.getString("sizes"),
                                            obj.getString("color")
                                    );

                                    // Log.i(TAG, "onRespoe: " + obj.getString("color"));

                                    Product_model.SubCat subCat = new Product_model.SubCat(
                                            obj.optString("sizes"),
                                            obj.optString("color")
                                    );

                                    subCats.add(subCat);

                                }
                            }
                            product_model1.setSubCat(subCats);
                            product_modelList.add(product_model1);

                        }

                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        Log.i(TAG, "onResponse:393 " + product_modelList.size());

                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    //Get Shop By Catogary
    private void makeGetSliderCategoryRequest(final String sub_cat_id) {
        makeGetCategoryRequest(sub_cat_id);
        Log.i(TAG, "makeGetSliderCategoryRequest: ");
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sub_cat", sub_cat_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_SLIDER_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("status");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Slider_subcat_model>>() {
                        }.getType();
                        slider_subcat_models = gson.fromJson(response.getString("sub_category"), listType);
                        if (!slider_subcat_models.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);
                            cat_menu_id.clear();
                            for (int i = 0; i < slider_subcat_models.size(); i++) {
                                cat_menu_id.add(slider_subcat_models.get(i).getId());
                                preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

                                language = preferences.getString("language", "");
                                if (language.contains("english")) {
                                    tab_cat.addTab(tab_cat.newTab().setText(slider_subcat_models.get(i).getTitle()));
                                } else {
                                    tab_cat.addTab(tab_cat.newTab().setText(slider_subcat_models.get(i).getArb_title()));

                                }
                            }
                        } else {
                            // makeGetProductRequest(sub_cat_id);
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    ////Get DEal Products
    private void makedealIconProductRequest(String cat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("dealproduct", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_DEAL_OF_DAY_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("Deal_of_the_day"), listType);
                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        Log.i(TAG, "onResponse:500 ");
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    ////Get Top Sale Products
    private void maketopsaleProductRequest(String cat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("top_selling_product", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("top_selling_product"), listType);
                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        Log.i(TAG, "onResponse:551 ");
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void makeGetBannerSliderRequest() {
        Log.i(TAG, "makeGetBannerSliderRequest: ");
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_BANNER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                //  url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                //  textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                banner_slider.addSlider(textSliderView);
                                // final String sub_cat = (String) textSliderView.getBundle().get("extra");
//                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
//                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                        Bundle args = new Bundle();
//                                        Fragment fm = new Product_fragment();
//                                        args.putString("id", sub_cat);
//                                        fm.setArguments(args);
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                .addToBackStack(null).commit();
//                                    }
//                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(req);
    }
}