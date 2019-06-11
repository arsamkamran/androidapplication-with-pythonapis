package com.example.arsam.cloudcomputing;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Arsam on 17/01/2018.
 */

public class CartItemsRecycler extends RecyclerView.Adapter<CartItemsRecycler.ViewHolder>{

    List<CartItemsModel> myModel;
    Context context;
    int position;

    public CartItemsRecycler(Context context, List<CartItemsModel> cartItemsModel) {
        this.context = context;
        this.myModel = cartItemsModel;
    }

    @Override
    public CartItemsRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.model_cart_items,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float q = myModel.get(viewHolder.getPosition()).getQuantity();
                q = q-1;

                if (q==0) {
                    myModel.remove(viewHolder.getPosition());
                    notifyDataSetChanged();
                }
                else{
                    myModel.get(viewHolder.getPosition()).setQuantity(q);

                    float t = 0;
                    float p = myModel.get(viewHolder.getPosition()).getPrice();
                    t = q * p;
                    myModel.get(viewHolder.getPosition()).setTotal(t);
                    notifyDataSetChanged();
                    MainActivity.total = MainActivity.total - p;
                    MainActivity.tvtotal.setText(String.valueOf(MainActivity.total));
                }

            }
        });

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position1= viewHolder.getPosition();
                float q = myModel.get(position1).getQuantity();
                q = q+1;
                myModel.get(position1).setQuantity(q);

                float t = 0;
                float p = myModel.get(position1).getPrice();
                t = q * p;
                myModel.get(position1).setTotal(t);
                notifyDataSetChanged();
                MainActivity.total = MainActivity.total + p;
                MainActivity.tvtotal.setText(String.valueOf(MainActivity.total));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartItemsRecycler.ViewHolder holder, int position) {

        holder.minus.setImageResource(myModel.get(position).getMinus());
        holder.plus.setImageResource(myModel.get(position).getPlus());
        holder.name.setText(myModel.get(position).getName());
        holder.quantity.setText(myModel.get(position).getQuantity().toString());
        holder.price.setText(myModel.get(position).getPrice().toString());
        holder.total.setText(myModel.get(position).getTotal().toString());

    }

    @Override
    public int getItemCount() {return myModel.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView minus;
        ImageView plus;
        TextView name;
        TextView quantity;
        TextView price;
        TextView total;

        public ViewHolder(View itemView) {
            super(itemView);

            minus = (ImageView) itemView.findViewById(R.id.minus_cart_item);
            plus = (ImageView) itemView.findViewById(R.id.plus_cart_item);
            name = (TextView) itemView.findViewById(R.id.item_name_cart_item);
            quantity = (TextView) itemView.findViewById(R.id.item_quantity_cart_item);
            price = (TextView) itemView.findViewById(R.id.item_price_cart_item);
            total = (TextView) itemView.findViewById(R.id.item_total_item_cart);
        }
    }
}
