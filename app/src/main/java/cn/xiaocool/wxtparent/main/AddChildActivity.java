package cn.xiaocool.wxtparent.main;

import android.app.AlertDialog;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;

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
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.UserRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.PicassoImageLoader;
import cn.xiaocool.wxtparent.utils.PicassoPauseOnScrollListener;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.view.WxtApplication;

public class AddChildActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_back,rl_name,rl_relation,rl_phone,rl_photo;
    private TextView tv_name,tv_relation,tv_phone,tv_add;
    private ImageView iv_photo;
    private String relation;
    private FunctionConfig functionConfig;
    private UserInfo userInfo;
    private ArrayList<PhotoInfo> mPhotoList = new ArrayList<>();
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private DisplayImageOptions options;
    private ArrayList<String> filepaths;
    private ArrayList<String> picnames=new ArrayList<>();
    private String filepath = "/sdcard/confimimg";
    private String picname = "newpic.jpg";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommunalInterfaces.ADDPARENT:
                    JSONObject obj = (JSONObject) msg.obj;
                    String status = obj.optString("status");
                    if (status.equals(CommunalInterfaces._STATE)) {
                        ToastUtils.ToastShort(context, "添加成功！");
                        showPopWindow();
                    }
                    break;
                case 122:
                    if (msg.obj!=null){
                        JSONObject obj1 = (JSONObject) msg.obj;
                        if (obj1.optString("status").equals("success")){
                            new SpaceRequest(context,handler).addParent(tv_name.getText().toString(), tv_relation.getText().toString(), tv_phone.getText().toString(),picname);
                            Log.e("pushhead","ok");
                        }else {
                            ToastUtils.ToastShort(context,"图片上传失败！");
                            Log.e("pushhead","not");
                        }
                    }

                    break;
            }
        }
    };

    private void showPopWindow() {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);    // 系统默认Dialog没有输入框

        // 获取自定义的布局
        View alertDialogView = View.inflate(context, R.layout.dialog_add_parent, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        TextView tv_number = (TextView) alertDialogView.findViewById(R.id.tv_number);
        TextView tv_password = (TextView) alertDialogView.findViewById(R.id.tv_password);
        TextView tv_weixin = (TextView) alertDialogView.findViewById(R.id.tv_weixin);
        TextView tv_duanxin = (TextView) alertDialogView.findViewById(R.id.tv_duanxin);
        TextView tv_quxiao = (TextView) alertDialogView.findViewById(R.id.tv_quxiao);
        tv_number.setText(tv_phone.getText().toString());
        tv_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微信分享
                shareWX();
                tempDialog.dismiss();
                finish();
            }
        });
        tv_duanxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //短信通知
                Uri uri = Uri.parse("smsto:"+tv_phone.getText().toString().trim());
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.putExtra("sms_body", userInfo.getChildName()+"的"+tv_relation.getText()+"，在智校园中可以关注"
                        +userInfo.getChildName()+"的最新照片！安装应用并使用账号"+tv_phone.getText().toString()+"，密码为：123进行查看。下载地址是http://t.cn/RcbM5Ed");
                startActivity(intent);
                tempDialog.dismiss();
                finish();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
            }
        });
        tempDialog.show();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams layoutParams = tempDialog.getWindow().getAttributes();
        layoutParams.width = width-100;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        tempDialog.getWindow().setAttributes(layoutParams);
    }

    private void shareWX() {
        WXTextObject textObject = new WXTextObject();
        textObject.text = userInfo.getChildName()+"的"+tv_relation.getText()+"，在智校园中可以关注"
                +userInfo.getChildName()+"的最新照片！安装应用并使用账号"+tv_phone.getText().toString()+"，密码为：123进行查看。下载地址是http://t.cn/RcbM5Ed";
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = "微校通家长端";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "weixin";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        WxtApplication wxtApplication = WxtApplication.getInstance();
        wxtApplication.api.sendReq(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_child);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_name.setOnClickListener(this);
        rl_relation = (RelativeLayout) findViewById(R.id.rl_relation);
        rl_relation.setOnClickListener(this);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_relation = (TextView) findViewById(R.id.tv_relation);
        tv_add = (TextView) findViewById(R.id.add_parent);
        tv_add.setOnClickListener(this);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_photo);
        rl_phone.setOnClickListener(this);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == 1) {
            //得到新Activity 关闭后返回的数据
            relation = data.getExtras().getString("relation");
            tv_relation.setText(relation);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.rl_name:
                showDialog(tv_name);
                break;
            case R.id.rl_relation:
                Intent intent = new Intent(getApplicationContext(), SelectRelationActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_phone:
                showDialog(tv_phone);
                break;
            case R.id.add_parent:
                if(tv_name.getText().length()==0){
                    ToastUtils.ToastShort(context,"家长姓名不能为空！");
                }else if(tv_relation.getText().length()==0){
                    ToastUtils.ToastShort(context,"请选择与宝宝的关系！");
                }else if(tv_phone.getText().length()==0){
                    ToastUtils.ToastShort(context,"请输入手机号!");
                }else{
                    if(picname.equals("newpic.jpg")){
                        new SpaceRequest(context,handler).addParent(tv_name.getText().toString(), tv_relation.getText().toString(), tv_phone.getText().toString(),"1");
                    }else{
                        redsetHead();
                    }
                }
                break;
            case R.id.rl_photo:
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
                ImageLoader.getInstance().displayImage("file:/" + mPhotoList.get(0).getPhotoPath(), iv_photo, options);
                Log.e("mPhotoList", mPhotoList.toString());
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
            filepaths.add(imagefile.getPath());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showDialog(final TextView tv) {

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
                    tv.setText(et_dialog_confirmphoneguardpswd.getText().toString());
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
}
