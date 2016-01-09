package com.axovel.ecommerce.sweetschoice.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.axovel.ecommerce.sweetschoice.R;
import com.axovel.ecommerce.sweetschoice.model.RequestResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Umesh Chauhan on 05-01-2016.
 * Axovel Private Limited
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder>  {
    private Context mContext;
    private ArrayList<RequestResponse> productList;

    public RecyclerAdapter(Context mContext, ArrayList<RequestResponse> productList){
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_product, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        // OnClick Product
        viewHolder.cvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getAdapterPosition();
            }
        });
        viewHolder.cvProduct.setTag(viewHolder);
        // OnClick Add to Cart
        viewHolder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getAdapterPosition();
                Snackbar.make(v, "Product Added to Cart " + position, Snackbar.LENGTH_SHORT).show();
            }
        });
        viewHolder.btnAddToCart.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        RequestResponse product = productList.get(i);

        //Download image using picasso library
        //Picasso.with(mContext).load(productList.get(i).getImgURL())
        Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png")
        .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.imgProductThumbnail);

        //Setting text
        customViewHolder.txtProductName.setText(productList.get(i).getProductName());
        customViewHolder.txtProductCost.setText(productList.get(i).getCost());
    }

    @Override
    public int getItemCount() {
        return (null != productList ? productList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgProductThumbnail;
        protected TextView txtProductName;
        protected TextView txtProductCost;
        protected View cvProduct;
        protected Button btnAddToCart;

        public CustomViewHolder(View view) {
            super(view);
            this.imgProductThumbnail = (ImageView) view.findViewById(R.id.imgProductThumbnail);
            this.txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            this.txtProductCost = (TextView) view.findViewById(R.id.txtProductCost);
            this.cvProduct = (View) view.findViewById(R.id.cv_product);
            this.btnAddToCart = (Button) view.findViewById(R.id.btnAddCart);
        }
    }
}
