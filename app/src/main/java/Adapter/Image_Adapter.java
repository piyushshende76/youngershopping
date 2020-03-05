package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.Product_model;
import youngershopping.tcc.R;


public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.MyViewHolder> {
    private final List<Product_model.GalModel> modelList2;

    private Context context;
    String language;
    SharedPreferences preferences;
    public String image;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imagev;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text);
            imagev = (ImageView) view.findViewById(R.id.imageser);
            Glide.with(context)
                    .load(BaseURL.IMG_PRODUCT_URL + image)
                    .centerCrop()
                    .crossFade()
                    .into(imagev);

        }
    }
    public Image_Adapter(List<Product_model.GalModel> modelList2, Context context) {
        this.modelList2 = modelList2;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.size, viewGroup, false);

      //  context = viewGroup.getContext();

        return new Image_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Product_model.GalModel mList = modelList2.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .placeholder(R.drawable.icon)
                .centerCrop()
                .crossFade()
              //  .diskCacheStrategy(DiskCacheStrategy.ALL)
              //  .dontAnimate()
                .into(myViewHolder.imagev);

//                    .load(BaseURL.IMG_PRODUCT_URL + image)
//                    .centerCrop()
//                    .crossFade()
//                    .into(imagev);
        Toast.makeText(context,  modelList2.size()+"", Toast.LENGTH_SHORT).show();

        myViewHolder.title.setText(mList.getPhoto());



    }

    @Override
    public int getItemCount() {
        return modelList2 == null ? 0 : modelList2.size();
    }
}
