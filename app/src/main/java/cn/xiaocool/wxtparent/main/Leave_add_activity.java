package cn.xiaocool.wxtparent.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.LocalImgGridAdapter;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.UserRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.PicassoImageLoader;
import cn.xiaocool.wxtparent.utils.PicassoPauseOnScrollListener;

public class Leave_add_activity extends FragmentActivity {
    private RelativeLayout up_jiantou, re_dzr, re_jsr, re_kssj, re_jssj, re_qjlb, re_send;
    private TextView tv_dingzhuren, tv_jieshouren, tv_kssj, tv_jssj,tv_qjlb;
    private String begintime, endtime, parentid, reason, studentid, teacherid;
    private EditText ed_reason;
    private Context mContext;
    private UserInfo user;
    private String userId,type;
    private GridView add_pic_gridview;
    private FunctionConfig functionConfig;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private LocalImgGridAdapter localImgGridAdapter;
    private ArrayList<PhotoInfo> photoInfos;
    private String filepath = "/sdcard/OnlineLeave";
    private static final int ADD_IMG_KEY1 = 101;
    private static final int ADD_IMG_KEY2 = 102;
    private static final int ADD_IMG_KEY3 = 103;
    private static final int ADD_IMG_KEY4 = 104;
    private static final int ADD_IMG_KEY5 = 105;
    private static final int ADD_IMG_KEY6 = 106;
    private static final int ADD_IMG_KEY7 = 107;
    private static final int ADD_IMG_KEY8 = 108;
    private static final int ADD_IMG_KEY9 = 109;
    private ArrayList<String> filepaths;
    private ArrayList<String> picnames;
    private String pics="";
    private int imgFlag;
    private KProgressHUD hud;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ADD_IMG_KEY1:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            imgFlag = 1;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY2);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败1" + obj.optString("data"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY2:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {

                            imgFlag = 2;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY3);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败2", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY3:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {

                            imgFlag = 3;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY4);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败3", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY4:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {

                            imgFlag = 4;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY5);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败4", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY5:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {

                            imgFlag = 5;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY6);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败5", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY6:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {

                            imgFlag = 6;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY7);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败6", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY7:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {

                            imgFlag = 7;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY8);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败7", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY8:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            imgFlag = 8;
                            if (imgFlag < filepaths.size()) {
                                new UserRequest(mContext, handler).pushImg(filepaths.get(imgFlag), ADD_IMG_KEY9);
                            } else {
                                send();
                            }
                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败8", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY9:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {

                            send();

                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败9", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case CommunalInterfaces.LEAVE_ADD:
                    JSONObject obj2 = (JSONObject) msg.obj;
                    if (obj2.optString("status").equals("success")) {
                        Log.e("reason", "finish");
                        hud.dismiss();
                        finish();
                    }
                    break;
                case 5231:
                    photoInfos.remove((int)msg.obj);
                    localImgGridAdapter = new LocalImgGridAdapter(photoInfos, mContext,handler);
                    add_pic_gridview.setAdapter(localImgGridAdapter);
                    filepaths.remove((int) msg.obj);
                    picnames.remove((int)msg.obj);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_student);
        mContext = this;
        user = new UserInfo(mContext);
        user.readData(this);
        userId = user.getUserId();
        init();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null && requestCode == 1) {
            String name = data.getExtras().getString("name");//得到新Activity 关闭后返回的数据
            tv_dingzhuren.setText(name);
            studentid = data.getExtras().getString("studentid");
            Log.e("yyy1", name);
            Log.e("yyy1", studentid);
            Log.e("yyy1", "requestCode" + requestCode + "resultCode" + resultCode);
        }

        if (data != null && requestCode == 2) {
            String name = data.getExtras().getString("name");//得到新Activity 关闭后返回的数据
            tv_jieshouren.setText(name);
            teacherid = data.getExtras().getString("teacherid");
            Log.i("yyy2", name);
            Log.i("yyy2", teacherid);
            Log.e("yyy2", "requestCode" + requestCode + "resultCode" + resultCode);

        }


    }
    private void init() {
        picnames = new ArrayList<>();
        filepaths = new ArrayList<>();
        photoInfos = new ArrayList<>();
        add_pic_gridview = (GridView) findViewById(R.id.add_gridview);
        localImgGridAdapter = new LocalImgGridAdapter(photoInfos, mContext,handler);
        add_pic_gridview.setAdapter(localImgGridAdapter);
        add_pic_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == photoInfos.size()) {
                    showActionSheet();
                }
            }
        });
        re_dzr = (RelativeLayout) findViewById(R.id.re_dzr);
        re_jsr = (RelativeLayout) findViewById(R.id.re_jsr);
        re_kssj = (RelativeLayout) findViewById(R.id.re_kssj);
        re_jssj = (RelativeLayout) findViewById(R.id.re_jssj);
        re_qjlb = (RelativeLayout) findViewById(R.id.re_qjlb);
        tv_qjlb = (TextView) findViewById(R.id.tv_qjlb);
        re_send = (RelativeLayout) findViewById(R.id.re_send);
        ed_reason = (EditText) findViewById(R.id.ed_reason);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        tv_dingzhuren = (TextView) findViewById(R.id.tv_dingzhuren);
        tv_jieshouren = (TextView) findViewById(R.id.tv_jieshouren);
        tv_kssj = (TextView) findViewById(R.id.tv_kssj);
        tv_jssj = (TextView) findViewById(R.id.tv_jssj);

        parentid = userId;
        //返回
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //叮嘱人
        re_dzr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Leave_stu_leibiao.class);
                startActivityForResult(intent, 1);
            }
        });
        //接收人
        re_jsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentid == null || studentid.equals("")) {
                    Toast.makeText(getApplicationContext(), "请选择孩子", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(mContext, Leave_tea_liebiao.class);
                    intent.putExtra("childId",studentid);
                    startActivityForResult(intent, 2);
                }
            }
        });

        //开始时间
        re_kssj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                Date myData = new Date();
                cal.setTime(myData);

                //获取系统的时间
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                final int hour = cal.get(Calendar.HOUR_OF_DAY);
                final int minute = cal.get(Calendar.MINUTE);
                final int second = cal.get(Calendar.SECOND);

                Log.e("MONTH", "year" + year);
                Log.e("MONTH", "month" + month);
                Log.e("MONTH", "day" + day);
                Log.e("MONTH", "hour" + hour);
                Log.e("MONTH", "minute" + minute);
                Log.e("MONTH", "second" + second);

                DatePickerDialog dlg = new DatePickerDialog(Leave_add_activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.e("MONTH", "monthOfYear" + monthOfYear);
                        monthOfYear += 1;//monthOfYear 从0开始

                        String data = year + "-" + monthOfYear + "-" + dayOfMonth;
                        tv_kssj.setText(data);
//                        String data_new = dataOne(data + "-" + hour + "-" + minute + "-" + second);

                        //时分秒用0代替
                        String data_new = dataOne(data + "-" + hour + "-" + minute + "-" + second);
                        Log.e("--444444---", data_new);
                        begintime = data_new;

                    }
                }, year, month, day);
                dlg.show();

            }
        });
        //结束时间
        re_jssj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                Date myData = new Date();
                cal.setTime(myData);
                final int hour = cal.get(Calendar.HOUR_OF_DAY);
                final int minute = cal.get(Calendar.MINUTE);
                final int second = cal.get(Calendar.SECOND);

                //获取系统的时间
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dlg = new DatePickerDialog(Leave_add_activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.e("MONTH", "monthOfYear" + monthOfYear);
                        monthOfYear += 1;//monthOfYear 从0开始

                        String data = year + "-" + monthOfYear + "-" + dayOfMonth;
                        tv_jssj.setText(data);
                        String data_new = dataOne(data + "-" + hour + "-" + minute + "-" + second);
                        Log.e("--444444--2---", data_new);
                        endtime = data_new;

                    }
                }, year, month, day);
                dlg.show();


            }
        });
        //请假类别
        re_qjlb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSexDialog();
            }
        });


        re_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = ed_reason.getText().toString();
                type = tv_qjlb.getText().toString();
                if (studentid == null || studentid.equals("")) {
                    Toast.makeText(getApplicationContext(), "请选择孩子", Toast.LENGTH_SHORT).show();
                } else if (teacherid == null || teacherid.equals("")) {
                    Toast.makeText(getApplicationContext(), "请选择老师", Toast.LENGTH_SHORT).show();
                } else if (begintime == null || begintime.equals("")) {
                    Toast.makeText(getApplicationContext(), "开始时间不可为空", Toast.LENGTH_SHORT).show();
                } else if (endtime == null || endtime.equals("")) {
                    Toast.makeText(getApplicationContext(), "结束时间不可为空", Toast.LENGTH_SHORT).show();
                } else if (reason == null || reason.equals("")) {
                    Toast.makeText(getApplicationContext(), "请假理由不可为空", Toast.LENGTH_SHORT).show();
                } else if (type == null || type.equals("")) {
                    Toast.makeText(getApplicationContext(), "请选择请假类别", Toast.LENGTH_SHORT).show();
                }else {
                    hud = KProgressHUD.create(Leave_add_activity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(true);
                    hud.show();
                    if (filepaths.size()>0){
                        for(int i=0;i<picnames.size();i++){
                            pics = pics+picnames.get(i)+",";
                        }
                        pics=pics.substring(0,pics.length()-1);
                        new UserRequest(mContext,handler).pushImg(filepaths.get(0),ADD_IMG_KEY1);
                    }else{
                        new SpaceRequest(mContext, handler).add_leave(begintime, endtime, reason, "",teacherid,studentid,type);
                    }

                }

            }
        });

    }
    public void send(){
        new SpaceRequest(mContext, handler).add_leave(begintime, endtime, reason, pics,teacherid,studentid,type);
    }
    private void showActionSheet() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;
        imageLoader = new PicassoImageLoader();
        pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);
        functionConfigBuilder.setMutiSelectMaxSize(9);
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setSelected(photoInfos);//添加过滤集合
        functionConfig = functionConfigBuilder.build();

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#9BE5B4"))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(Color.parseColor("#9BE5B4"))
                .setFabPressedColor(Color.BLUE)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.parseColor("#9BE5B4"))
                .setIconBack(R.drawable.ic_fanhui)
                .setIconRotate(R.mipmap.ic_action_repeat)
                .setIconCrop(R.mipmap.ic_action_crop)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(mContext, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                                break;
                            case 1:

                                //获取拍照权限
                                if (hasPermission("android.permission.CAMERA")) {
                                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    // 判断存储卡是否可以用，可用进行存储
                                    String state = Environment.getExternalStorageState();
                                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                        File file = new File(path, "newpic.png");
                                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                    }
                                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);

                                } else {


                                    String[] perms = {"android.permission.CAMERA"};

                                    int permsRequestCode = 200;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(perms, permsRequestCode);
                                    }

                                }

                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }
    private boolean canMakeSmores() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private boolean hasPermission(String permission) {

        if (canMakeSmores()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }

        }

        return true;

    }
    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                photoInfos.addAll(resultList);
//                localImgGridAdapter.notifyDataSetChanged();
                Bitmap bitmap;
                for (PhotoInfo photoInfo : resultList) {
                    bitmap = BitmapFactory.decodeFile(photoInfo.getPhotoPath(), getBitmapOption(1));
                    getImageToView(bitmap);

                }

                localImgGridAdapter = new LocalImgGridAdapter(photoInfos, mContext,handler);
                add_pic_gridview.setAdapter(localImgGridAdapter);
                Log.e("mPhotoList", photoInfos.toString());

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    /**
     * 保存图片数据
     */
    private void getImageToView(Bitmap photo) {

        if (photo != null) {
            Random random = new Random();
            String picname = "leave" + random.nextInt(1000) + String.valueOf(new Date().getTime()) + ".jpg";
            Log.e("picname", picname);
            picnames.add(picname);
            storeImageToSDCARD(photo, picname, filepath);
        }
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     */
    public void storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imagefile = new File(file, ImageName);
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.e("filepaths",imagefile.getPath());
            filepaths.add(imagefile.getPath());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    protected void ShowSexDialog() {
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("事假", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tv_qjlb.setText("事假");
            }
        }).setPositiveButton("病假", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                tv_qjlb.setText("病假");
            }
        }).show();
    }

}
