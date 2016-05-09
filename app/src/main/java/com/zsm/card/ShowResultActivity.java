package com.zsm.card;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.intsig.openapilib.OpenApi;

public class ShowResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);

		Intent data = getIntent();
		String trim = data.getStringExtra(OpenApi.EXTRA_KEY_IMAGE);
		System.out.println("图片地址："+trim);
		if (!TextUtils.isEmpty(trim)) {
			ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            imageView.setImageBitmap(BitmapFactory.decodeFile(trim));
		}

		String vcf = data.getStringExtra(OpenApi.EXTRA_KEY_VCF);

		TextView textView = (TextView) findViewById(R.id.textView1);
		textView.setText("vcf名片信息："+vcf);
	}
    
    

}
