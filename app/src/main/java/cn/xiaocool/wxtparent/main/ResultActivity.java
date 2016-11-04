package cn.xiaocool.wxtparent.main;

import android.app.Activity;

public class ResultActivity extends Activity {
//
//	private TextView tv;
//	private ImageView img;
//	private Button btn;
//	private Button btnintent;
//	private Bundle bundle;
//	private WebView web;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_result);
//		initView();
//
//
//		initdata();
//
//	}
//
//	private void initdata() {
//		Intent intentvalue = getIntent();
//		bundle = intentvalue.getExtras();
//		tv.setText(bundle.getString("result"));
//		img.setImageBitmap((Bitmap)intentvalue.getParcelableExtra("bitmap"));
//		btnintent.setOnClickListener(new OnClickListener() {
//
//
//
//			@Override
//			public void onClick(View v) {
//				String str = bundle.getString("result");
//				String substr = str.substring(0, 4);
//				if(substr.equals("http")){
//					web = new WebView(ResultActivity.this);
//					web.loadUrl(str);
//					setContentView(web);
//					Toast.makeText(ResultActivity.this, "success!", Toast.LENGTH_SHORT).show();
//				}else{
//					Toast.makeText(ResultActivity.this, "失败了", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//
//		/*
//		 * ����رյ�ǰҳ��
//		 * */
//		btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ResultActivity.this.finish();
//			}
//		});
//	}
//
//	private void initView() {
//		tv = (TextView) findViewById(R.id.result_name);
//		img = (ImageView) findViewById(R.id.result_bitmap);
//		btn = (Button) findViewById(R.id.button_back);
//		btnintent = (Button) findViewById(R.id.intent2view);
//	}
//
////	@Override
////	public boolean onKeyDown(int keyCode, KeyEvent event) {
////		if((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
////			web.goBack();
////			return true;
////		}
////		return false;
////
////	}
//

}
