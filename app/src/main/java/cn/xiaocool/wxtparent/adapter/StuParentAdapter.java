package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.StuParent;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class StuParentAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<StuParent> stuParents;
    private LayoutInflater inflater;
    public StuParentAdapter(Context context,ArrayList<StuParent> stuParents){
        this.context = context;
        this.stuParents = stuParents;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return stuParents.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return stuParents.get(groupPosition).getParent_info().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return stuParents.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return stuParents.get(groupPosition).getParent_info().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        StuParent stuParent = stuParents.get(groupPosition);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_stu,null);
        }
        ImageView iv_avatar = (ImageView) convertView.findViewById(R.id.stu_avatar);
        TextView tv_name = (TextView) convertView.findViewById(R.id.stu_name);
        ImageView iv_jiantou = (ImageView) convertView.findViewById(R.id.jiantou);
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+stuParent.getPhoto(),iv_avatar);
        tv_name.setText(stuParent.getName());
        if(isExpanded){
            iv_jiantou.setImageResource(R.drawable.address_triangle_down);
        }else{
            iv_jiantou.setImageResource(R.drawable.address_triangle);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        StuParent.ParentInfoBean parentInfoBean = stuParents.get(groupPosition).getParent_info().get(childPosition);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_parent_info,null);
        }
        ImageView iv_avatar = (ImageView) convertView.findViewById(R.id.avator);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+parentInfoBean.getPhoto(),iv_avatar);
        tv_name.setText(parentInfoBean.getName()+"（"+parentInfoBean.getAppellation()+"）");
        tv_phone.setText(parentInfoBean.getPhone());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
