package com.example.quiz;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = 16;
        int total = 20;
        
        updateScoreUI(score, total);
        startTrophyAnimation();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        findViewById(R.id.btn_back_res).setOnClickListener(v -> finish());
        View exitBtn = findViewById(R.id.txt_exit);
        if (exitBtn != null) {
            exitBtn.setOnClickListener(v -> finishAffinity());
        }
    }

    private void updateScoreUI(int score, int total) {
        ProgressBar progressBar = findViewById(R.id.score_progress);
        View scoreDot = findViewById(R.id.score_dot);
        TextView scoreText = findViewById(R.id.score_text);
        TextView percentageText = findViewById(R.id.percentage_text);

        int progress = (int) ((score / (float) total) * 100);
        progressBar.setProgress(progress);
        scoreText.setText(String.valueOf(score));
        percentageText.setText(progress + "%");

        float angle = (progress / 100f) * 360f;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scoreDot.getLayoutParams();
        params.circleAngle = angle;
        scoreDot.setLayoutParams(params);
    }

    private void startTrophyAnimation() {
        View trophyContainer = findViewById(R.id.trophy_animation_container);
        View trophyGlow = findViewById(R.id.trophy_glow);
        View trophyArc = findViewById(R.id.trophy_arc);

        if (trophyContainer == null) return;

        // Оставляем только статичное свечение (если нужно) и убираем арку, так как она была чисто анимационной
        if (trophyGlow != null) {
            trophyGlow.setAlpha(0.5f);
            trophyGlow.setScaleX(1.0f);
            trophyGlow.setScaleY(1.0f);
        }
        
        if (trophyArc != null) {
            trophyArc.setVisibility(View.GONE);
        }

        // 1. Анимация всего контейнера (Кубок + Свечение двигаются вместе)
        // Плавное движение вверх-вниз
        ObjectAnimator containerMoveY = ObjectAnimator.ofFloat(trophyContainer, "translationY", 0f, -25f);
        containerMoveY.setDuration(2800);
        containerMoveY.setRepeatCount(ValueAnimator.INFINITE);
        containerMoveY.setRepeatMode(ValueAnimator.REVERSE);
        containerMoveY.setInterpolator(new AccelerateDecelerateInterpolator());

        // Легкое покачивание
        ObjectAnimator containerRotate = ObjectAnimator.ofFloat(trophyContainer, "rotation", -2f, 2f);
        containerRotate.setDuration(3500);
        containerRotate.setRepeatCount(ValueAnimator.INFINITE);
        containerRotate.setRepeatMode(ValueAnimator.REVERSE);
        containerRotate.setInterpolator(new AccelerateDecelerateInterpolator());

        containerMoveY.start();
        containerRotate.start();
    }
}
