package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.common.XTextView;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class MakeQRCodeActivity extends AppCompatActivity
{
    private String id;
    private String threaf;
    private String departmentItem;
    private ImageView mImageView;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_qrcode);

        tv = (XTextView) this.findViewById(R.id.makeQRCodeBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        //会议ID
        id = i.getStringExtra("id");
        threaf = i.getStringExtra("thread");
        departmentItem  =  i.getStringExtra("department");

        TextView threafTv = (TextView) findViewById(R.id.qrcodeTheaf);
        threafTv.setText("会议主题：" + threaf);

        TextView qrcodeDep = (TextView) findViewById(R.id.qrcodeDep);
        qrcodeDep.setText("组织部门：" + departmentItem);

        mImageView = (ImageView) this.findViewById(R.id.img_shouw);
        Bitmap qrCode = EncodingUtils.createQRCode(id, 700, 700, null);
        mImageView.setImageBitmap(qrCode);
    }
}
