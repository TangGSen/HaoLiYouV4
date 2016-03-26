package com.sen.haoliyou.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sen.videolib.R;

import java.util.ArrayList;

import player.StringUtil;
import player.VideoBaseActivity;
import player.VideoItem;
import player.VideoView;


public class VideoPlayerActivity extends VideoBaseActivity {
    private VideoView video_view;
    // 顶部控制面板控件
    private LinearLayout ll_top_control;
    private TextView tv_name, tv_system_time;
    private ImageView iv_battery;
    // 底部控制面板控件
    private LinearLayout ll_bottom_control;
    private SeekBar video_seekbar;
    private TextView tv_current_progress, tv_duration;
    private AppCompatTextView btn_exit,btn_play,btn_screen,btn_exit_video;
    private RelativeLayout ll_loading;
    private LinearLayout ll_buffering;

    private BatteryChangeReceiver batteryChangeReceiver;

    private final int MESSAGE_UPDATE_SYSTEM_TIME = 0;// 更新系统时间
    private final int MESSAGE_UPDATE_PLAY_PROGRESS = 1;// 更新播放进度
    private final int MESSAGE_HIDE_CONTROL = 2;// 延时隐藏控制面板
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_UPDATE_SYSTEM_TIME:
                    updateSystemTime();
                    break;
                case MESSAGE_UPDATE_PLAY_PROGRESS:
                    updatePlayProgress();
                    break;
                case MESSAGE_HIDE_CONTROL:
                    hideControlLayout();
                    break;
            }
        }

        ;
    };
    private int screenWidth;
    private int screenHeight;
    private int mTouchSlop;// 滑动的界限值
    private int currentPosition;// 当前视频在videoList中的位置
    private ArrayList<VideoItem> videoList;// 视频列表
    private GestureDetector gestureDetector;
    private boolean isShowContol = false;// 是否是显示控制面板


    //DBDao mDbDao;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_video_player);
        video_view = (VideoView) findViewById(R.id.video_view);

        ll_top_control = (LinearLayout) findViewById(R.id.ll_top_control);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_system_time = (TextView) findViewById(R.id.tv_system_time);
        iv_battery = (ImageView) findViewById(R.id.iv_battery);

        ll_bottom_control = (LinearLayout) findViewById(R.id.ll_bottom_control);
        video_seekbar = (SeekBar) findViewById(R.id.video_seekbar);
        btn_play = (AppCompatTextView) findViewById(R.id.btn_play);
        btn_exit = (AppCompatTextView) findViewById(R.id.btn_exit);
        btn_exit_video = (AppCompatTextView) findViewById(R.id.btn_exit_video);
        btn_screen = (AppCompatTextView) findViewById(R.id.btn_screen);
        tv_current_progress = (TextView) findViewById(R.id.tv_current_progress);
        tv_duration = (TextView) findViewById(R.id.tv_duration);

        ll_loading = (RelativeLayout) findViewById(R.id.ll_loading);
        ll_buffering = (LinearLayout) findViewById(R.id.ll_buffering);

    }

    @Override
    protected void initListener() {
        btn_exit.setOnClickListener(this);
        btn_exit_video.setOnClickListener(this);
        btn_screen.setOnClickListener(this);
        btn_play.setOnClickListener(this);

        video_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CONTROL, 5000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(MESSAGE_HIDE_CONTROL);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    video_view.seekTo(progress);
                    tv_current_progress.setText(StringUtil
                            .formatVideoDuration(progress));
                }
            }
        });
        // 监听播放结束
        video_view.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
              //  btn_play.setBackgroundResource(R.drawable.selector_btn_play);
                //calculateAndUpload(startTime, endTime);
                Toast.makeText(getApplicationContext(), "视频播放完毕", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        video_view
                .setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        // LogUtil.e(this, "percent: "+percent);
                        // percent:0-100
                        int bufferedProgress = (int) ((percent / 100.0f) * video_view
                                .getDuration());
                        video_seekbar.setSecondaryProgress(bufferedProgress);
                    }
                });
        video_view.setOnInfoListener(new OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:// 当拖动卡顿开始时调用
                        ll_buffering.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:// 当拖动卡顿结束调用
                        ll_buffering.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });
        video_view.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Toast.makeText(VideoPlayerActivity.this, "不支持该格式", Toast.LENGTH_SHORT)
                                .show();
                        break;

                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        gestureDetector = new GestureDetector(this,new MyGestureLitener());
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        mTouchSlop = ViewConfiguration.getTouchSlop();
        updateSystemTime();
        registerBatteryChangeReceiver();

        Uri uri = getIntent().getData();
//        String fileName = getIntent().getStringExtra("courseName");
//        courseId = getIntent().getStringExtra("courseId");
        if (uri != null) {
            // 从文件发起的播放请求

            tv_name.setText("");
            video_view.setVideoURI(uri);

        } else {
            // 正常从视频列表进入的
            currentPosition = getIntent().getExtras().getInt("currentPosition");
            videoList = (ArrayList<VideoItem>) getIntent().getExtras()
                    .getSerializable("videoList");

            playVideo();
        }

        video_view.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // ll_loading.setVisibility(View.GONE);
                // 给加载界面增加渐隐动画
                ViewPropertyAnimator.animate(ll_loading).alpha(0)
                        .setDuration(600).setListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        ll_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {
                    }
                });
                // 视频开始播放
                video_view.start();
