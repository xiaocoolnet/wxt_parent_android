package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ClassCricleInfo;
import cn.xiaocool.wxtparent.bean.LikeBean;
import cn.xiaocool.wxtparent.bean.MemberInfo;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.ui.NoScrollGridView;
import cn.xiaocool.wxtparent.ui.NoScrollListView;
import cn.xiaocool.wxtparent.ui.RoundImageView;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.TimeToolUtils;

/**
 * Created by mac on 16/2/21.
 */
public class ProfessionCircleAdapter_del extends BaseAdapter {
    private Context context;
    private ArrayList<ClassCricleInfo> workRingList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    public ProfessionCircleAdapter_del(Context mContext, ArrayList<ClassCricleInfo> workRings) {
        this.context = mContext;
        if (workRings == null) {
            workRings = new ArrayList<ClassCricleInfo>();
        }
        this.workRingList = workRings;
    }

    @Override
    public int getCount() {
        return workRingList.size();
    }

    @Override
    public ClassCricleInfo getItem(int position) {
        return workRingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final ClassCricleInfo workRing = workRingList.get(position);
        final ArrayList<LikeBean> praises = workRing.getWorkPraise();
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.profession_circle_item, parent, false);
            holder.gridView = (NoScrollGridView) convertView.findViewById(R.id.gridView_profession_circle_item);
            holder.iv_head = (RoundImageView) convertView.findViewById(R.id.iv_profession_circle_item_head);
            holder.iv_isKa=(ImageView) convertView.findViewById(R.id.iv_profession_circle_item_ka);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_profession_circle_item_name);
//            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_profession_circle_item_post);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_profession_circle_item_content);
            holder.tv_tag = (TextView) convertView.findViewById(R.id.tv_profession_circle_item_label);
            holder.rl_praise = (RelativeLayout) convertView.findViewById(R.id.include_profession_circle_item_praise);
            holder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_profession_praise);
            holder.tv_praise_num = (TextView) convertView.findViewById(R.id.tv_profession_praise);
            holder.tv_discuss_num = (TextView) convertView.findViewById(R.id.tv_profession_comment);
            holder.rl_share = (RelativeLayout) convertView.findViewById(R.id.include_profession_circle_item_share);
            holder.rl_delete = (RelativeLayout) convertView.findViewById(R.id.include_profession_circle_item_delete);
            holder.tv_praise_names = (TextView) convertView.findViewById(R.id.tv_profession_circle_item_praise_names);
            holder.ll_praiseNames = (LinearLayout) convertView.findViewById(R.id.linearLayout_profession_circle_item_praise);
            holder.ll_discussContent = (LinearLayout) convertView.findViewById(R.id.linearLayout_profession_circle_item_discuss);
            holder.ll_discussContent.setVisibility(View.GONE);
            holder.lv_first_discuss = (NoScrollListView) convertView.findViewById(R.id.listView_profession_circle_item_discuss);
            holder.tv_more_discuss = (TextView) convertView.findViewById(R.id.tv_profession_circle_item_more_discuss);
            holder.tv_more_discuss.setVisibility(View.GONE);
            holder.ll_informatoin = (LinearLayout) convertView.findViewById(R.id.linearLayout_profession_circle_item_information);
            holder.tv_information = (TextView) convertView.findViewById(R.id.tv_profession_circle_item_information);
            holder.iv_information = (ImageView) convertView.findViewById(R.id.iv_profession_circle_item_information);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_profession_cicle_item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(workRing.getMemberName());
//        holder.tv_position.setHint(workRing.getMemberPostition());
        holder.tv_tag.setHint(workRing.getMemberCompany());
        imageLoader.displayImage(NetBaseConstant.NET_BASE_HOST + "/" + workRing.getMemberImg(), holder.iv_head, options);
        if (workRing.getMemberIsKa().equals("0")) {
            holder.iv_isKa.setVisibility(View.GONE);
        }else if (workRing.getMemberIsKa().equals("1")){
            holder.iv_isKa.setVisibility(View.VISIBLE);
        }
        if (workRing.getNewId().equals("0")) {
            holder.ll_informatoin.setVisibility(View.GONE);
            holder.gridView.setVisibility(View.VISIBLE);
            holder.tv_content.setText(workRing.getMatter());
        } else {
            holder.gridView.setVisibility(View.GONE);
            holder.ll_informatoin.setVisibility(View.VISIBLE);
            holder.tv_content.setText("分享资讯");
            holder.tv_information.setText(workRing.getNews().getNewsTitle());
            if (workRing.getNews().getNewsImg() != null) {
                if (workRing.getNews().getNewsImg().size() > 0) {
                    imageLoader.displayImage(NetBaseConstant.NET_BASE_HOST + "/" + workRing.getNews().getNewsImg().get(0), holder.iv_information, options);
                } else {
                    holder.iv_information.setImageResource(R.drawable.default_avatar);
                }
            } else {
                holder.iv_information.setImageResource(R.drawable.default_avatar);
            }

        }
        holder.ll_informatoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent(context, ProfessionCircleInformationActivity.class);
