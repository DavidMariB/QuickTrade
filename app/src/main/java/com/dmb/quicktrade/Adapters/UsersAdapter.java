package com.dmb.quicktrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dmb.quicktrade.model.User;
import com.dmb.quicktrade.R;

import java.util.ArrayList;

/**
 * Created by davidmari on 19/1/18.
 */

public class UsersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> users;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context=context;
        this.users=users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ){
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.user_item, parent, false);
        }
        User user = users.get(position);
        ((TextView) convertView.findViewById(R.id.itemListUsername)).setText(user.getUsername());
        ((TextView) convertView.findViewById(R.id.itemListName)).setText(user.getName());
        ((TextView) convertView.findViewById(R.id.itemListSurname)).setText(user.getSurname());
        return convertView;
    }
}