//                btn_play.setBackgroundResource(R.drawable.selector_btn_pause);

                video_seekbar.setMax(video_view.getDuration());
                tv_duration.setText(StringUtil.formatVideoDuration(video_view
                        .getDuration()));
                updatePlayProgress();
            }
        });

        // video_view.setMediaController(new MediaController(this));
    }

    // 在电话来的时候，或者被其他应用，就暂停播放
    @Override
    protected void onPause() {
        if (video_view.isPlaying()) {
            video_view.pause();
           // calculateAndUpload(startTime, endTime);
            handler.removeMessages(MESSAGE_UPDATE_PLAY_PROGRESS);
        }
        super.onPause();
    }

    /**
     * 播放currentPosition当前位置的视频
     */
    private void playVideo() {
        if (videoList == null || videoList.size() == 0) {
            finish();
            return;
        }

        VideoItem videoItem = videoList.get(currentPosition);
        tv_name.setText(videoItem.getTitle());
        video_view.setVideoURI(Uri.parse(videoItem.getPath()));

    }

    /**
     * 更新播放进度
     */
    private void updatePlayProgress() {
        tv_current_progress.setText(StringUtil.formatVideoDuration(video_view
                .getCurrentPosition()));
        video_seekbar.setProgress(video_view.getCurrentPosition());
        handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PLAY_PROGRESS, 1000);
    }




    /**
     * 注册电量变化的广播接受者
     */
    private void registerBatteryChangeReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryChangeReceiver = new BatteryChangeReceiver();
        registerReceiver(batteryChangeReceiver, filter);
    }

    /**
     * 更新系统时间
     */
    private void updateSystemTime() {
        tv_system_time.setText(StringUtil.formatSystemTime());
        handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_SYSTEM_TIME, 1000);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                if (video_view.isPlaying()) {
                    video_view.pause();
                    handler.removeMessages(MESSAGE_UPDATE_PLAY_PROGRESS);
                }
                finish();
                break;
            case R.id.btn_exit_video:
                finish();
                break;
            case R.id.btn_play:
                if (video_view.isPlaying()) {
                    video_view.pause();
                    handler.removeMessages(MESSAGE_UPDATE_PLAY_PROGRESS);
                } else {
                    video_view.start();
                    handler.sendEmptyMessage(MESSAGE_UPDATE_PLAY_PROGRESS);
                }
                updatePlayBtnBg();
                break;

            case R.id.btn_screen:
                video_view.switchScreen();
                updateScreenBtnBg();
                break;
        }
    }

    /**
     * 更新屏幕按钮的背景图片
     */
    private void updateScreenBtnBg() {
        Drawable drawable= ContextCompat.getDrawable(this,video_view.isFullScreen() ? R.drawable.btn_fullscreen
                : R.drawable.btn_defualt_screen);
        drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btn_screen.setCompoundDrawables(drawable,null,null,null);
    }

    private float downY;
    private String courseId;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        unregisterReceiver(batteryChangeReceiver);

        getApplication();
        getApplicationContext();
    }

    private class BatteryChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // level：表示系统电量等级，0-100
            int level = intent.getIntExtra("level", 0);
            updateBatteryBg(level);
        }
    }

    private class MyGestureLitener extends SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            processClick(btn_play);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            processClick(btn_screen);
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isShowContol) {
                // 隐藏操作
                hideControlLayout();
            } else {
                // 显示操作
                showControlLayout();
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    /**
     * 显示控制面板
     */
    private void showControlLayout() {

//        ViewPropertyAnimator.animate(ll_top_control).translationY(0)
//                .setDuration(100);
//        ViewPropertyAnimator.animate(ll_bottom_control).translationY(0)
//                .setDuration(100);
        ll_top_control.setVisibility(View.VISIBLE);
        ll_bottom_control.setVisibility(View.VISIBLE);
        isShowContol = true;

        handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CONTROL, 5000);
    }

    /**
     * 隐藏控制面板
     */
    private void hideControlLayout() {
//        ViewPropertyAnimator.animate(ll_top_control)
//                .translationY(-ll_top_control.getHeight()).setDuration(100);
//        ViewPropertyAnimator.animate(ll_bottom_control)
//                .translationY(ll_bottom_control.getHeight()).setDuration(100);
        ll_top_control.setVisibility(View.GONE);
        ll_bottom_control.setVisibility(View.GONE);
        isShowContol = false;

    }

    /**
     * 根据系统电量等级去设置对应的图片
     *
     * @param level
     */
    private void updateBatteryBg(int level) {
        if (level <= 0) {
            iv_battery.setBackgroundResource(R.mipmap.ic_battery_0);
        } else if (level > 0 && level <= 10) {
            iv_battery.setBackgroundResource(R.mipmap.ic_battery_10);
        } else if (level > 10 && level <= 20) {
            iv_battery.setBackgroundResource(R.mipmap.ic_battery_20);
        } else if (level > 20 && level <= 40) {
            iv_battery.setBackgroundResource(R.mipmap.ic_battery_40);
        } else if (level > 40 && level <= 60) {
            iv_battery.setBackgroundResource(R.mipmap.ic_battery_60);
        } else if (level > 60 && level <= 80) {
            iv_battery.setBackgroundResource(R.mipmap.ic_battery_80);
        } else {
            iv_battery.setBackgroundResource(R.mipmap.ic_battery_100);
        }
    }

    /**
     * 更新播放按钮的背景图片
     */
    private void updatePlayBtnBg() {
        Drawable drawable= ContextCompat.getDrawable(this,video_view.isPlaying() ? R.drawable.btn_pause_video : R.drawable.btn_play_video);
        drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btn_play.setCompoundDrawables(drawable,null,null,null);

    }



}