//                intent.putExtra("NewId", workRing.getNewId());
//                startActivity(intent);
            }
        });
        if (workRing.getWorkPraiseNum().equals("0")) {
            holder.tv_praise_num.setVisibility(TextView.INVISIBLE);
        } else {
            holder.tv_praise_num.setVisibility(TextView.VISIBLE);
            holder.tv_praise_num.setText(workRing.getWorkPraiseNum());
        }
        if (workRing.getWorkDiscussNum().equals("0")) {
            holder.tv_discuss_num.setVisibility(TextView.INVISIBLE);
        } else {
            holder.tv_discuss_num.setVisibility(TextView.VISIBLE);
            holder.tv_discuss_num.setText(workRing.getWorkDiscussNum());
        }
        // 显示图片
        if (workRing != null && workRing.getWorkImgs().size() > 0) {
            holder.gridView.setVisibility(View.VISIBLE);
            holder.gridView.setAdapter(new MyGridAdapter(workRing.getWorkImgs(), context));
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", workRing.getWorkImgs());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.gridView.setVisibility(View.GONE);
        }
        Long time = Long.parseLong(workRing.getAddtime());
        String t = TimeToolUtils.fromateTimeShowByRule(time * 1000);
        holder.tv_time.setHint(t);
        UserInfo user = new UserInfo();
        user.readData(context);
        if (workRing.getBindMemberId().equals(user.getUserId())) {
            holder.rl_delete.setVisibility(View.VISIBLE);
        } else {
            holder.rl_delete.setVisibility(View.GONE);
        }
        String names = "";
        if (praises != null) {
            if (praises.size() > 0) {
                holder.ll_praiseNames.setVisibility(RelativeLayout.VISIBLE);
                LogUtils.i("ID", "workRing.getWorkBindMemberId()" + workRing.getBindMemberId());
                if (isPraise(praises)) {
                    holder.iv_praise.setImageResource(R.drawable.icon_praise_red);
                } else {
                    holder.iv_praise.setImageResource(R.drawable.icon_praise_blue);
                }
                if (praises.size() > 5) {
                    for (int i = 0; i < 5; i++) {
                        if (names.equals("")) {
                            names = praises.get(i).getName();
                        } else {
                            names = names + "," + praises.get(i).getName();
                        }
                    }
                    names = names + "等" + praises.size() + "人赞过";
                } else {
                    for (int i = 0; i < praises.size(); i++) {
                        if (names.equals("")) {
                            names = praises.get(i).getName();
                        } else {
                            names = names + "," + praises.get(i).getName();
                        }
                    }
                }
                holder.tv_praise_names.setText(names);
            } else {
                holder.ll_praiseNames.setVisibility(RelativeLayout.GONE);
                holder.iv_praise.setImageResource(R.drawable.icon_praise_blue);
            }
        } else {
            holder.ll_praiseNames.setVisibility(RelativeLayout.GONE);
        }
        // 显示一级评论
        if (workRing != null && workRing.getWorkDiscuss().size() > 0) {
            holder.ll_discussContent.setVisibility(View.VISIBLE);
            holder.lv_first_discuss.setVisibility(View.VISIBLE);
//            holder.lv_first_discuss.setAdapter(new DiscussListAdapter(workRing.getWorkDiscuss(), workRing.getId()));
            // holder.lv_first_discuss.setOnItemClickListener(new
            // OnItemClickListener() {
            //
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view, int
            // position, long id) {
            // WorkDiscussInfo discussInfo = (WorkDiscussInfo)
            // parent.getAdapter().getItem(position);
            // UserInfo user = new UserInfo();
            // user.readData(context);
            // if
            // (!user.getUserId().equals(discussInfo.getWorkBindMemberId()))
            // {
            // discuss.setVisibility(LinearLayout.VISIBLE);
            // write_comment.setFocusableInTouchMode(true);
            // write_comment.requestFocus();
            // InputMethodManager imm = (InputMethodManager)
            // context.getSystemService(Context.INPUT_METHOD_SERVICE);
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            // grade = 2;
            // WorkId = workRing.getWorkId();
            // dicussId = discussInfo.getWorkDiscussId();
            // }
            // }
            // });
            if (workRing != null && workRing.getWorkDiscuss().size() > 5) {
                holder.tv_more_discuss.setVisibility(View.VISIBLE);
                holder.tv_more_discuss.setText("查看全部评论" + workRing.getWorkDiscuss().size() + "条评论");
            }
        } else {
            holder.ll_discussContent.setVisibility(View.GONE);
        }
        holder.iv_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MemberInfo member = new MemberInfo();
                member.setMemberId(workRing.getBindMemberId());
                member.setMemberImg(workRing.getMemberImg());
                member.setMemberName(workRing.getMemberName());
                member.setMemberPosition(workRing.getMemberPostition());
