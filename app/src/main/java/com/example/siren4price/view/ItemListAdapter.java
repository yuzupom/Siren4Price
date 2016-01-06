package com.example.siren4price.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.siren4price.R;
import com.example.siren4price.value.Item;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by k_asano on 16/01/06.
 */
public class ItemListAdapter extends BaseAdapter{

    private Context context;
    private List<Item> list;
    private LayoutInflater layoutInflater = null;

    public ItemListAdapter(Context c, List<Item> list){
        super();
        this.context = c;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item i = list.get(position);
        View view;
        if(convertView != null){
            view = convertView;
        }else {
            view = layoutInflater.inflate(R.layout.list_item_item, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.list_item_item_name);
        tv.setText(i.getName());

        tv = (TextView) view.findViewById(R.id.list_item_item_buy);
        tv.setText(String.valueOf(i.getBuyPrice()));

        tv = (TextView) view.findViewById(R.id.list_item_item_sell);
        tv.setText(String.valueOf(i.getSellPrice()));

        return view;
    }

    public void setItemList(List<Item> list){
        this.list = list;
    }
}
