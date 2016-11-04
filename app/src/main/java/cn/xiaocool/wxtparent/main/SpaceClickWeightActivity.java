package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.WeightAdapter;
import cn.xiaocool.wxtparent.bean.Weight;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.Chart.FancyChart;
import cn.xiaocool.wxtparent.ui.Chart.data.ChartData;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickWeightActivity extends Activity implements View.OnClickListener, NumberPicker.Formatter, NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener {

    private RelativeLayout btn_exit;
    private TextView btn_input_weight;
    private FancyChart chart;
    private Context context;
    private TextView button1, button2;
    public static String input_text;
    private LinearLayout lv_bottom;
    private NumberPicker numberPicker1, numberPicker2;
    private ListView listView;
    private ArrayList<Weight> weights;
    private WeightAdapter adapter;
    private String[] xName = {"半岁", "一岁", "一岁半", "两岁", "两岁半", "三岁"};
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case CommunalInterfaces.INPUT_WEIGHT:
                    JSONObject obj_weight = (JSONObject)msg.obj;
                    try {
                        String status = obj_weight.getString("status");
                        if (status.equals("success")){
                            ToastUtils.ToastShort(context,"录入体重数据成功！");
                            getAllInformation();
                        }
                        else {
                            ToastUtils.ToastShort(context,"录入体重失败，请重试！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 123:
                    showPopupWindow();
                    break;
                case CommunalInterfaces.GETWEIGHT:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj.optJSONArray("data");
                        weights.clear();
                        JSONObject itemObject;
                        for (int i = 0; i < hwArray.length(); i++) {
                            itemObject = hwArray.optJSONObject(i);
                            Weight weight = new Weight();
                            weight.setWeight(itemObject.optString("weight"));
                            weight.setLogdate(itemObject.optString("log_date"));
                            weights.add(weight);
                        }
                        setChartData();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new WeightAdapter(context, weights);
                            listView.setAdapter(adapter);
                        }
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_click_weight);
        context = this;
        initView();
        weights = new ArrayList<>();
        int type = getIntent().getIntExtra("type_weight", 0);
        if (type == 0) {
            handler.sendEmptyMessageDelayed(123,200);
        } else {
            ToastUtils.ToastShort(context, "您今天已经记录过体重了");
        }
        getAllInformation();
    }

    private void getAllInformation() {
        new SpaceRequest(context,handler).getWeight();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showPopupWindow() {
        View layout = LayoutInflater.from(this).inflate(R.layout.select_weight_popview, null);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(lv_bottom, Gravity.BOTTOM, 0, 0);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        numberPicker1 = (NumberPicker) layout.findViewById(R.id.picker1);
        numberPicker2 = (NumberPicker) layout.findViewById(R.id.picker2);
        numberPicker1.setFormatter(this);
        numberPicker1.setOnValueChangedListener(this);
        numberPicker1.setOnScrollListener(this);
        numberPicker1.setMaxValue(99);
        numberPicker1.setMinValue(1);
        numberPicker1.setValue(10);
        numberPicker2.setOnValueChangedListener(this);
        numberPicker2.setMaxValue(9);
        numberPicker2.setMinValue(0);
        numberPicker2.setValue(0);
        button1 = (TextView) layout.findViewById(R.id.btn_confirm);
        button2 = (TextView) layout.findViewById(R.id.btn_cancel);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_text = numberPicker1.getValue() + "." + numberPicker2.getValue();
                new SpaceRequest(context,handler).InputWeight();
                popupWindow.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.weight_list);
        btn_exit = (RelativeLayout) findViewById(R.id.up_jiantou);
        btn_exit.setOnClickListener(this);
        btn_input_weight = (TextView) findViewById(R.id.btn_input_weight);
        btn_input_weight.setOnClickListener(this);
        chart = (FancyChart) findViewById(R.id.chart);
        setChartData();
        lv_bottom = (LinearLayout) findViewById(R.id.lv_bottom);
    }

    private void setChartData() {
        /*ChartData data = new ChartData(ChartData.LINE_COLOR_RED);
        int k = weights.size()>10?10:weights.size();
        int[] yValues = new int[]{7, 13, 15, 19, 25};
        for (int i = 0; i < k; i++) {
            data.addXValue(i + 1, weights.get(i).getLogdate());
        }
        for (int i = 0; i < 5; i++) {
            data.addYValue((i + 1) * 5, (i + 1) * 5 + "");
        }
        for (int i = 0; i < k; i++) {
            data.addPoint(i + 1, Integer.parseInt(weights.get(i).getWeight()));
        }
        chart.addData(data);*/
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);
        int[] yValues = new int[]{7, 13, 15, 19, 25};
        for (int i = 0; i < 6; i++) {
            data.addXValue(i + 1, xName[i]);
        }
        for (int i = 0; i < 6; i++) {
            data.addYValue((i + 1) * 5, (i + 1) * 5 + "");
        }
        for (int i = 0; i < 5; i++) {
            data.addPoint(i + 1, yValues[i]);
        }
        chart.addData(data);
        ChartData data1 = new ChartData(ChartData.LINE_COLOR_BLUE);
        int[] yValues1 = new int[]{8, 12, 17, 18, 26};
        for (int i = 0; i < 5; i++) {
            data1.addPoint(i + 1, yValues1[i]);
        }
        chart.addData(data1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.btn_input_weight:
                int type = getIntent().getIntExtra("type_weight", 0);
                if (type == 0) {
                    showPopupWindow();
                } else {
                    ToastUtils.ToastShort(context, "您今天已经记录过体重了");
                }
                break;
        }


    }


    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        picker.setValue(newVal);
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {

    }
}
