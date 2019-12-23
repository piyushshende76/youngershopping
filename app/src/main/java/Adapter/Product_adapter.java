package Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Fragment.ProductDailogFragment;
import Model.Home_Size_model;
import Model.Product_model;
import Model.Size_model;
import de.hdodenhof.circleimageview.CircleImageView;
import util.RecyclerTouchListener;
import youngershopping.tcc.MainActivity;
import youngershopping.tcc.R;
import util.DatabaseHandler;

import static Adapter.SuggestionAdapter.TAG;
import static android.content.Context.MODE_PRIVATE;


public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder> {

    private Home_Size_Adapter size_adapter;
    private List<Home_Size_model> size_models = new ArrayList<>();


    private List<Product_model> modelList;
    private Context context;
    private DatabaseHandler dbcart;
    String language;
    String getprice;
    SharedPreferences preferences;

    String mColor = "#000000", mSize = "6";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_reward, tv_total, tv_contetiy, tv_add, tv_price2;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        public Double reward;

        public MyViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            iv_remove.setVisibility(View.GONE);
            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_logo.setOnClickListener(this);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (id == R.id.iv_subcat_plus) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;
                tv_contetiy.setText(String.valueOf(qty));

            } else if (id == R.id.iv_subcat_minus) {

                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

            } else if (id == R.id.tv_subcat_add) {
                HashMap<String, String> map = new HashMap<>();
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");
                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("product_name", modelList.get(position).getProduct_name());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_description", modelList.get(position).getProduct_description());
                map.put("deal_price", modelList.get(position).getDeal_price());
                map.put("start_date", modelList.get(position).getStart_date());
                map.put("start_time", modelList.get(position).getStart_time());
                map.put("end_date", modelList.get(position).getEnd_date());
                map.put("end_time", modelList.get(position).getEnd_time());
                map.put("price", modelList.get(position).getPrice());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("status", modelList.get(position).getStatus());
                map.put("in_stock", modelList.get(position).getIn_stock());
                map.put("unit_value", modelList.get(position).getUnit_value());
                map.put("unit", modelList.get(position).getUnit());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("rewards", modelList.get(position).getRewards());
                map.put("stock", modelList.get(position).getStock());
                map.put("title", modelList.get(position).getTitle());


                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }
                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));

                Double price = Double.parseDouble(map.get("price").trim());
                Double reward = Double.parseDouble("0");
                tv_reward.setText("" + reward * items);
                tv_total.setText("" + price * items);
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

            } else if (id == R.id.iv_subcat_img) {
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");
                Log.d("lang", language);
                if (language.contains("english")) {
                    showProductDetail(modelList.get(position).getProduct_image(),
                            modelList.get(position).getProduct_name(),
                            modelList.get(position).getProduct_description(),
                            "",
                            position, tv_contetiy.getText().toString(),
                            modelList.get(position).getSubCat());
                } else {
                    showProductDetail(modelList.get(position).getProduct_image(),
                            modelList.get(position).getProduct_name_arb(),
                            modelList.get(position).getProduct_description_arb(),
                            "",
                            position, tv_contetiy.getText().toString(),
                            modelList.get(position).getSubCat());
                }
            }

        }
    }

    public Product_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        dbcart = new DatabaseHandler(context);
    }


    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_rv, parent, false);
        context = parent.getContext();
        return new Product_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Product_adapter.MyViewHolder holder, int position) {
        Product_model mList = modelList.get(position);

       /* if (mList.getSubCat().size() > 0) {
            for (int i = 0; i < mList.getSubCat().size(); i++)
                Log.i(TAG, "onBindViewHolder: " + mList.getSubCat().get(i).getColor());
        }

*/

        Log.i(TAG, "onBindViewHolder:position "+position);
        try {
            if (mList.getSubCat().size() > 0) {
                for (int i = 0; i < mList.getSubCat().size(); i++)
                    Log.i(TAG, "onBindViewHolder: " + mList.getSubCat().get(i).getColor());
                Log.i(TAG, "onBindViewHolder: " + position);
            }
        } catch (Exception ignored) {

        }

        //   Log.i(TAG, "onBindViewHolder: " + modelList.get(3).getSubCat().get(0).getColor());


        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
               // .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language = preferences.getString("language", "");
       holder.tv_title.setText(mList.getProduct_name());
        holder.tv_reward.setText(mList.getRewards());
        holder.tv_price.setText(context.getResources().getString(R.string.tv_pro_price) + " " +
                mList.getPrice() + context.getResources().getString(R.string.currency));
        Log.i(TAG, "price2: " + getprice);


        holder.iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* ProductDailogFragment fragmentB=new ProductDailogFragment();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel,fragmentB)
                        .addToBackStack(null).commit();
