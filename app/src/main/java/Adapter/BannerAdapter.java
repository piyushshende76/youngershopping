package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import Config.BaseURL;
import Model.BannerCartModel;
import Model.Child_Home_Model;
import youngershopping.tcc.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.Holder> {

    Context context;
    ArrayList<Child_Home_Model> bannerCartModels = new ArrayList<>();

    public BannerAdapter(Context context, ArrayList<Child_Home_Model> bannerCartModels) {
        this.context = context;
        this.bannerCartModels = bannerCartModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.banner_cart, viewGroup, false);
        context = viewGroup.getContext();
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        final Child_Home_Model bannerCartModel = bannerCartModels.get(i);

        Glide.with(context)
                .load(R.drawable.icon)
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        holder.product_name.setText(bannerCartModel.getChildName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return bannerCartModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView product_name, product_prize;
        public ImageView image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_prize = (TextView) itemView.findViewById(R.id.product_prize);
            image = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }
}
