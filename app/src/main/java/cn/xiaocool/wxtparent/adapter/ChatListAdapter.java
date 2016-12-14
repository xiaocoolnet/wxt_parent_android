package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.CommunicateListModel;
import cn.xiaocool.wxtparent.main.TeacherCommunicationActivity;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.ui.RoundImageView;


/**
 * Created by Administrator on 2016/10/27.
 */
public class ChatListAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommunicateListModel> communicateModelList;
    public ChatListAdapter(Context context, List<cn.xiaocool.wxtparent.bean.CommunicateListModel> communicateModels){
        this.mContext = context;
        this.communicateModelList = communicateModels;

    }

    @Override
    public int getCount() {
        return communicateModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return communicateModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final cn.xiaocool.wxtparent.bean.CommunicateListModel communicateModel = communicateModelList.get(position);
        cn.xiaocool.wxtparent.utils.ImgLoadUtil.displayHeader(NetBaseConstant.NET_CIRCLEPIC_HOST + communicateModel.getOther_face(), holder.item_header);
        holder.item_name.setText(communicateModel.getOther_nickname());
        holder.item_content.setText(communicateModel.getLast_content());
        holder.item_time.setText(communicateModel.getCreate_time());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,TeacherCommunicationActivity.class);
                intent.putExtra("reciver_id",communicateModel.getChat_uid());
                intent.putExtra("chatid",communicateModel.getType().equals("0") ? communicateModel.getId() : communicateModel.getChat_uid());
                intent.putExtra("chat_name",communicateModel.getOther_nickname());
                intent.putExtra("type",communicateModel.getType());
                intent.putExtra("usertype",communicateModel.getReceive_type());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{

        RoundImageView item_header;
        TextView item_name,item_content,item_time;
        public ViewHolder(View convertView) {
            item_header = (RoundImageView) convertView.findViewById(R.id.item_header);
            item_name = (TextView) convertView.findViewById(R.id.item_name);
            item_content = (TextView) convertView.findViewById(R.id.item_content);
            item_time = (TextView) convertView.findViewById(R.id.item_time);


        }
    }
}
