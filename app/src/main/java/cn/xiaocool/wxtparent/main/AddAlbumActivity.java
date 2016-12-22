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
import android.widget.ImageView;
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
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.PicassoImageLoader;
import cn.xiaocool.wxtparent.utils.PicassoPauseOnScrollListener;
import cn.xiaocool.wxtparent.utils.StringUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.utils.pushimage.PushImage;
import cn.xiaocool.wxtparent.utils.pushimage.PushImageUtil;

/**
 * Created by Administrator on 2016/5/11.
 */
public class AddAlbumActivity extends BaseActivity implements View.OnClickListener {
    private ImageView btn_exit;
    private EditText homework_content;
    private Intent intent;
    private TextView homework_send;
    private GridView homework_pic_grid;


    private static final int ADD_KEY = 4;
    private LocalImgGridAdapter localImgGridAdapter;
    private Context mContext;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<String> mPhototNames;
    private String pushImgName;
    private KProgressHUD hud;
    private FunctionConfig functionConfig;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_KEY:
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
                    mPhotoList.remove((int) msg.obj);
                    localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext,handler);
                    homework_pic_grid.setAdapter(localImgGridAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_album);
        mContext = this;
        initView();
    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        homework_content = (EditText) findViewById(R.id.homework_content);
        // homework_receiveList = (TextView) findViewById(R.id.homework_receiveList);
        homework_pic_grid = (GridView) findViewById(R.id.homework_pic_grid);
        homework_send = (TextView) findViewById(R.id.homework_send);
        homework_send.setOnClickListener(this);
        intent = getIntent();
        String classid = intent.getStringExtra("classID");
        String classname = intent.getStringExtra("className");
        String type = intent.getStringExtra("type");
        //homework_receiveList.setText("接收人:  " + classname);
        mPhotoList = new ArrayList<>();
        mPhototNames = new ArrayList<>();
        //添加图片按钮
        localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext,handler);
        homework_pic_grid.setAdapter(localImgGridAdapter);
        homework_pic_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", String.valueOf(position));
                if (position == mPhotoList.size()) {
                    showActionSheet();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            case R.id.homework_send:


                sendContent();

                break;
        }

    }

    private void sendContent() {

        if (homework_content.getText().length() < 1) {
            ToastUtils.ToastShort(mContext, "发送内容不能为空");
        } else {
            if (NetUtil.isConnnected(this)) {
                hud = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true);
                hud.show();
                if (mPhotoList.size() < 1) {
                    pushImgName = "";
                    new SpaceRequest(mContext, handler).send_trend(homework_content.getText().toString(), pushImgName, "3", ADD_KEY);
                } else {

                    new PushImageUtil().setPushIamge(mContext, mPhotoList, mPhototNames, new PushImage() {
                        @Override
                        public void success(boolean state) {
                            pushImgName = StringUtils.listToString(mPhototNames, ",");
                            new SpaceRequest(mContext, handler).send_trend(homework_content.getText().toString(), pushImgName, "3", ADD_KEY);
                        }

                        @Override
                        public void error() {
                            ToastUtils.ToastShort(mContext,"图片上传失败！");
                        }
                    });
                }



            } else {
                ToastUtils.ToastShort(mContext, "网络请求失败");
            }
        }
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
        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
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

    private boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private boolean hasPermission(String permission){

        if(canMakeSmores()){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);
            }

        }

        return true;

    }


    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted){
                    //授权成功之后，调用系统相机进行拍照操作等
                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);

                }else{
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtils.ToastShort(this,"已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }

    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, mContext,handler);
                homework_pic_grid.setAdapter(localImgGridAdapter);
                Log.e("mPhotoList", mPhotoList.toString());

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
}
