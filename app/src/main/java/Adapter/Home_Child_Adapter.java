package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.Home_Child_model;
import youngershopping.tcc.R;

import static Adapter.SuggestionAdapter.TAG;
import static android.content.Context.MODE_PRIVATE;

public class Home_Child_Adapter extends RecyclerView.Adapter<Home_Child_Adapter.MyViewHolder> {
    private List<Home_Child_model> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;

    public Home_Child_Adapter(List<Home_Child_model> modelList) {
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.child_catogaries, viewGroup, false);
        context = viewGroup.getContext();


        return new Home_Child_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Home_Child_model mList = modelList.get(i);
//        Glide.with(context)
//                .load(BaseURL.IMG_CATEGORY_URL + mList.getImage())
//                .placeholder(R.drawable.icon)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate();

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            myViewHolder.title.setText(mList.getTitle());
        }
        else {
            myViewHolder.title.setText(mList.getTitle());

        }

    }

    @Override
    public int getItemCount() {
           return modelList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.service_text);

            CardView cardView = (CardView) itemView.findViewById(R.id.icon_card_view);


        }
    }
}
