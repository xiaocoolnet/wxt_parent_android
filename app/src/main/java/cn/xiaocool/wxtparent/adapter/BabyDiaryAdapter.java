package cn.xiaocool.wxtparent.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ClassCricleInfo;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.bean.LikeBean;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.CommentPopupWindow;
import cn.xiaocool.wxtparent.utils.LogUtils;


/**
 * Created by Administrator on 2016/5/9.
 */
public class BabyDiaryAdapter extends BaseAdapter {
    private static final String TAG = "homework_praise";
    private ArrayList<String> pics;
    private List<ClassCricleInfo> homeworkDataList;
    private LinearLayout commentView;
    private LayoutInflater inflater;
    private String type;
    private Context context;
    private Handler handler;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private static final int GET_VIEWPAPER_LIST_KEY = 1;
    private static final int GET_LIST_KEY = 102;
    private static final int HOMEWORK_PRAISE_KEY = 104;
    private static final int DEL_HOMEWORK_PRAISE_KEY = 105;
    private static long lastClickTime;
    private DisplayImageOptions displayImage;
    private ArrayList<Comments> comment;
    public CommentPopupWindow commentPopupWindow;
    private UserInfo user;
    public BabyDiaryAdapter(List<ClassCricleInfo> homeworkDataList, Context mContext, Handler handler, LinearLayout commentView, String type) {
        this.context = mContext;
        this.handler= handler;
        this.commentView=commentView;
        this.type = type;
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();
        this.homeworkDataList = homeworkDataList;
        this.inflater = LayoutInflater.from(mContext);
        user = new UserInfo(context);
        user.readData(context);
        Log.e("sdsd",homeworkDataList.toString());
    }
    @Override
    public int getCount() {
        return homeworkDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeworkDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null){
            convertView = inflater.inflate(R.layout.my_message_diary,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.homework_content.setText(homeworkDataList.get(position).getMatter());
        Date date = new Date();
        date.setTime(Long.parseLong(homeworkDataList.get(position).getAddtime()) * 1000);
        //判断当天
        String section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.rl_second.setVisibility(View.VISIBLE);
            holder.rl_first.setVisibility(View.GONE);
        } else {
            holder.rl_first.setVisibility(View.VISIBLE);
            holder.rl_second.setVisibility(View.GONE);
        }
        holder.tv_week.setText(new SimpleDateFormat("EEEE").format(date));
        holder.tv_date.setText(new SimpleDateFormat("MM-dd").format(date));
        holder.homework_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));


        //判断图片并显示（一张图片显示imageview，多于一张显示gridview）
        if (homeworkDataList.get(position).getWorkImgs().size()>1){
            holder.parent_warn_img_gridview.setVisibility(View.VISIBLE);
            holder.iv_homework.setVisibility(View.GONE);
            MyGridAdapter parWarnImgGridAdapter = new MyGridAdapter( homeworkDataList.get(position).getWorkImgs(),context);
            holder.parent_warn_img_gridview.setAdapter(parWarnImgGridAdapter);
            holder.parent_warn_img_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    /*ArrayList<String> pics = new ArrayList<String>();
                    pics.add(homeworkDataList.get(position).getPhoto());*/
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", homeworkDataList.get(position).getWorkImgs());
//                    intent.putExtra("type", "4");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });
        }else if(homeworkDataList.get(position).getWorkImgs().size()==1){
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_homework.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + homeworkDataList.get(position).getWorkImgs().get(0), holder.iv_homework, displayImage);
            holder.iv_homework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", homeworkDataList.get(position).getWorkImgs());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else{
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_homework.setVisibility(View.GONE);
        }
        //判断点赞点赞与否
        holder.linearLayout_homework_item_praise.setVisibility(View.GONE);
        if (homeworkDataList.get(position).getWorkPraise().size()>0){
            Log.e("size------",homeworkDataList.get(position).getWorkPraise().size()+"");
            holder.linearLayout_homework_item_praise.setVisibility(View.VISIBLE);
            String names = "";
            for (int i=0;i<homeworkDataList.get(position).getWorkPraise().size();i++){
                names = names+" "+homeworkDataList.get(position).getWorkPraise().get(i).getName();
            }
            holder.homework_item_praise_names.setText(names);
        }

        //判断本人是否已经点赞
        if (isPraise(homeworkDataList.get(position).getWorkPraise())) {
            //点赞成功后图片变红
            holder.homework_praise.setSelected(true);
        } else {
            //取消点赞后
            holder.homework_praise.setSelected(false);
        }

        //获取评论
        comment=homeworkDataList.get(position).getComment();
        CommentAdapter adapter=new CommentAdapter(context,comment);
        holder.comment_list.setAdapter(adapter);

        //点赞事件
        holder.homework_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFastClick()){
                    return;
                }else{
                    if (isPraise(homeworkDataList.get(position).getWorkPraise())) {
                        LogUtils.d("FindFragment", "delPraise");
                        delPraise(homeworkDataList.get(position).getId());
                    } else {
                        LogUtils.d("FindFragment", "workPraise");
                        workPraise(homeworkDataList.get(position).getId());
                    }
                }
            }
        });

        final int a = position;
        //评论事件
        holder.homework_discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPopupWindow = new CommentPopupWindow(context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_comment:
                                if (commentPopupWindow.ed_comment.getText().length() > 0) {
                                    new SpaceRequest(context, handler).send_remark(homeworkDataList.get(position).getId(), String.valueOf(commentPopupWindow.ed_comment.getText()), type);
                                    commentPopupWindow.dismiss();
                                    commentPopupWindow.ed_comment.setText("");
                                } else {

                                    Toast.makeText(context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
                final EditText editText = commentPopupWindow.ed_comment;
                commentPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    public void run() {
                        InputMethodManager inputManager =
                                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(editText, 0);
                    }
                },300);

            }
        });
        return convertView;
    }

    private void showDialog(final String id) {

        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);    // 系统默认Dialog没有输入框

        // 获取自定义的布局
        View alertDialogView = View.inflate(context, R.layout.edit_and_send, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);

        // 2.密码框-EditText。alertDialogView.findViewById(R.id.自定义布局中的文本框)
        final EditText et_dialog_confirmphoneguardpswd = (EditText) alertDialogView.findViewById(R.id.btn_discuss);

        // 确认按钮，确认验证密码
        Button btn_dialog_resolve_confirmphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_ok);
        btn_dialog_resolve_confirmphoneguardpswd.setOnClickListener(new View.OnClickListener() {
            // 点击按钮处理
            public void onClick(View v) {
                // 提取文本框中输入的文本密码
                if (et_dialog_confirmphoneguardpswd.getText().length() > 0) {
                    //获取到需要上传的参数

                    new SpaceRequest(context, handler).send_remark(id, String.valueOf(et_dialog_confirmphoneguardpswd.getText()), "7");

                } else {

                    Toast.makeText(context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                }
                tempDialog.dismiss();
            }
        });
        // 取消按钮，不验证密码
        Button btn_dialog_cancel_confirmphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_cancel);
        btn_dialog_cancel_confirmphoneguardpswd.setOnClickListener(new View.OnClickListener() {
            // 点击按钮处理
            public void onClick(View v) {
                //
                tempDialog.dismiss();
            }
        });

        tempDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_dialog_confirmphoneguardpswd, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        /** 3.自动弹出软键盘 **/
        tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_dialog_confirmphoneguardpswd, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        tempDialog.show();
    }
    // 点赞
    private void workPraise(String workBindId) {
        Log.i("begintopppp-=====", "222222");
            new SpaceRequest(context, handler).Praise(workBindId, HOMEWORK_PRAISE_KEY,type);
    }

    // 取消点赞
    private void delPraise(String workBindId) {
        new SpaceRequest(context, handler).DelPraise(workBindId, DEL_HOMEWORK_PRAISE_KEY, type);
    }

    /**
     * 判断当前用户是否点赞
     * */
    private boolean isPraise(ArrayList<LikeBean> praises) {
        for (int i = 0; i < praises.size(); i++) {
            if (praises.get(i).getUserid().equals(user.getUserId())) {
                Log.d("praisesid", praises.get(i).getUserid());
                return true;
            }
        }
        return false;
    }

    public boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    class ViewHolder{
        TextView homework_content,homework_time,homework_item_praise_names,alread_text,not_read_text;
        ImageView homework_praise,homework_discuss;
        private ImageView iv_homework;
        LinearLayout linearLayout_homework_item_praise,comment_view;
        GridView parent_warn_img_gridview;
        TextView tv_week,tv_date;
        RelativeLayout rl_first,rl_second;
        private ListView comment_list;
        public ViewHolder(View convertView) {
            tv_week = (TextView) convertView.findViewById(R.id.tv_week);
            tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            rl_first = (RelativeLayout) convertView.findViewById(R.id.rl_first);
            rl_second = (RelativeLayout) convertView.findViewById(R.id.rl_second);
            iv_homework = (ImageView) convertView.findViewById(R.id.parent_warn_img);
            comment_list = (ListView) convertView.findViewById(R.id.comment_list);
            comment_view = (LinearLayout) convertView.findViewById(R.id.edit_and_send);
            homework_content = (TextView) convertView.findViewById(R.id.myhomework_content);
            homework_time = (TextView) convertView.findViewById(R.id.myhomework_time);
            not_read_text = (TextView) convertView.findViewById(R.id.not_read_text);
            alread_text = (TextView) convertView.findViewById(R.id.alread_text);
            homework_praise = (ImageView) convertView.findViewById(R.id.homework_praise);
            homework_discuss = (ImageView) convertView.findViewById(R.id.homework_discuss);
            homework_item_praise_names = (TextView) convertView.findViewById(R.id.homework_item_praise_names);
            linearLayout_homework_item_praise = (LinearLayout) convertView.findViewById(R.id.linearLayout_homework_item_praise);
            parent_warn_img_gridview = (GridView) convertView.findViewById(R.id.parent_warn_img_gridview);

        }
    }
    public static long getTodayZero() {
        Date date = new Date();
        long l = 24*60*60*1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
    }
    public String getSectionForPosition(int position) {
        return getDate(homeworkDataList.get(position).getAddtime());
    }


    public int getPositionForSection(String section) {
        for (int i = 0; i < getCount(); i++) {
            String firstChar = getDate(homeworkDataList.get(i).getAddtime());
            if (firstChar.equals(section) ) {
                return i;
            }
        }

        return -1;
    }

    public String getDate(String time){
        Date date = new Date();
        date.setTime(Long.parseLong(time) * 1000);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
