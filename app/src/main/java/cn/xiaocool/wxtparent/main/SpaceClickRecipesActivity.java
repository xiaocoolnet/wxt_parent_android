package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.RecipesAdapter;
import cn.xiaocool.wxtparent.bean.RecipeInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.DateUtils;


/**
 * Created by wzh on 2016/3/27.
 */
public class SpaceClickRecipesActivity extends BaseActivity implements View.OnClickListener {
    private ImageView btn_exit;
    private ListView recipesView;
    private RecipesAdapter recipesAdapter;
    private ArrayList<RecipeInfo> recipeDatas,recipeInfoArrayList;
    private Context context;
    private View location_pop;
    private RelativeLayout iv_left, iv_right;
    private String begindate, enddate;
    private TextView tv_date;
    private Date date;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.RECIPESNEW:
                    if (((JSONObject) msg.obj).optJSONArray("data").length()>0) {
                        JSONObject obj = (JSONObject) msg.obj;
                        JSONArray hwArray = obj.optJSONArray("data");
                        recipeDatas.clear();
                        recipeDatas = new ArrayList<>();
                        String str = hwArray.optJSONObject(0).optString("date") + "000";
                        Long t1 = Long.parseLong(str);
                        Long t2 = DateUtils.currentWeekMonday(new Date(t1)).getTime() / 1000;
                        Log.e("str----", t2.toString());
                        RecipeInfo recipeInfo1 = new RecipeInfo();
                        RecipeInfo recipeInfo2 = new RecipeInfo();
                        RecipeInfo recipeInfo3 = new RecipeInfo();
                        RecipeInfo recipeInfo4 = new RecipeInfo();
                        RecipeInfo recipeInfo5 = new RecipeInfo();
                        RecipeInfo recipeInfo6 = new RecipeInfo();
                        RecipeInfo recipeInfo7 = new RecipeInfo();
                        for (int i = 0; i < hwArray.length(); i++) {
                            JSONObject object = hwArray.optJSONObject(i);
                            if (object.optString("date").equals(t2.toString())) {
                                recipeInfo1.setWeek("星期一");
                                RecipeInfo.RecipeData data = new RecipeInfo.RecipeData();
                                data.setContent(object.optString("content"));
                                data.setTitle(object.optString("title"));
                                data.setPhoto(object.optString("photo"));
                                recipeInfo1.getData().add(data);
                            }
                            if (object.optString("date").equals(String.valueOf(t2 + 86400))) {
                                recipeInfo2.setWeek("星期二");
                                RecipeInfo.RecipeData data = new RecipeInfo.RecipeData();
                                data.setContent(object.optString("content"));
                                data.setTitle(object.optString("title"));
                                data.setPhoto(object.optString("photo"));
                                recipeInfo2.getData().add(data);
                            }
                            if (object.optString("date").equals(String.valueOf(t2 + 86400 * 2))) {
                                recipeInfo3.setWeek("星期三");
                                RecipeInfo.RecipeData data = new RecipeInfo.RecipeData();
                                data.setContent(object.optString("content"));
                                data.setTitle(object.optString("title"));
                                data.setPhoto(object.optString("photo"));
                                recipeInfo3.getData().add(data);
                            }
                            if (object.optString("date").equals(String.valueOf(t2 + 86400 * 3))) {
                                recipeInfo4.setWeek("星期四");
                                RecipeInfo.RecipeData data = new RecipeInfo.RecipeData();
                                data.setContent(object.optString("content"));
                                data.setTitle(object.optString("title"));
                                data.setPhoto(object.optString("photo"));
                                recipeInfo4.getData().add(data);
                            }
                            if (object.optString("date").equals(String.valueOf(t2 + 86400 * 4))) {
                                recipeInfo5.setWeek("星期五");
                                RecipeInfo.RecipeData data = new RecipeInfo.RecipeData();
                                data.setContent(object.optString("content"));
                                data.setTitle(object.optString("title"));
                                data.setPhoto(object.optString("photo"));
                                recipeInfo5.getData().add(data);
                            }
                            if (object.optString("date").equals(String.valueOf(t2 + 86400 * 5))) {
                                recipeInfo6.setWeek("星期六");
                                RecipeInfo.RecipeData data = new RecipeInfo.RecipeData();
                                data.setContent(object.optString("content"));
                                data.setTitle(object.optString("title"));
                                data.setPhoto(object.optString("photo"));
                                recipeInfo6.getData().add(data);
                            }
                            if (object.optString("date").equals(String.valueOf(t2 + 86400 * 6))) {
                                recipeInfo7.setWeek("星期日");
                                RecipeInfo.RecipeData data = new RecipeInfo.RecipeData();
                                data.setContent(object.optString("content"));
                                data.setTitle(object.optString("title"));
                                data.setPhoto(object.optString("photo"));
                                recipeInfo7.getData().add(data);
                            }
                        }
                        recipeDatas.add(recipeInfo1);
                        recipeDatas.add(recipeInfo2);
                        recipeDatas.add(recipeInfo3);
                        recipeDatas.add(recipeInfo4);
                        recipeDatas.add(recipeInfo5);
                        recipeDatas.add(recipeInfo6);
                        recipeDatas.add(recipeInfo7);
                        recipeInfoArrayList = new ArrayList<>();
                        for (int i = 0; i < recipeDatas.size(); i++) {
                            if (recipeDatas.get(i).getData().size() > 0) {
                                recipeInfoArrayList.add(recipeDatas.get(i));
                            }
                        }
                        recipesAdapter = new RecipesAdapter(recipeInfoArrayList, SpaceClickRecipesActivity.this);
                        recipesView.setAdapter(recipesAdapter);
                    }else{
                        recipeDatas.clear();
                        recipesAdapter = new RecipesAdapter(recipeDatas, SpaceClickRecipesActivity.this);
                        recipesView.setAdapter(recipesAdapter);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_recipes_click);
        context = this;
        initView();
        showData();
        //发起网络请求
        //new NewsRequest(this, handler).recipes();
        getRecipes();
        //设置适配器
//        mondayAdapter = new SpaceRecipesAdapter(this,mondayList);
//        mondayView.setAdapter(mondayAdapter);
//        tuesdayAdapter = new SpaceRecipesAdapter(this,tuesdayList);
//        tuesdayView.setAdapter(tuesdayAdapter);
//        wednesdayAdapter = new SpaceRecipesAdapter(this,wednesdayList);
//        wednesdayView.setAdapter(wednesdayAdapter);
//        thursdayAdapter = new SpaceRecipesAdapter(this,thursdayList);
//        thursdayView.setAdapter(thursdayAdapter);
//        fridayAdapter = new SpaceRecipesAdapter(this,fridayList);
//        fridayView.setAdapter(fridayAdapter);
        //设置ListView点击事件
        //setOnClick();
    }

    private void showData() {

    }

    private void initView() {
        //初始化ListView组件
        tv_date = (TextView) findViewById(R.id.recipes_tv_date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        begindate = String.valueOf(DateUtils.lastDayWholePointDate(DateUtils.currentWeekMonday(date)).getTime() / 1000);
        Log.e("hello---------", begindate);
        enddate = String.valueOf(DateUtils.nextDayWholePointDate(DateUtils.currentWeekSunday(date)).getTime() / 1000);
        tv_date.setText(df.format(date));
        iv_left = (RelativeLayout) findViewById(R.id.recipes_iv_left);
        iv_left.setOnClickListener(this);
        iv_right = (RelativeLayout) findViewById(R.id.recipes_iv_right);
        iv_right.setOnClickListener(this);
        recipeDatas = new ArrayList<>();
        location_pop = findViewById(R.id.location_pop);
        recipesView = (ListView) findViewById(R.id.space_recipes_click_list);
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            case R.id.recipes_iv_left:
                //上一周
                date = DateUtils.lastWeekMonday(date);
                begindate = String.valueOf(DateUtils.lastDayWholePointDate(DateUtils.currentWeekMonday(date)).getTime() / 1000);
                enddate = String.valueOf(DateUtils.nextDayWholePointDate(DateUtils.currentWeekSunday(date)).getTime() / 1000);
                getRecipes();
                tv_date.setText(df.format(date));
                break;
            case R.id.recipes_iv_right:
                //下一周
                date = DateUtils.nextWeekMonday(date);
                begindate = String.valueOf(DateUtils.lastDayWholePointDate(DateUtils.currentWeekMonday(date)).getTime() / 1000);
                enddate = String.valueOf(DateUtils.nextDayWholePointDate(DateUtils.currentWeekSunday(date)).getTime() / 1000);
                getRecipes();
                tv_date.setText(df.format(date));
                break;
        }
    }


    private void getRecipes() {
        new SpaceRequest(this, handler).getRecipes(begindate, enddate);
    }


}
