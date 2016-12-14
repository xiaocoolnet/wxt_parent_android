package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/3/30.
 */
public class ScanQrCodeActivity extends BaseActivity {
    private TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        resultTextView = (TextView) findViewById(R.id.tv_scan_result);

        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(ScanQrCodeActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            resultTextView.setText(scanResult);
        }
    }
}
