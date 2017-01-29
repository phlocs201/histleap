package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    Handler mHandler = new Handler();
    Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),
                        MapsActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        };
        mHandler.postDelayed(mRunnable, 2000); // 2秒
    }

    @Override
    protected void onPause() {
        super.onPause();
        // スプラッシュ中に戻るボタンを押された場合、キャンセルする
        mHandler.removeCallbacks(mRunnable);
    }
}
