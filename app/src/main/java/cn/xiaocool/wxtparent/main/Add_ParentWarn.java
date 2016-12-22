package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.LocalImgGridAdapter;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.PicassoImageLoader;
import cn.xiaocool.wxtparent.utils.PicassoPauseOnScrollListener;
import cn.xiaocool.wxtparent.utils.StringUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.utils.pushimage.PushImage;
import cn.xiaocool.wxtparent.utils.pushimage.PushImageUtil;

public class Add_ParentWarn extends BaseActivity implements View.OnClickListener {

    private RelativeLayout up_jiantou, re_send, re_dzr, re_jsr;
    private EditText ed_content;
    private TextView tv_dzr, tv_jsr;
    private String reason, studentid, teacherid;

    private Context mContext;
    private FunctionConfig functionConfig;
    private GridView add_pic_gridview;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private LocalImgGridAdapter localImgGridAdapter;
    private ArrayList<PhotoInfo> photoInfos;
    private ArrayList<String> mPhototNames;
    private String pushImgName;
    private KProgressHUD hud;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommunalInterfaces.PARENT_WARN_ADD:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                            hud.dismiss();
                            finish();

                        } else {
                            hud.dismiss();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 5231:
                    photoInfos.remove((int) msg.obj);
                    localImgGridAdapter = new LocalImgGridAdapter(photoInfos, mContext,handler);
                    add_pic_gridview.setAdapter(localImgGridAdapter);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add__parent_warn);
        mContext = this;
        init();
    }

    private void init() {
        mPhototNames = new ArrayList<>();
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
        tv_dzr = (TextView) findViewById(R.id.tv_dzr);
        tv_jsr = (TextView) findViewById(R.id.tv_jsr);
        ed_content = (EditText) findViewById(R.id.ed_content);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        re_send = (RelativeLayout) findViewById(R.id.re_send);
        re_dzr = (RelativeLayout) findViewById(R.id.re_dzr);
        re_jsr = (RelativeLayout) findViewById(R.id.re_jsr);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        re_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reason = ed_content.getText().toString();
                if(studentid == null||studentid.equals("")){
                    Toast.makeText(getApplicationContext(), "请选择孩子", Toast.LENGTH_SHORT).show();
                }else if (teacherid == null || teacherid.equals("")) {
                    Toast.makeText(getApplicationContext(), "请选择老师", Toast.LENGTH_SHORT).show();
                }else if(reason==null||reason.equals("")){
                    Toast.makeText(getApplicationContext(), "请输入理由", Toast.LENGTH_SHORT).show();
                }else {
                    hud = KProgressHUD.create(Add_ParentWarn.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(true);
                    hud.show();
                    if(photoInfos.size()==0){
                        new SpaceRequest(mContext, handler).parentWarn_add(studentid, teacherid, reason, "");
                    }else{
                        new PushImageUtil().setPushIamge(mContext, photoInfos, mPhototNames, new PushImage() {
                            @Override
                            public void success(boolean state) {
                                pushImgName = StringUtils.listToString(mPhototNames, ",");
                                new SpaceRequest(mContext, handler).parentWarn_add(studentid, teacherid, reason, pushImgName);
                            }

                            @Override
                            public void error() {
                                ToastUtils.ToastShort(mContext, "图片上传失败！");
                            }
                        });
                    }
                }
            }
        });
        re_dzr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Leave_stu_leibiao.class);
                startActivityForResult(intent, 1);
            }
        });
        re_jsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentid == null || studentid.equals("")) {
                    Toast.makeText(getApplicationContext(), "请选择孩子", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, Leave_tea_liebiao.class);
                    intent.putExtra("childId", studentid);
                    startActivityForResult(intent, 2);
                }
            }
        });


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
     * 展示dialog
     */

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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null && requestCode == 1) {
            String name = data.getExtras().getString("name");//得到新Activity 关闭后返回的数据
            tv_dzr.setText(name);
            studentid = data.getExtras().getString("studentid");
            Log.e("yyy1", name);
            Log.e("yyy1", studentid);
            Log.e("yyy1", "requestCode" + requestCode + "resultCode" + resultCode);
        }
        if (data != null && requestCode == 2) {
            String name = data.getExtras().getString("name");//得到新Activity 关闭后返回的数据
            tv_jsr.setText(name);
            teacherid = data.getExtras().getString("teacherid");
            Log.i("yyy2", name);
            Log.i("yyy2", teacherid);
            Log.e("yyy2", "requestCode" + requestCode + "resultCode" + resultCode);
        }
    }

    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                photoInfos.clear();
                photoInfos.addAll(resultList);
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

    @Override
    public void onClick(View v) {

    }
}
