package com.dmb.quicktrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dmb.quicktrade.R;
import com.dmb.quicktrade.model.Product;

import java.util.ArrayList;

/**
 * Created by davidmari on 21/1/18.
 */

public class ProductsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Product> products;

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        this.context=context;
        this.products=products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ){
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.product_item, parent, false);
        }
        Product product = products.get(position);
        ((TextView) convertView.findViewById(R.id.listProdName)).setText(product.getName());
        ((TextView) convertView.findViewById(R.id.listProdDesc)).setText(product.getDescription());
        ((TextView) convertView.findViewById(R.id.listProdPrice)).setText(product.getPrice());
        return convertView;
    }

    public void updateAdapter(ArrayList<Product> products){

        this.products = products;
        notifyDataSetChanged();
    }

}