package cn.xiaocool.wxtparent.main;

import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.LocalImgGridAdapter;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.UserRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class BabyAlbum_Add_Activity extends BaseActivity implements View.OnClickListener {


    private Intent intent;
    private GridView homework_pic_grid;
    private String filepath = "/sdcard/homeworkimg";
    private String picname = "newpic.jpg";
    private String imagePath;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    private String data = null;
    private static final int ADD_IMG_KEY = 5;
    private static final int ADD_KEY = 4;
    private ArrayList<Drawable> drawables;
    private ArrayList<String> filepaths;
    private ArrayList<String> picnames;
    private String type;
    private LocalImgGridAdapter localImgGridAdapter;
    private RelativeLayout re_fabu, up_jiantou;
    private EditText ed_content;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_IMG_KEY:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            data = obj.optString("data");
                            new SpaceRequest(BabyAlbum_Add_Activity.this,handler).send_trend(ed_content.getText().toString(), picname, ADD_KEY);
                        }
                    }
                    break;
                case ADD_KEY:
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_baby_album__add_);
        init();


    }

    private void init() {
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        re_fabu = (RelativeLayout) findViewById(R.id.re_fabu);
        up_jiantou.setOnClickListener(this);
        re_fabu.setOnClickListener(this);
        ed_content= (EditText) findViewById(R.id.ed_content);


        intent = getIntent();
        String classid = intent.getStringExtra("classID");
        String classname = intent.getStringExtra("className");
        String type = intent.getStringExtra("type");
        //homework_receiveList.setText("接收人:  " + classname);
        drawables = new ArrayList<>();
        filepaths = new ArrayList<>();
        picnames = new ArrayList<>();



        homework_pic_grid = (GridView) findViewById(R.id.img_add_gridview);
        //添加图片按钮
        //localImgGridAdapter = new LocalImgGridAdapter(drawables, BabyAlbum_Add_Activity.this);
        homework_pic_grid.setAdapter(localImgGridAdapter);
        homework_pic_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", String.valueOf(position));
                if (position == drawables.size()) {
                    showPicDialog();
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.up_jiantou:
                finish();
                break;
            case R.id.re_fabu:

                if (drawables.size()>0){
                    for (int i=0; i<filepaths.size();i++){
                        new UserRequest(this,handler).pushImg(filepaths.get(i),ADD_IMG_KEY);
                    }

                }else {
                    String pname = "";
                    for (int i=0; i<picnames.size();i++){
                        pname = pname +picnames.get(i);
                    }
                    new SpaceRequest(BabyAlbum_Add_Activity.this,handler).send_trend(ed_content.getText().toString(),pname,ADD_KEY);
                }
                if (TextUtils.isEmpty(ed_content.toString())||TextUtils.isEmpty(ed_content.toString())){
                    ToastUtils.ToastShort(BabyAlbum_Add_Activity.this, "发送内容不能为空");
                }else {
                    if (NetUtil.isConnnected(this)){
                        Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(this,"网络请求失败",Toast.LENGTH_SHORT).show();
                    }
                }

                break;




        }


    }


    /**
     * 展示dialog
     */

    private void showPicDialog() {
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


    //重写onActivityResult以获得你需要的信息
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
//                case 4:
//                    if (data != null) {
//                        cityId = data.getStringExtra("cityId");
//                        tv_city.setText(data.getStringExtra("city"));
//                        Toast.makeText(getApplicationContext(), data.getStringExtra("city"), Toast.LENGTH_SHORT).show();
//                    }
//                    break;
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
            drawables.add(drawable);
            //LocalImgGridAdapter localImgGridAdapter = new LocalImgGridAdapter(drawables,BabyAlbum_Add_Activity.this);
            homework_pic_grid.setAdapter(localImgGridAdapter);
            picname = "album"+String.valueOf(new Date().getTime())+ ".jpg";
            picnames.add(picname);
            storeImageToSDCARD(photo, picname, filepath);
        }
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     * */
    public void storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imagefile = new File(file, ImageName );
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            imagePath = imagefile.getPath();
            filepaths.add(imagefile.getPath());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
