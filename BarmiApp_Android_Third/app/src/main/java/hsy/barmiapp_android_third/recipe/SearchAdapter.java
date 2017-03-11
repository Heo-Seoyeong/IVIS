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

public class SearchAdapter extends BaseAdapter {
    private LayoutInflater inflaterA = null;
    private ArrayList<HashMap<String,String>> allAry = null;
    int[] imgs = null;

    public SearchAdapter(Context conA, ArrayList<HashMap<String,String>> allAry, int[] imgs){
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
            convertView = this.inflaterA.inflate(R.layout.cell_search,null);
            holder = new ViewHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.search_cell_recipe_name);
            holder.tvImgae = (ImageView) convertView.findViewById(R.id.search_cell_recipe_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.tvName.setText(allAry.get(position).get("name"));
        holder.tvImgae.setBackgroundResource(imgs[position]);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName; //레시피 이름
        ImageView tvImgae; //레시피 이미지

    }
}