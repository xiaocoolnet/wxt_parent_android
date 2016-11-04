package cn.xiaocool.wxtparent.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.app.ExitApplication;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.net.UserRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.UserNetConstant;

public class Annou_pinglun_add extends BaseActivity implements View.OnClickListener  {

    private String TAG = "ReleaseActivity";
    private Context mContext;
    private static final int ADD_KEY = 4;
    private static final int ADD_IMG_KEY = 5;
    private static final int REQUEST_IMAGE = 2;
    private int responsecode = 0;
    private ProgressDialog proDialog;
    private int count = 0;
    private static final String REQUESTURL = UserNetConstant.NET_USER_UPLOAD_HEAD_IMG;
    private UserInfo user;
    private ArrayList<String> mSelectPath;
    private ImageView iv_addphoto;
    private String picname = "";
    private String filepath = "/sdcard/myheader";
    private String head = null;
    private String gender = null;
    private String type;
    //发布的内容
    private String contents = null;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁

    //
    private RelativeLayout re1;
    private LinearLayout lin1;
    private EditText ed_content;

    private Intent intent;
    private String zy_id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_IMG_KEY:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals("success")) {
                        new SpaceRequest(getApplicationContext(), handler).send_pinglun(type, zy_id, contents, picname, ADD_KEY);
                    }
                    break;
                case ADD_KEY:
                    JSONObject obj2 = (JSONObject) msg.obj;
                    if (obj2.optString("status").equals("success")) {
                        finish();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annou_pinglun_add);
        Log.e("tttt", "Annou_pinglun_add");

        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        user = new UserInfo(mContext);
        initView();


    }



    private void initView() {
        intent=getIntent();
        zy_id=intent.getStringExtra("refid");
        type=intent.getStringExtra("type");



        re1 = (RelativeLayout) findViewById(R.id.up_jiantou);
        lin1 = (LinearLayout) findViewById(R.id.linear_send);
        re1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contents = ed_content.getText().toString();
                if (head != null) {
                    new UserRequest(getApplicationContext(), handler).pushImg(head, ADD_IMG_KEY);
                } else {
                    new SpaceRequest(getApplicationContext(), handler).send_pinglun(type,zy_id,contents, picname, ADD_KEY);
                }
            }
        });


        ed_content = (EditText) findViewById(R.id.et_content);
        contents = ed_content.getText().toString();


        iv_addphoto = (ImageView) findViewById(R.id.iv_add_image);
        iv_addphoto.setOnClickListener(this);


    }


    protected void ShowPickDialog() {
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, PHOTO_REQUEST_ALBUM);

            }
        }).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file = new File(path, "newpic.jpg");
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }

                startActivityForResult(intentFromCapture, PHOTO_REQUEST_CAMERA);
            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:// 相册
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "newpic.jpg");
                        startPhotoZoom(Uri.fromFile(tempFile));
                        getImageToView(data);
                    } else {
                        Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PHOTO_REQUEST_ALBUM:// 图库
                    startPhotoZoom(data.getData());
                    getImageToView(data);
                    break;

                case PHOTO_REQUEST_CUT: // 图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            iv_addphoto.setImageDrawable(drawable);
            picname = "avatar" + user.getUserId() + String.valueOf(new Date().getTime()) + ".jpg";
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
            head = imagefile.getPath();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.iv_add_image:
                ShowPickDialog();
                break;

            case R.id.linear_send:
                contents = ed_content.getText().toString();
                if (head != null) {
                    new UserRequest(this, handler).pushImg(head, ADD_IMG_KEY);
                } else {
                    new SpaceRequest(getApplicationContext(), handler).send_pinglun(type,zy_id,contents, picname, ADD_KEY);
                }

                break;
        }
    }
}