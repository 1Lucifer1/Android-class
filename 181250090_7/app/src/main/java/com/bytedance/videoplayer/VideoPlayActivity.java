package com.bytedance.videoplayer;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayActivity extends AppCompatActivity {

    private Button playbtn;
    private Button pausebtn;
    private VideoView videoView;
    private SeekBar seekBar;
    private TextView totaltimetv;
    private TextView currenttimetv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        //隐藏ActionBar
        getSupportActionBar().hide();

        videoView = findViewById(R.id.videoView);
        playbtn = findViewById(R.id.playbutton);
        pausebtn = findViewById(R.id.pausebutton);
        seekBar = findViewById(R.id.seekBar);
        totaltimetv = findViewById(R.id.totaltime_textview);
        currenttimetv = findViewById(R.id.currenttime_textview);

        totaltimetv.bringToFront();

        videoView.setVideoPath(getVideoPath(R.raw.bytedance));

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        videoView.setLayoutParams(params);

        VideoThread videoThread = new VideoThread();
        videoThread.start();

        videoView.requestFocus();
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                seekBar.setMax(videoView.getDuration());//必须在这里获取，因为getduration是获取当前播放的视频的总长度，所以如果不在视频开始播放后获取，就会把seekbar的max设为0
                int total = videoView.getDuration();
                int minute = total/60000;
                int second = (total-minute*6000)/1000;
                totaltimetv.setText(" "+minute+":"+second);
            }
        });

        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                videoView.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(seekBar.getProgress());//不能在onprogresschanged里面设置，因为handler改变seekbar的progress也会触发onprogresschanged，导致在一秒内重复播放
                int total= seekBar.getProgress();
                int minute=total/60000;
                int second=(total-minute*6000)/1000;
                currenttimetv.setText(" "+minute+":"+second);
            }
        });
    }

    Handler videohandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 888:
                    seekBar.setProgress(videoView.getCurrentPosition());
                    int total = videoView.getCurrentPosition();
                    int minute = total/60000;
                    int second = (total-minute*6000)/1000;
                    currenttimetv.setText(" "+minute+":"+second);
                    break;
            }
        }
    };

    class VideoThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(!Thread.currentThread().isInterrupted())
            {
                Message message = new Message();
                message.what = 888;
                videohandler.sendMessage(message);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
}