package com.example.quiz;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Убрали SplashScreen.installSplashScreen(this) для корректной работы своего макета
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // СКРЫВАЕМ СИСТЕМНУЮ НАВИГАЦИЮ
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        TextView appTitle = findViewById(R.id.app_title);
        if (appTitle != null) {
            appTitle.post(() -> {
                Shader textShader = new LinearGradient(
                        0, 0, appTitle.getPaint().measureText(appTitle.getText().toString()), 0,
                        new int[]{0xFF8B5CF6, 0xFFEC4899},
                        null, Shader.TileMode.CLAMP
                );
                appTitle.getPaint().setShader(textShader);
                appTitle.invalidate();
            });
        }

        startFloatingAnimation(findViewById(R.id.shape1), 4000, -30f, 0);
        startFloatingAnimation(findViewById(R.id.shape2), 4000, 25f, 500);

        View bulbGroup = findViewById(R.id.bulb_group);
        if (bulbGroup != null) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(bulbGroup, "scaleX", 1f, 1.1f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(bulbGroup, "scaleY", 1f, 1.1f, 1f);
            scaleX.setDuration(2000);
            scaleY.setDuration(2000);
            scaleX.setRepeatCount(ValueAnimator.INFINITE);
            scaleY.setRepeatCount(ValueAnimator.INFINITE);
            AnimatorSet bulbAnim = new AnimatorSet();
            bulbAnim.playTogether(scaleX, scaleY);
            bulbAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            bulbAnim.start();
        }

        ProgressBar progressBar = findViewById(R.id.splash_progress);
        if (progressBar != null) {
            ObjectAnimator progressAnim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
            progressAnim.setDuration(3000);
            progressAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            progressAnim.start();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3200);
    }

    private void startFloatingAnimation(View view, int duration, float translationY, int delay) {
        if (view == null) return;
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0f, translationY, 0f);
        animator.setDuration(duration);
        animator.setStartDelay(delay);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}