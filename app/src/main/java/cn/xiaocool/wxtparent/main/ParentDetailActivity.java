package cn.xiaocool.wxtparent.main;

import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ParentInfo;
import cn.xiaocool.wxtparent.net.UserRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.ui.PicassoImageLoader;
import cn.xiaocool.wxtparent.utils.PicassoPauseOnScrollListener;

public class ParentDetailActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_back,rl_img;
    private ParentInfo parentInfo;
    private TextView tv_name,tv_relation,tv_phone;
    private ImageView iv_photo;
    private ArrayList<String> picnames=new ArrayList<>();
    private String filepath = "/sdcard/confimimg";
    private String picname = "newpic.jpg";
    private FunctionConfig functionConfig;
    private ArrayList<PhotoInfo> mPhotoList = new ArrayList<>();
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 122:
                    if (msg.obj!=null){
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals("success")){
                            filepath="/sdcard/confimimg";
                            new SpaceRequest(context,handler).updateAvatar(parentInfo.getId(),picname, 123);
                            Log.e("pushhead","ok");
                        }else {
                            Log.e("pushhead","not");
                        }
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_detail);
        context = this;
        parentInfo = (ParentInfo) getIntent().getSerializableExtra("parent");
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(parentInfo.getName());
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setText(parentInfo.getPhone());
        tv_relation = (TextView) findViewById(R.id.tv_relation);
        tv_relation.setText(parentInfo.getAppellation());
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+parentInfo.getPhoto(),iv_photo);
        rl_img = (RelativeLayout) findViewById(R.id.rl_img);
        rl_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.rl_img:
                showActionSheet();
                break;
        }
    }

    private void showActionSheet() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;
        imageLoader = new PicassoImageLoader();
        pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);
        functionConfigBuilder.setMutiSelectMaxSize(1);
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        functionConfig = functionConfigBuilder.build();

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#9BE5B4"))
                .setTitleBarTextColor(Color.BLACK)
                .setTitleBarIconColor(Color.BLACK)
                .setFabNornalColor(Color.parseColor("#9BE5B4"))
                .setFabPressedColor(Color.BLUE)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.parseColor("#9BE5B4"))
                .setIconBack(R.drawable.ic_fanhui)
                .setIconRotate(R.mipmap.ic_action_repeat)
                .setIconCrop(R.mipmap.ic_action_crop)
                .setIconCamera(R.mipmap.ic_action_camera)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        ActionSheet.createBuilder(context, getSupportFragmentManager())
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


    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
//                localImgGridAdapter.notifyDataSetChanged();
                Bitmap bitmap;
                for (PhotoInfo photoInfo : resultList) {
                    bitmap = BitmapFactory.decodeFile(photoInfo.getPhotoPath(), getBitmapOption(2));
                    getImageToView(bitmap);

                }

                iv_photo.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage("file:/" + mPhotoList.get(0).getPhotoPath(), iv_photo);
                Log.e("mPhotoList", mPhotoList.toString());
                redsetHead();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 修改头像
     * */
    private void redsetHead() {

        new UserRequest(context,handler).pushImg(filepath, 122);

    }
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
            Random random=new Random();
            picname = "newsgroup" + random.nextInt(1000) + String.valueOf(new Date().getTime()) + ".jpg";
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
            colorImage.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            filepath = imagefile.getPath();
            Log.e("filepath-----",filepath);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("filepath-----","error");
            e.printStackTrace();
        }
    }
}
