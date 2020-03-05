package Fragment;


import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Adapter.Home_Size_Adapter;
import Adapter.Image_Adapter;
import Adapter.Product_adapter;
import Adapter.Size_Adapter;
import Config.BaseURL;

import Model.Home_Size_model;
import Model.Product_model;
import Model.Size_model;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.RecyclerTouchListener;
import youngershopping.tcc.AppController;
import youngershopping.tcc.MainActivity;
import youngershopping.tcc.R;


public class ProductDailogFragment extends Fragment {
    private RecyclerView size, gallery;

    public ProductDailogFragment() {
    }

    //private values

    String product_id;
    public static final String TAG = "ProductDailogFragment";
    TextView tvItemName, tvRepees, tvMrp, tvRating, tvDesc, ivMainItemPic;
    public ImageView iv_logo;
    ImageView iv_minus, iv_plus;
    public Button buy, addcart;
    TextView tv_contetiy;
    Context context;
    private List<Product_model> product_modelList2;

    String image;
    String title;
    String description;
    String detail;
    int position;
    String qty;
    private Product_adapter adapter_product;
   // private Size_Adapter size_adapter;
   // private Home_Size_Adapter size_adapter;
    private List<Home_Size_model> size_models = new ArrayList<>();


    private DatabaseHandler dbcart;
    ArrayList<Product_model.SubCat> subCats = new ArrayList<>();
    ArrayList<Product_model.GalModel> gallist = new ArrayList<>();

    ArrayList<Product_model.SubCat> subCat;
    Product_model product_model2;
    Size_Adapter sizeadpt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_details, container, false);
        dbcart = new DatabaseHandler(getActivity());


       // getData();

        tvItemName = view.findViewById(R.id.tvItemName);
        tvRepees = view.findViewById(R.id.tvRepees);
        tvMrp = view.findViewById(R.id.tvMrp);
        tvRating = view.findViewById(R.id.tvRating);
        tvDesc = view.findViewById(R.id.tvDesc);
        iv_logo = (ImageView) view.findViewById(R.id.ivMainItemPic);
        buy = (Button) view.findViewById(R.id.cvBuyNow);
        addcart = (Button)view.findViewById(R.id.cvAddToCart);
        tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
        iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
        iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);


        if (getArguments().getString("product_id") != null) {
            product_id = getArguments().getString("product_id");
            int position = getArguments().getInt("position");

            product_modelList2 = (List<Product_model>) getArguments().getSerializable(
                    "product_modelList");

           // Log.i(TAG, "sdddd: " + product_modelList2.get(position).getProduct_name());

        }

        final ArrayList<Size_model>[] size_models = new ArrayList[]{new ArrayList<>()};
        final String[] getid = new String[1];
        size = (RecyclerView) view.findViewById(R.id.size);
        gallery = (RecyclerView) view.findViewById(R.id.gallery);
//        LinearLayoutManager layoutManager76 = new LinearLayoutManager(getActivity()) {
//
//            @Override
//            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
//                    private static final float SPEED = 2000f;// Change this value (default=25f)
//
//                    @Override
//                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                        return SPEED / displayMetrics.densityDpi;
//                    }
//                };
//                smoothScroller.setTargetPosition(position);
//                startSmoothScroll(smoothScroller);
//            }
//        };
//        layoutManager76.setOrientation(LinearLayoutManager.HORIZONTAL);
//        size.setLayoutManager(layoutManager76);
//        size.setHasFixedSize(true);
//        size.setItemViewCacheSize(10);
//        size.setDrawingCacheEnabled(true);
//        size.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