*/
            }
        });



        getprice = mList.getPrice();


        if (Integer.valueOf(modelList.get(position).getStock()) <= 0) {
            holder.tv_add.setText(R.string.tv_out);
            holder.tv_add.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
            holder.tv_add.setEnabled(false);
            holder.iv_minus.setEnabled(false);
            holder.iv_plus.setEnabled(false);
        } else if (dbcart.isInCart(mList.getProduct_id())) {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getProduct_id()));

        } else {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }
        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
        Double price = Double.parseDouble(mList.getPrice());
        //   Double reward = Double.parseDouble(mList.getRewards());
        holder.tv_total.setText("" + price * items);
        // holder.tv_reward.setText("" + reward * items);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.product_image_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void showProductDetail(String image,
                                   String title,
                                   String description,
                                   String detail,
                                   final int position,
                                   String qty,
                                   final ArrayList<Product_model.SubCat> subCat) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_detail);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();


        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_product_detail_img);
        final ImageView iv_minus = (ImageView) dialog.findViewById(R.id.iv_subcat_minus);
        final ImageView iv_plus = (ImageView) dialog.findViewById(R.id.iv_subcat_plus);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_product_detail_title);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = (TextView) dialog.findViewById(R.id.tv_subcat_contetiy);
        final TextView tv_add = (TextView) dialog.findViewById(R.id.tv_subcat_add);
        final TextView tv_price = (TextView) dialog.findViewById(R.id.tv_product_price);
        final ArrayList<Size_model>[] size_models = new ArrayList[]{new ArrayList<>()};
        final String[] getid = new String[1];
        final RecyclerView size_rv = (RecyclerView) dialog.findViewById(R.id.size);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {
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
        size_rv.setLayoutManager(layoutManager);
        size_rv.setHasFixedSize(true);
        size_rv.setItemViewCacheSize(10);
        size_rv.setDrawingCacheEnabled(true);
        size_rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        size_rv.setAdapter(new SubCatDetals(subCat, context));


//Recycler View Menu Products
        size_rv.addOnItemTouchListener(new RecyclerTouchListener(context, size_rv, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //   getid = size_models.get(position).getProduct_id();
                Bundle args = new Bundle();
                //  args.putString("cat_id", getid);
                Log.i(TAG, "onItemClick: " + getid);
                //  makeGetProductRequest(getid);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        tv_price.setText(context.getResources().getString(R.string.tv_pro_price) + " " +
                getprice + context.getResources().getString(R.string.currency));
        Log.i(TAG, "price: " + getprice);

        tv_title.setText(title);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_detail.setText(description);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .crossFade()
                .into(iv_image);
        if (Integer.valueOf(modelList.get(position).getStock()) <= 0) {
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(context.getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        } else if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");

                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("product_name", modelList.get(position).getProduct_name());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_description", modelList.get(position).getProduct_description());
                map.put("deal_price", modelList.get(position).getDeal_price());
                map.put("start_date", modelList.get(position).getStart_date());
                map.put("start_time", modelList.get(position).getStart_time());
                map.put("end_date", modelList.get(position).getEnd_date());
                map.put("end_time", modelList.get(position).getEnd_time());
                map.put("price", modelList.get(position).getPrice());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("status", modelList.get(position).getStatus());
                map.put("in_stock", modelList.get(position).getIn_stock());
                map.put("unit_value", mSize);
                map.put("unit", mColor);
                map.put("increament", modelList.get(position).getIncreament());
                map.put("rewards", modelList.get(position).getRewards());
                map.put("stock", modelList.get(position).getStock());
                map.put("title", modelList.get(position).getTitle());


                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

                notifyItemChanged(position);


                dialog.dismiss();

            }
        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }
            }
        });






        /*String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        boolean isSubcat = true;
       *//* if (parent_id != null && parent_id != "") {
        }*//*

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                Log.i(TAG, "ragu: " + response);

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("status");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            size_models[0] = gson.fromJson(response.getString("main_categoty"), listType);
                            size_adapter = new Home_Size_Adapter(size_models[0]);
                               size_rv.setAdapter(size_adapter);
                            size_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                   // Toast.makeText(context, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


*/
    }

    public class SubCatDetals extends RecyclerView.Adapter<SubCatDetals.Holder> {

        ArrayList<Product_model.SubCat> subCats = new ArrayList<>();
        Context context;

        public SubCatDetals(ArrayList<Product_model.SubCat> subCat, Context context) {
            this.subCats = subCat;
            this.context = context;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.size, viewGroup, false);
            return new Holder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int i) {
            try {
                final Product_model.SubCat cat = subCats.get(i);
                holder.service_text.setText(cat.getSizes());
                holder.service_image.setBackgroundColor(Color.parseColor(cat.getColor()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*HashMap<String, String> map = new HashMap<>();
                        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                        language = preferences.getString("language", "");
                        Toast.makeText(context, ""+cat.getColor(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: "+cat.getColor());
                        map.put("unit_value", cat.getSizes());
                        map.put("unit", cat.getColor());*/

                        mColor = cat.getColor();
                        mSize = cat.getSizes();

                        Log.i(TAG, "onClick: "+mSize);
                    }
                });

            } catch (Exception ignored) {

            }

        }

        @Override
        public int getItemCount() {
            return subCats == null ? 0 : subCats.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            CircleImageView service_image;
            TextView service_text;

            public Holder(@NonNull View itemView) {
                super(itemView);
                service_image = itemView.findViewById(R.id.service_image);
                service_text = itemView.findViewById(R.id.service_text);
            }
        }
    }

}