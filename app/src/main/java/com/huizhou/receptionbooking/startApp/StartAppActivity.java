package com.huizhou.receptionbooking.startApp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.huizhou.receptionbooking.LoginActivity;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.AfterLogin;
import com.service.TimeGetDataService;

import org.apache.commons.lang3.StringUtils;

public class StartAppActivity extends Activity
{
    CustomVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        videoView = (CustomVideoView) findViewById(R.id.videoView1);
        /***
         * 将播放器关联上一个音频或者视频文件
         * videoView.setVideoURI(Uri uri)
         * videoView.setVideoPath(String path)
         * 以上两个方法都可以。
         */
        //videoView.setVideoPath("raw/test.mp4");

        /**
         * w为其提供一个控制器，控制其暂停、播放……等功能
         */
        videoView.setMediaController(new MediaController(this));

        videoView.setVideoURI(Uri.parse("android.resource://com.huizhou.receptionbooking/" + R.raw.startapp));

        videoView.start();

        /**
         * 视频或者音频到结尾时触发的方法
         */
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                //Log.i("通知", "完成");
                goToLogin();
                videoView = null;
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener()
        {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                // Log.i("通知", "播放中出现错误");
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener()
                {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra)
                    {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                            videoView.setBackgroundColor(Color.TRANSPARENT);
                        return true;
                    }
                });

            }
        });
    }

    public void skipButton(View view)
    {
        goToLogin();
    }

    private void goToLogin()
    {
        SharedPreferences userSettings = getApplicationContext().getSharedPreferences("userInfo", 0);
        String name = userSettings.getString("loginUserName", "default");

        SharedPreferences password = getApplicationContext().getSharedPreferences("password", 0);
        String passwordLg = password.getString("passwordLg", "default");

        if (StringUtils.isNotBlank(name) && !"default".equals(name) && StringUtils.isNotBlank(passwordLg)
                && !"default".equals(passwordLg))
        {
            Intent intent = new Intent(StartAppActivity.this, AfterLogin.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(StartAppActivity.this, LoginActivity.class);
            startActivityForResult(intent, 100);
            finish();
        }
    }

}