//        gallery.setLayoutManager(layoutManager76);
//        gallery.setHasFixedSize(true);
//        gallery.setItemViewCacheSize(10);
//        gallery.setDrawingCacheEnabled(true);
//        gallery.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        //size.setAdapter(new Size_Adapter(subCats, getActivity()));
        //Recycler View Menu Products

        size = view.findViewById(R.id.size);
        // add a divider after each item for more clarity
        size.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        sizeadpt = new Size_Adapter(subCats, getActivity());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        size.setLayoutManager(horizontalLayoutManager);
        size.setAdapter(sizeadpt);
        size.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), size, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              //   getid = ((ArrayList[]) size_models).get(position).getProduct_id();
                Bundle args = new Bundle();
                //  args.putString("cat_id", getid);
                Log.i(TAG, "onItemClick: " + getid);
                //  makeGetProductRequest(getid);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(getActivity(), "minus", Toast.LENGTH_SHORT).show();
                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }
            }
        });
        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(getActivity(), "add", Toast.LENGTH_SHORT).show();
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;
                tv_contetiy.setText(String.valueOf(qty));
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextpage();

                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;
                tv_contetiy.setText(String.valueOf(qty));
            //    Log.i(TAG, "gee " + product_modelList2.get(position).getProduct_id());

                HashMap<String, String> map = new HashMap<>();

                map.put("product_id",  product_model2.getProduct_id());
                map.put("product_name",product_model2.getProduct_name());
                map.put("category_id", product_model2.getCategory_id());
                map.put("product_description", product_model2.getProduct_description());
                map.put("deal_price", product_model2.getDeal_price());
                map.put("start_date",product_model2.getStart_date());
                map.put("start_time",product_model2.getStart_time());
                map.put("end_date", product_model2.getEnd_date());
                map.put("end_time", product_model2.getEnd_time());
                map.put("price", product_model2.getPrice());
                map.put("product_image", product_model2.getProduct_image());
                map.put("status", product_model2.getStatus());
                map.put("in_stock", product_model2.getIn_stock());
                map.put("unit_value", "");
                map.put("unit", "");
                map.put("increament",product_model2.getIncreament());
                map.put("rewards", product_model2.getRewards());
                map.put("stock",product_model2.getStock());
                map.put("title", product_model2.getTitle());

                 dbcart.setCart(map, Float.parseFloat(tv_contetiy.getText().toString()));
                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));
                ((MainActivity) getActivity()).setCartCounter(""+dbcart.getCartCount());


            }
        });
       // Toast.makeText(getActivity(), "infrag", Toast.LENGTH_SHORT).show();

        getData();

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "add to cart clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

    private void nextpage() {
                Fragment ft = new Cart_fragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentPanel, ft)
                .addToBackStack(null).commit();
    }


    private void getData() {
       // Toast.makeText(getActivity(), "indata", Toast.LENGTH_SHORT).show();

        Map<String, String> params = new HashMap<String, String>();
       // Toast.makeText(getActivity(), product_id, Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(BaseURL.POST_PRODUCT_DETAILS)
                .addBodyParameter("product_id", product_id)

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                    //Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();

                    //  Boolean status = response.getBoolean("status");

                    if (response != null && response.length() > 0) {
                       // Log.d(TAG, "free" + response.toString());
                        Boolean status = response.getBoolean("responce");
                     //   Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();

                        if (status) {
                            JSONObject productDailog = response;
                            Log.i(TAG, "onResponse:cccc " + productDailog);

                            JSONArray jsonArray = response.getJSONArray("data");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                 product_model2 = new Product_model();
                                product_model2.setProduct_id(jsonObject.getString("product_id"));
                                product_model2.setProduct_name(jsonObject.getString("product_name"));
                                product_model2.setCategory_id(jsonObject.getString("category_id"));
                                product_model2.setPrice(jsonObject.getString("price"));
                                product_model2.setProduct_image(jsonObject.getString("product_image"));
                                product_model2.setStock(jsonObject.getString("stock"));
                                product_model2.setProduct_description(jsonObject.getString("product_description"));
                                tvItemName.setText(jsonObject.getString("product_name"));
                                tvRepees.setText(jsonObject.getString("price"));
                                tvRating.setText(jsonObject.getString("rating"));
                                tvDesc.setText(jsonObject.getString("product_description"));

                                JSONArray jsonArray1 = jsonObject.getJSONArray("sub_cat");
                                if (jsonArray1.length() > 0){
                                    for (int p = 0; p < jsonArray1.length(); p++){
                                        JSONObject obj = jsonArray1.getJSONObject(p);
//                                        Product_model.SubCat sub = new Product_model.SubCat(
//                                                obj.getString("sizes"),
//                                                obj.getString("color")
//
//                                        );
                                       // Log.i(TAG, "sizewq: " + obj.getString("sizes"));
                                        Product_model.SubCat subCat = new Product_model.SubCat(
                                                obj.getString("sizes"),
                                                obj.getString("color")

                                        );

                                         subCats.add(subCat);
                                    }
                                   // Toast.makeText(getActivity(),  subCats.size()+"", Toast.LENGTH_SHORT).show();
                                    size.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

                                  //  Size_Adapter size_adapter = new Size_Adapter(subCats, getActivity());
                                  //  size.setAdapter(size_adapter);
                                    sizeadpt.notifyDataSetChanged();

                                }

                                JSONArray jsonArray2 = jsonObject.getJSONArray("galaries");
                                if (jsonArray2.length() > 0){
                                    for (int p = 0; p < jsonArray2.length(); p++){
                                        JSONObject obj = jsonArray2.getJSONObject(p);
//                                        Product_model.SubCat sub = new Product_model.SubCat(
//                                                obj.getString("sizes"),
//                                                obj.getString("color")
//
//                                        );
                                        // Log.i(TAG, "sizewq: " + obj.getString("sizes"));
                                        Product_model.GalModel gal = new Product_model.GalModel(
                                                obj.getString("product_id"),
                                                obj.getString("photo")

                                        );

                                        gallist.add(gal);
                                    }
                                    //Toast.makeText(getActivity(),  gallist.size()+"", Toast.LENGTH_SHORT).show();
                                    gallery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
                                    Image_Adapter galadapter = new Image_Adapter(gallist, getActivity());
                                    gallery.setAdapter(galadapter);
                                   // galadapter.notifyDataSetChanged();

                                }

//
                                product_model2.setSubCat(subCats);
                                product_modelList2.add(product_model2);



                            }





                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                    }
                    @Override
                    public void onError(ANError error) {

                    }
                });

    }

}
