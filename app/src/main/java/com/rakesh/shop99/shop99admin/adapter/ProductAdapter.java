package com.rakesh.shop99.shop99admin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.rakesh.shop99.shop99admin.R;
import com.rakesh.shop99.shop99admin.model.ProductModel;

import java.util.List;

/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<ProductModel> products;

    public ProductAdapter(Context context, List<ProductModel> TempList) {
        this.products = TempList;
        this.context = context;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductModel productModel = products.get(position);

        holder.name.setText(productModel.getName());
        holder.description.setText(productModel.getDescription());
        holder.r_price.setText(productModel.getRegular_price());
        holder.s_price.setText(productModel.getSelling_price());
        Glide.with(context).load(productModel.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {

        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name,description,r_price,s_price;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            description=(TextView)itemView.findViewById(R.id.description);
            r_price=(TextView)itemView.findViewById(R.id.r_price);
            s_price=(TextView)itemView.findViewById(R.id.s_price);
            image = (ImageView) itemView.findViewById(R.id.image);


        }
    }
}