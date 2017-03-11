package hsy.barmiapp_android_third.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import hsy.barmiapp_android_third.R;

/**
 * Created by hsy on 2016. 12. 29..
 */

public class FavoriteAdapter extends BaseAdapter {
    private LayoutInflater inflaterA = null;
    private ArrayList<HashMap<String,String>> allAry = null;
    int[] imgs = null;

    public FavoriteAdapter(Context conA, ArrayList<HashMap<String,String>> allAry, int[] imgs){
        this.inflaterA = (LayoutInflater)conA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imgs = imgs;
        this.allAry = allAry;
    }

    @Override
    public int getCount() {
        return allAry.size();
    }

    @Override
    public Object getItem(int position) {
        return this.allAry.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null){
            convertView = this.inflaterA.inflate(R.layout.cell_favorite,null);
            holder = new ViewHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.favorit_info_cell_title_textview);
            holder.tvImgae = (ImageView) convertView.findViewById(R.id.favorit_cell_recipe_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        String id = allAry.get(position).get("id");
        holder.tvName.setText(allAry.get(position).get("name"));
        holder.tvImgae.setBackgroundResource(imgs[Integer.parseInt(id)-1]);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        ImageView tvImgae;
    }
}