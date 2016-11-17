package cn.xiaocool.wxtparent.camera;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.videogo.constant.IntentConsts;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by hzh on 16/11/15.
 */

public class DeviceInfoListActivity extends BaseActivity implements SelectCameraDialog.CameraItemClick{
    private ListView deviceLv;
    private DeviceInfoAdapter adapter;
    private List<EZDeviceInfo> deviceInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info_list);
        deviceInfos = new ArrayList<>();
        adapter = new DeviceInfoAdapter(deviceInfos,this);
        deviceLv = (ListView) findViewById(R.id.lv_device);
        deviceLv.setAdapter(adapter);

        deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectCameraDialog selectCameraDialog = new SelectCameraDialog();
                selectCameraDialog.setEZDeviceInfo(deviceInfos.get(position));
                selectCameraDialog.setCameraItemClick(DeviceInfoListActivity.this);
                selectCameraDialog.show(getFragmentManager(), "onPlayClick");
            }
        });
        new GetDeviceInfoTask().execute();
    }

    @Override
    public void onCameraItemClick(EZDeviceInfo deviceInfo, int camera_index) {
        EZCameraInfo cameraInfo = EZUtils.getCameraInfoFromDevice(deviceInfo, camera_index);
        Intent intent = new Intent(DeviceInfoListActivity.this, EZRealPlayActivity.class);
        intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
        intent.putExtra(IntentConsts.EXTRA_DEVICE_INFO, deviceInfo);
        startActivity(intent);
    }


    public class GetDeviceInfoTask extends AsyncTask<Void,Void,List<EZDeviceInfo>>{

        @Override
        protected List<EZDeviceInfo> doInBackground(Void... params) {
            try {
                return EZOpenSDK.getInstance().getDeviceList(0, 20);
            } catch (BaseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<EZDeviceInfo> mDeviceInfos) {
            super.onPostExecute(mDeviceInfos);
            deviceInfos.clear();
            deviceInfos.addAll(mDeviceInfos);
            adapter.notifyDataSetChanged();
        }
    }



    public class DeviceInfoAdapter extends BaseAdapter {
        private List<EZDeviceInfo> deviceInfos;
        private Context context;

        public DeviceInfoAdapter(List<EZDeviceInfo> deviceInfos, Context context) {
            this.deviceInfos = deviceInfos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return deviceInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EZDeviceInfo deviceInfo = deviceInfos.get(position);
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_camera_list, parent, false);
                holder = new Holder();
                holder.deviceImgIv = (ImageView) convertView.findViewById(R.id.iv_camera_img);
                holder.deviceNameTv = (TextView) convertView.findViewById(R.id.tv_camera_name);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            holder.deviceNameTv.setText(deviceInfo.getDeviceName());

            return convertView;
        }

        class Holder {
            private TextView deviceNameTv;
            private ImageView deviceImgIv;//视频封面无法从网络获取 建议退出观看时截图保存
        }
    }

}
