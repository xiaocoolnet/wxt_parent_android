package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.RecipeInfo;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class Recipes_Adapter extends BaseAdapter {


    private LayoutInflater layoutInflater;
    private ArrayList<RecipeInfo> recipeDatas;
    private ArrayList<String> pics;

    private Context context;

    public Recipes_Adapter(ArrayList<RecipeInfo> recipeDatas, Context context) {
        this.recipeDatas = recipeDatas;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recipeDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_receipe, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        vh.tv_week.setText(recipeDatas.get(position).getWeek());
        RecipesItemAdapter adapter = new RecipesItemAdapter(recipeDatas.get(position).getData(),context);
        vh.lv_recipe.setAdapter(adapter);


        return convertView;
    }


    class ViewHolder{
        TextView tv_week;
        ListView lv_recipe;
        public ViewHolder(View convertView){
            tv_week = (TextView) convertView.findViewById(R.id.recipes_item_week);
            lv_recipe = (ListView) convertView.findViewById(R.id.recipes_item_list);
        }
    }
}
