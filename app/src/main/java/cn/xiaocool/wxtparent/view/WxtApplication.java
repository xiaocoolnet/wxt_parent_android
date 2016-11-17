package cn.xiaocool.wxtparent.view;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EzvizAPI;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import cn.jpush.android.api.JPushInterface;
import cn.xiaocool.wxtparent.bean.Address;
import cn.xiaocool.wxtparent.utils.LogUtils;

/**
 * Created by THB on 2016/7/6.
 */
public class WxtApplication extends Application {
    private static WxtApplication mInstance;
    private Context applicationContext;
    public static String city = "温州";
    public boolean k_bKeyRight = true;
    public static int UID;
    public static int ISCAM;
    public static int C_ID = 0;
    public static String isFrist = "yes";
    public static String TOKEN = "null";
    private ExecutorService mExecutorService;
    private final int CORE_POOL_SIZE = 5;
    private ArrayList<WeakReference<OnLowMemoryListener>> mLowMemoryListeners;
    boolean istoken = true;
    public IWXAPI api; //第三方app与微信通信的openapi接口
    private SQLiteDatabase db;  //数据库对象
    private ArrayList<Address> usersinfos = new ArrayList<>();
    public static final String APP_ID = "wx1929ca4290231712";
    //环信注册

    public static String APP_KEY = "e6a9eb61e34d4b64a6a1d92867914d9c";
    public static String API_URL = "https://open.ys7.com";
    public static String WEB_URL = "https://auth.ys7.com";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        applicationContext = this;
        mInstance = this;
        setWeShare();


        mLowMemoryListeners = new ArrayList<WeakReference<OnLowMemoryListener>>();
        SharedPreferences sp = getSharedPreferences("UserUID", Context.MODE_PRIVATE);
        UID = sp.getInt("UID", 0);
        isFrist = sp.getString("isFrist", "");
        Log.e("hou", "APPlication:UID=" + UID);
        initImageLoader(getApplicationContext());


        //初始化极光
        setPush();
        setEzOpen();
    }

    private void setEzOpen() {

        /**
         * sdk日志开关，正式发布需要去掉
         */
        EZOpenSDK.showSDKLog(true);

        /**
         * 设置是否支持P2P取流,详见api
         */
        EZOpenSDK.enableP2P(true);

        /**
         * APP_KEY请替换成自己申请的
         */
        EZOpenSDK.initLib(this, APP_KEY, "");

        EzvizAPI.getInstance().setServerUrl(API_URL, WEB_URL);
        //此token只是暂时写死，以后需要从服务器获取
        EzvizAPI.getInstance().setAccessToken("at.4yjt0i3p998gk0re7hpq7v4d74lqjmnl-2a1wdrskh3-156772n-bfitxirxx");

    }

    private void setPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }


    private void setWeShare() {
        //微信分享
        api = WXAPIFactory.createWXAPI(this, APP_ID); //初始化api
        api.registerApp(APP_ID); //将APP_ID注册到微信中
    }


    public static WxtApplication getInstance() {
        return mInstance;
    }


    public interface OnLowMemoryListener {

        public void onLowMemoryReceived();
    }


    private final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "GreenDroid thread #"
                    + mCount.getAndIncrement());
        }
    };


    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "wxt_parent/Cache");// 获取到缓存的目录地址
        LogUtils.d("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
                // max width,max height，即保存的每个缓存文件的最大长宽default=device screen dimensions
                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(5)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 1).tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
                // 1024))
                // You can pass your own memory cache
                // .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCache(new WeakMemoryCache())
                // implementation你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024)
                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5加密
                // .discCacheFileNameGenerator(new
                // HashCodeFileNameGenerator())// 将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(1000) // 缓存的File数量
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                // connectTimeout (5s), readTimeout(30s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }
}

