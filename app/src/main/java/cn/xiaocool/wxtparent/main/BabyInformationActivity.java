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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.BabyInfo;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.UserRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.ui.PicassoImageLoader;
import cn.xiaocool.wxtparent.ui.SquareLayout;
import cn.xiaocool.wxtparent.utils.DateUtils;
import cn.xiaocool.wxtparent.utils.PicassoPauseOnScrollListener;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class BabyInformationActivity extends FragmentActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_back;
    private LinearLayout ll_baby_pic,ll_mother_pic,ll_father_pic;
    private ImageView iv_baby,iv_family,iv_mother,iv_father;
    private TextView tv_baby_name,tv_baby_sex,tv_baby_class,tv_baby_birth,tv_baby_hobby,tv_baby_address,tv_baby_jiesong;
    private TextView tv_mother_name,tv_mother_job,tv_mother_phone,tv_mother_like;
    private TextView tv_father_name,tv_father_job,tv_father_phone,tv_father_like;
    private SquareLayout squareLayout;
    private ImageView pic_family;
    private BabyInfo babyInfo;
    private ArrayList<String> picnames=new ArrayList<>();
    private String filepath = "/sdcard/confimimg";
    private String picname = "newpic.jpg";
    private FunctionConfig functionConfig;
    private ArrayList<PhotoInfo> mPhotoList = new ArrayList<>();
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private DisplayImageOptions options;
    private String babyId,fatherId,motherId;
    private UserInfo userInfo;
    private ArrayList<String> filepaths;
    private boolean isFather=false,isMother=false;
    private int type;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.BABYINFO:
                    JSONObject object = (JSONObject) msg.obj;
                    if(object.optString("status").equals("success")){
                        JSONObject jsonObject = object.optJSONArray("data").optJSONObject(0);
                        tv_baby_name.setText(jsonObject.optString("name"));
                        if(jsonObject.optString("sex").equals("1")){
                            tv_baby_sex.setText("男");
                        }else {
                            tv_baby_sex.setText("女");
                        }
                        Date date = new Date();
                        date.setTime(Long.parseLong(jsonObject.optString("birthday")) * 1000);
                        tv_baby_birth.setText(new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.lastDayWholePointDate(date)));
                        imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + jsonObject.optString("avatar"), iv_baby);
                        tv_baby_class.setText(jsonObject.optString("classname"));
                        tv_baby_hobby.setText(jsonObject.optString("hobby"));
                        tv_baby_address.setText(jsonObject.optString("address"));
                        tv_baby_jiesong.setText(jsonObject.optString("delivery"));
                        if(!jsonObject.optString("photo").equals("")&&!jsonObject.optString("photo").equals("null")){
                            squareLayout.setVisibility(View.VISIBLE);
                            iv_family.setVisibility(View.GONE);
                            imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + jsonObject.optString("photo"), pic_family);
                            squareLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    type = 4;
                                    showActionSheet();
                                }
                            });
                        }else {
                            squareLayout.setVisibility(View.GONE);
                            iv_family.setVisibility(View.VISIBLE);
                        }
                        JSONArray array = jsonObject.optJSONArray("parentinfo");
                        for(int i=0;i<array.length();i++){
                            if(array.optJSONObject(i).optString("parent_sex").equals("0")){
                                isMother = true;
                                tv_mother_name.setText(array.optJSONObject(i).optString("parent_name"));
                                tv_mother_phone.setText(array.optJSONObject(i).optString("parent_phone"));
                                motherId = array.optJSONObject(i).optString("parentid");
                                imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+array.optJSONObject(i).optString("parent_photo"),iv_mother);
                            }
                            if(array.optJSONObject(i).optString("parent_sex").equals("1")){
                                isFather = true;
                                tv_father_name.setText(array.optJSONObject(i).optString("parent_name"));
                                tv_father_phone.setText(array.optJSONObject(i).optString("parent_phone"));
                                fatherId = array.optJSONObject(i).optString("parentid");
                                imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+array.optJSONObject(i).optString("parent_photo"),iv_father);
                            }
                        }
                        if(isFather){
                            tv_father_job.setText(jsonObject.optString("fatherpro"));
                            tv_father_like.setText(jsonObject.optString("withfather"));
                        }
                        if(isMother){
                            tv_mother_job.setText(jsonObject.optString("motherpro"));
                            tv_mother_like.setText(jsonObject.optString("withmother"));
                        }
                    }
                    break;
                case 122:
                    if (msg.obj!=null){
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals("success")){
                            filepath="/sdcard/confimimg";
                            if(type==1){
                                new SpaceRequest(context,handler).updateAvatar(babyId,picname, 123);
                            }else if(type==2){
                                new SpaceRequest(context,handler).updateAvatar(fatherId,picname, 123);
                            }else if(type==3){
                                new SpaceRequest(context,handler).updateAvatar(motherId,picname, 123);
                            }else if(type==4){
                                new SpaceRequest(context,handler).updateFamily(picname);
                            }
                            Log.e("pushhead","ok");
                        }else {
                            Log.e("pushhead","not");
                        }
                    }
                    break;
                default:
                    if(((JSONObject)msg.obj).optString("status").equals("success")){
                        ToastUtils.ToastShort(context,"修改成功！");
                        getBabyInformation();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_baby_information);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        babyId = userInfo.getChildId();
        initView();
        new SpaceRequest(context,handler).getBabyInformation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBabyInformation();
    }

    private void getBabyInformation() {
        new SpaceRequest(context,handler).getBabyInformation();
    }

    private void initView() {
        babyInfo = new BabyInfo();
        rl_back= (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        ll_baby_pic= (LinearLayout) findViewById(R.id.ll_baby_pic);
        ll_baby_pic.setOnClickListener(this);
        ll_father_pic= (LinearLayout) findViewById(R.id.ll_father_pic);
        ll_father_pic.setOnClickListener(this);
        ll_mother_pic= (LinearLayout) findViewById(R.id.ll_mother_pic);
        ll_mother_pic.setOnClickListener(this);
        iv_family= (ImageView) findViewById(R.id.iv_add_pic);
        iv_family.setOnClickListener(this);
        iv_baby= (ImageView) findViewById(R.id.iv_baby_pic);
        iv_mother= (ImageView) findViewById(R.id.iv_mother_pic);
        iv_father= (ImageView) findViewById(R.id.iv_father_pic);
        tv_baby_address= (TextView) findViewById(R.id.tv_baby_address);
        tv_baby_birth= (TextView) findViewById(R.id.tv_baby_birth);
        tv_baby_class= (TextView) findViewById(R.id.tv_baby_class);
        tv_baby_hobby= (TextView) findViewById(R.id.tv_baby_hobby);
        tv_baby_name= (TextView) findViewById(R.id.tv_baby_name);
        tv_baby_sex= (TextView) findViewById(R.id.tv_baby_sex);
        tv_baby_jiesong = (TextView) findViewById(R.id.tv_baby_jiesong);
        tv_mother_job = (TextView) findViewById(R.id.tv_mother_job);
        tv_mother_like = (TextView) findViewById(R.id.tv_mother_like);
        tv_mother_name = (TextView) findViewById(R.id.tv_mother_name);
        tv_mother_phone = (TextView) findViewById(R.id.tv_mother_phone);
        tv_father_job = (TextView) findViewById(R.id.tv_father_job);
        tv_father_like = (TextView) findViewById(R.id.tv_father_like);
        tv_father_name = (TextView) findViewById(R.id.tv_father_name);
        tv_father_phone = (TextView) findViewById(R.id.tv_father_phone);
        tv_baby_birth.setOnClickListener(this);
        tv_baby_hobby.setOnClickListener(this);
        tv_baby_address.setOnClickListener(this);
        tv_baby_jiesong.setOnClickListener(this);
        tv_mother_job.setOnClickListener(this);
        tv_mother_like.setOnClickListener(this);
        tv_father_job.setOnClickListener(this);
        tv_father_like.setOnClickListener(this);
        squareLayout = (SquareLayout) findViewById(R.id.square);
        pic_family = (ImageView) findViewById(R.id.quanjiafu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.ll_baby_pic:
                //修改宝宝头像
                type = 1;
                showActionSheet();
                break;
            case R.id.ll_mother_pic:
                //修改母亲头像
                if(isMother){
                    type = 3;
                    showActionSheet();
                }else{
                    ToastUtils.ToastShort(context,"您未添加母亲账号");
                }
                break;
            case R.id.ll_father_pic:
                //修改父亲头像
                if(isFather){
                    type = 2;
                    showActionSheet();
                }else{
                    ToastUtils.ToastShort(context,"您未添加父亲账号");
                }
                break;
            case R.id.iv_add_pic:
                //上传全家福
                type = 4;
                showActionSheet();
                break;
            case R.id.tv_baby_birth:
                //宝宝生日
                showDateSelector();
                break;
            case R.id.tv_baby_hobby:
                //宝宝爱好
                showDialog(tv_baby_hobby);
                break;
            case R.id.tv_baby_address:
                //宝宝住址
                showDialog(tv_baby_address);
                break;
            case R.id.tv_baby_jiesong:
                //宝宝接送人
                showDialog(tv_baby_jiesong);
                break;
            case R.id.tv_mother_job:
                //妈妈职业
                if(isMother){
                    showDialog(tv_mother_job);
                }else {
                    ToastUtils.ToastShort(context,"您未添加母亲账号");
                }
                break;
            case R.id.tv_mother_like:
                //喜欢和妈妈做什么
                if(isMother){
                    showDialog(tv_mother_like);
                }else{
                    ToastUtils.ToastShort(context,"您未添加母亲账号");
                }
                break;
            case R.id.tv_father_job:
                //爸爸职业
                if(isFather){
                    showDialog(tv_father_job);
                }else{
                    ToastUtils.ToastShort(context,"您未添加父亲账号");
                }
                break;
            case R.id.tv_father_like:
                //喜欢喝爸爸做什么
                if(isFather){
                    showDialog(tv_father_like);
                }else{
                    ToastUtils.ToastShort(context,"您未添加父亲账号");
                }
                break;
        }
    }

    /**
     * 修改头像
     * */
    private void redsetHead() {

        new UserRequest(context,handler).pushImg(filepath, 122);

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

                iv_baby.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage("file:/" + mPhotoList.get(0).getPhotoPath(), iv_baby, options);
                Log.e("mPhotoList", mPhotoList.toString());
                redsetHead();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
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
            filepaths.clear();
            filepaths.add(imagefile.getPath());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("filepath-----","error");
            e.printStackTrace();
        }
    }
    private void showDialog(final TextView view) {

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
                    switch (view.getId()){
                        case R.id.tv_baby_hobby:
                            new SpaceRequest(context,handler).changeHobby(et_dialog_confirmphoneguardpswd.getText().toString());
                            break;
                        case R.id.tv_baby_address:
                            new SpaceRequest(context,handler).changeAddress(et_dialog_confirmphoneguardpswd.getText().toString());
                            break;
                        case R.id.tv_baby_jiesong:
                            new SpaceRequest(context,handler).changeReliver(et_dialog_confirmphoneguardpswd.getText().toString());
                            break;
                        case R.id.tv_mother_job:
                            new SpaceRequest(context,handler).changeMotherJob(et_dialog_confirmphoneguardpswd.getText().toString());
                            break;
                        case R.id.tv_mother_like:
                            new SpaceRequest(context,handler).changeMotherLike(et_dialog_confirmphoneguardpswd.getText().toString());
                            break;
                        case R.id.tv_father_job:
                            new SpaceRequest(context,handler).changeFatherJob(et_dialog_confirmphoneguardpswd.getText().toString());
                            break;
                        case R.id.tv_father_like:
                            new SpaceRequest(context,handler).changeFatherLike(et_dialog_confirmphoneguardpswd.getText().toString());
                            break;
                    }
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
    public void showDateSelector(){
        final Calendar cal = Calendar.getInstance();
        Date myData = new Date();
        cal.setTime(myData);

        //获取系统的时间
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int minute = cal.get(Calendar.MINUTE);
        final int second = cal.get(Calendar.SECOND);

        Log.e("MONTH", "year" + year);
        Log.e("MONTH", "month" + month);
        Log.e("MONTH", "day" + day);
        Log.e("MONTH", "hour" + hour);
        Log.e("MONTH", "minute" + minute);
        Log.e("MONTH", "second" + second);

        DatePickerDialog dlg = new DatePickerDialog(BabyInformationActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                Log.e("hello",String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth));
                calendar.set(year,monthOfYear,dayOfMonth);
                new SpaceRequest(context,handler).changeBirth(String.valueOf(calendar.getTime().getTime()/1000));
            }
        }, year, month, day);
        dlg.show();
    }

}
