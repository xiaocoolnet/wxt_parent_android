package cn.xiaocool.wxtparent.main;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.DateGridAdapter;
import cn.xiaocool.wxtparent.adapter.TeacherAttendanceAdapter;
import cn.xiaocool.wxtparent.bean.Attendance;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.NoScrollGridView;
import cn.xiaocool.wxtparent.ui.NoScrollListView;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.TimeToolUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * 考勤Activity
 * Created by wzh on 2016/2/19.
 */
public class SpaceClickAttendanceActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout up_jiantou;
    private ImageView btn_exit, last_month, next_month;
    private RelativeLayout last_month_layout, next_month_layout;
    private TextView time, mom, title_bar_name;
    private String mYear, mMonth, mDay, mWay;
    private NoScrollGridView grid_date;
    private NoScrollListView teacher_attend_list;
    private Context context;
    private TextView year_month;
    private int year, month,currentDay;
    private TeacherAttendanceAdapter studentGridListAdapter;
    ArrayList<Attendance> attendances = new ArrayList<>();
    private UserInfo user = new UserInfo();
    private String userid;
    private DateGridAdapter dateGridAdapter;
    private ArrayList<String> dateArray;
    private int[] state;
    private int num;
    private int b;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        ProgressViewUtil.dismiss();
                        Log.d("ChooseCollectActivity", obj.optString("status"));
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            JSONArray dataArray = obj.optJSONArray("data");
                            Log.d("ChooseCollectActivity", obj.optString("data"));
                            JSONObject itemObject;
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Attendance attendance = new Attendance();
                                attendance.setId(itemObject.optString("id"));
                                attendance.setName(itemObject.optString("name"));
                                attendance.setUserid(itemObject.optString("userid"));
                                attendance.setPhoto(itemObject.optString("photo"));
                                attendance.setSchoolid(itemObject.optString("schoolid"));
                                attendance.setArrivetime(itemObject.optString("arrivetime"));
                                attendance.setLeavetime(itemObject.optString("leavetime"));
                                attendance.setArrivepicture(itemObject.optString("arrivepicture"));
                                attendance.setLeavepicture(itemObject.optString("leavepicture"));
                                attendance.setArrivevideo(itemObject.optString("arrivevideo"));
                                attendance.setLeavevideo(itemObject.optString("leavevideo"));
                                attendance.setCreate_time(itemObject.optString("create_time"));
                                String time = itemObject.optString("create_time");
                                String arrive = itemObject.optString("arrivetime");
                                if(!arrive.equals("null")){
                                    state[getDay(time)+b-2]=1;
                                }
                                attendance.setType(itemObject.optString("type"));
                                attendances.add(attendance);
                            }
                            Collections.sort(attendances, new Comparator<Attendance>() {
                                @Override
                                public int compare(Attendance lhs, Attendance rhs) {
                                    return (int) (Long.parseLong(rhs.getCreate_time()) - Long.parseLong(lhs.getCreate_time()));
                                }
                            });

                            Log.e("ChooseCollectActivity", attendances.size() + "");
                            studentGridListAdapter.notifyDataSetChanged();
                            grid_date.setAdapter(new DateGridAdapter(getDateTextArray(year, month), context, state));
                            int m=1;
                            for(int s:state){
                                Log.e("state--"+m, String.valueOf(s));
                                m++;
                            }
                            boolean flag = false;
                            for(int i=0;i<attendances.size();i++){
                                Date date = new Date(Long.parseLong(attendances.get(i).getCreate_time())*1000);
                                Calendar c1 = Calendar.getInstance();
                                Calendar c2 = Calendar.getInstance();
                                c1.setTime(date);
                                if(c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)){
                                    if(new SimpleDateFormat("yyyy-MM-dd").format(date).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
                                        if(!attendances.get(i).getArrivetime().equals("null")){
                                            flag = true;
                                        }
                                    }
                                }else{
                                    flag = true;
                                }

                            }
                            if(!flag){
                                handler.sendEmptyMessageDelayed(123,200);
                            }
                        }else{
                            studentGridListAdapter.notifyDataSetChanged();
                            grid_date.setAdapter(new DateGridAdapter(getDateTextArray(year, month), context, state));
                            if(Calendar.getInstance().get(Calendar.MONTH)+1==month&&Calendar.getInstance().get(Calendar.YEAR)==year){
                                handler.sendEmptyMessageDelayed(123, 200);
                            }
                        }
                        break;

                    }
                case 123:
                    if(SPUtils.get(context,"today","").equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                            &&SPUtils.get(context,"isShow",false).equals(true)){
                        return;
                    }else{
                        showPopupWindow();
                    }
                    break;
                case CommunalInterfaces.RESIGN:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        Log.d("ChooseCollectActivity", obj.optString("status"));
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            ToastUtils.ToastShort(context,"签到成功！");
                            state();
                            getAttendList();
                        }
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_attendance);
        context = this;
        ProgressViewUtil.show(context);
        user.readData(this);
        userid = user.getUserId();
        dateArray = new ArrayList<>();
        initView();
        getdate();
        getAttendList();
    }

    private void initView() {
        attendances = new ArrayList<>();
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        grid_date = (NoScrollGridView) findViewById(R.id.grid_date);
        teacher_attend_list = (NoScrollListView) findViewById(R.id.teacher_attend_list);
        title_bar_name = (TextView) findViewById(R.id.title_bar_name);
        studentGridListAdapter = new TeacherAttendanceAdapter(attendances, SpaceClickAttendanceActivity.this);
        teacher_attend_list.setAdapter(studentGridListAdapter);
    }


    /**
     * 弹出补签对话框
     */
    private void showPopupWindow() {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(context, R.layout.confirm_attendance, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        TextView tv_queding = (TextView) alertDialogView.findViewById(R.id.queding);
        TextView tv_quxiao = (TextView) alertDialogView.findViewById(R.id.quxiao);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SpaceRequest(context, handler).resign();
                tempDialog.dismiss();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                SPUtils.put(context,"today",today);
                SPUtils.put(context,"isShow",true);
                tempDialog.dismiss();
            }
        });
        tempDialog.show();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams layoutParams = tempDialog.getWindow().getAttributes();
        layoutParams.width = width-300;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        tempDialog.getWindow().setAttributes(layoutParams);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取数据
     */
    private void getAttendList() {
        attendances.clear();
        studentGridListAdapter.notifyDataSetChanged();
        long begintime = TimeToolUtils.getMonthBeginTimestamp(year, month) / 1000;
        long endtime = TimeToolUtils.getMonthEndTimestamp(year, month) / 1000;
        new SpaceRequest(this, handler).getTeacherAttendance(String.valueOf(begintime), String.valueOf(endtime), userid, 111);
    }

    private void getdate() {


        //获取当前年月日
        Calendar c = Calendar.getInstance();
        year = getIntent().getIntExtra("year",c.get(Calendar.YEAR));
        month = getIntent().getIntExtra("month", c.get(Calendar.MONTH) + 1);
        state();
        year_month = (TextView) findViewById(R.id.year_month);
        year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
        Log.d("年-月", String.valueOf(year) + String.valueOf(month));
        last_month_layout = (RelativeLayout) findViewById(R.id.last_month_layout);
        last_month_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month - 1;
                if (month == 0) {
                    month = 12;
                    year = year - 1;
                }
                year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
                state();
                grid_date.setAdapter(new DateGridAdapter(getDateTextArray(year, month), context, state));
                getAttendList();

            }
        });
        next_month_layout = (RelativeLayout) findViewById(R.id.next_month_layout);
        next_month_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH)+1;
                if(y==year&&m==month){
                    ToastUtils.ToastShort(context,"无法显示超过当前月的信息");
                }else {
                    month = month + 1;
                    if (month == 13) {
                        month = 1;
                        year = year + 1;
                    }
                    year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
                    state();
                    grid_date.setAdapter(new DateGridAdapter(getDateTextArray(year, month), context, state));
                    getAttendList();
                }
            }
        });
        year_month = (TextView) findViewById(R.id.year_month);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
        }

    }

    /**
     * 获取该月日历所填充的数据
     */
    private ArrayList<String> getDateTextArray(int year, int month) {

        ArrayList arrayList = new ArrayList();
        int datenum;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int a = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("本月第一天是：" + a);

        //判断是否闰年
        if (isRun(year)) {
            //判断月份有多少天
            datenum = getMonthCountForRun(month);
        } else {
            datenum = getMonthCountForNotRun(month);
        }

        for (int i = 0; i < a - 1; i++) {
            arrayList.add(" ");
        }

        for (int i = 0; i < datenum; i++) {
            arrayList.add(String.valueOf(i + 1));
        }


        return arrayList;
    }

    /**
     * 填充考勤状态
     */
    public void state(){
        if (isRun(year)) {
            //判断月份有多少天
            num = getMonthCountForRun(month);
        } else {
            num = getMonthCountForNotRun(month);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        b = calendar.get(Calendar.DAY_OF_WEEK);
        state = new int[num+b-1];
        if(year==Calendar.getInstance().get(Calendar.YEAR)&&month==(Calendar.getInstance().get(Calendar.MONTH)+1)){
            currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            for (int i = currentDay+b-1; i <num+b-1 ; i++) {
                state[i]=2;
            }
        }
    }

    /**
     * 根据时间戳获取当月第几天
     */
    private int getDay(String time){
        long t = Long.parseLong(time)*1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(t));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    /**
     * 获取闰年该月多少天
     */
    private int getMonthCountForNotRun(int month) {

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 2) {
            return 28;
        } else {
            return 30;
        }
    }

    /**
     * 获取平年该月多少天
     */
    private int getMonthCountForRun(int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 2) {
            return 29;
        } else {
            return 30;
        }


    }

    /**
     * 判断是否闰年
     */
    private Boolean isRun(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

}