//                Intent intent = new Intent(context, FriendDataActivity.class);
//                intent.putExtra("member", member);
//                startActivityForResult(intent, 1);
            }
        });
        // 点赞与取消点赞
        holder.rl_praise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPraise(workRing.getWorkPraise())) {
//                    delWorkPraise(workRing.getId());
                } else {
//                    workPraise(workRing.getId());
                }

            }
        });
        // 删除
        holder.rl_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                delWork(workRing.getWorkId());

            }
        });
        // holder.lv_first_discuss.setOnClickListener(new OnClickListener()
        // {
        // @Override
        // public void onClick(View v) {
        // discuss.setVisibility(LinearLayout.VISIBLE);
        // write_comment.setFocusableInTouchMode(true);
        // write_comment.requestFocus();
        // InputMethodManager imm = (InputMethodManager)
        // mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        // grade = 1;
        // WorkId = workRing.getWorkId();
        // }
        // });
        // 分享
        holder.rl_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 分享的文章路径
//                String url = NetBaseConstant.NET_BASE_HOST + "/m.php/ntes/special/Matter/WorkMatter/?ids=" + workRing.getWorkId();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);// APP
//                if (workRing.getNewId().equals("0")) {// 普通职业圈
//                    // 分享的图片路径
//                    String imgUrl = "";
//                    if (workRing.getWorkImgs().size() > 0) {
//                        imgUrl = NetBaseConstant.NET_BASE_HOST + "/" + workRing.getWorkImgs().get(0);
//                    } else {
//                        imgUrl = NetBaseConstant.NET_BASE_HOST + "/" + "uploads/WorkImg/b82a9f3c3108058b7cf2ebcb379ec5bd.jpg";
//                    }
//                    new ImagesInformationItemUtils(ProfessionCircleActivity.this).getDialogWork(workRing.getWorkMatter(), workRing.getMemberName(), url, workRing.getWorkMatter(), bitmap,
//                            imgUrl);
//                } else {// 分享资讯
//                    // 分享的图片路径
//                    String imgUrl = "";
//                    if (workRing.getNews().getNewsImg().size() > 0) {
//                        imgUrl = NetBaseConstant.NET_BASE_HOST + "/" + workRing.getNews().getNewsImg().get(0);
//                    } else {
//                        imgUrl = NetBaseConstant.NET_BASE_HOST + "/" + "uploads/WorkImg/b82a9f3c3108058b7cf2ebcb379ec5bd.jpg";
//                    }
//                    new ImagesInformationItemUtils(ProfessionCircleActivity.this).getDialogWork(workRing.getNews().getNewsTitle(), workRing.getMemberName(), url, workRing.getNews()
//                            .getNewsTitle(), bitmap, imgUrl);
//                }
            }
        });
        return convertView;
    }

    private boolean isPraise(ArrayList<LikeBean> praises) {
        UserInfo user = new UserInfo();
        user.readData(context);
        for (int i = 0; i < praises.size(); i++) {
            if (praises.get(i).getUserid().equals(user.getUserId())) {
                return true;
            }
        }
        return false;
    }

    public class ViewHolder {
        RoundImageView iv_head;
        NoScrollListView lv_first_discuss;
        ImageView iv_information;
        LinearLayout ll_praiseNames, ll_discussContent, ll_informatoin;
        NoScrollGridView gridView;
        TextView tv_name, tv_position, tv_tag, tv_content, tv_praise_num, tv_discuss_num, tv_praise_names, tv_more_discuss, tv_information, tv_time;
        RelativeLayout rl_praise, rl_delete, rl_share;
        ImageView iv_praise;
        ImageView iv_isKa;
    }
}
