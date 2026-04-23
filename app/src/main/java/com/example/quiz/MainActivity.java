package com.example.quiz;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // СКРЫВАЕМ НИЖНЮЮ ПАНЕЛЬ (Immersive Mode)
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            
            View topBar = findViewById(R.id.top_bar);
            if (topBar != null) {
                topBar.setPadding(topBar.getPaddingLeft(), systemBars.top, topBar.getPaddingRight(), topBar.getPaddingBottom());
            }
            
            return insets;
        });

        // ПЕРЕХОД НА НОВЫЙ ЭКРАН
        findViewById(R.id.btn_start_quiz_wrapper).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Применяем градиент к заголовку
        TextView headlineAccent = findViewById(R.id.txt_question_mark_gradient);
        if (headlineAccent != null) {
            headlineAccent.getPaint().setShader(new LinearGradient(
                    0, 0, headlineAccent.getPaint().measureText(headlineAccent.getText().toString()), 0,
                    new int[]{0xFF8B5CF6, 0xFFEC4899},
                    null, Shader.TileMode.CLAMP
            ));
        }

        // Применяем градиент к центральному знаку вопроса
        TextView qMark = findViewById(R.id.txt_question_mark);
        if (qMark != null) {
            qMark.post(() -> {
                Shader textShader = new LinearGradient(0, 0, 0, qMark.getHeight(),
                        new int[]{0xFFDDD6FE, 0xFF8B5CF6, 0xFFEC4899},
                        new float[]{0f, 0.4f, 1f}, Shader.TileMode.CLAMP);
                qMark.getPaint().setShader(textShader);
                qMark.invalidate();
            });
            
            animateFloat(qMark, 4000, -20f, 0);
            animateFloat(findViewById(R.id.txt_question_mark_shadow), 4000, -20f, 0);
        }

        // Декоративные элементы
        int duration = 4000;
        float distance = -15f;
        animateFloat(findViewById(R.id.decor_stick_left), duration, distance, 200);
        animateFloat(findViewById(R.id.decor_stick_right_1), duration, distance, 400);
        animateFloat(findViewById(R.id.decor_stick_right_2), duration, distance, 600);
        animateFloat(findViewById(R.id.decor_diamond), duration, distance, 800);
        animateFloat(findViewById(R.id.decor_star), duration, distance, 300);
        animateFloat(findViewById(R.id.decor_qmark_1), duration, distance, 500);
        animateFloat(findViewById(R.id.decor_qmark_2), duration, distance, 700);
        animateFloat(findViewById(R.id.decor_qmark_3), duration, distance, 100);

        // Анимация блеска
        View shimmer = findViewById(R.id.shimmer_view);
        if (shimmer != null) {
            shimmer.post(() -> {
                float startX = -shimmer.getWidth();
                float endX = findViewById(R.id.btn_start_quiz_wrapper).getWidth() + shimmer.getWidth();
                ObjectAnimator animator = ObjectAnimator.ofFloat(shimmer, "translationX", startX, endX);
                animator.setDuration(2500);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setStartDelay(1000);
                animator.start();
            });
        }
    }

    private void animateFloat(View view, int duration, float translationY, int delay) {
        if (view != null) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, translationY, 0f);
            anim.setDuration(duration);
            anim.setStartDelay(delay);
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();
        }
    }
}