package com.example.quiz;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private View selectedOption = null;
    private List<View> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Immersive mode
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quiz_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initOptions();

        findViewById(R.id.txt_finish_later).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Переход на экран результатов по нажатию на кнопку Next
        findViewById(R.id.btn_next_wrapper).setOnClickListener(v -> {
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            startActivity(intent);
        });
    }

    private void initOptions() {
        options.add(findViewById(R.id.option_a));
        options.add(findViewById(R.id.option_b));
        options.add(findViewById(R.id.option_c));
        options.add(findViewById(R.id.option_d));

        for (View option : options) {
            option.setOnClickListener(v -> selectOption(v));
        }
    }

    private void selectOption(View view) {
        // Reset previous selection
        if (selectedOption != null) {
            updateOptionStyle(selectedOption, false);
        }

        // Set new selection
        selectedOption = view;
        updateOptionStyle(selectedOption, true);
    }

    private void updateOptionStyle(View view, boolean isSelected) {
        // Update background
        view.setBackgroundResource(isSelected ? R.drawable.bg_option_selected : R.drawable.bg_option_normal);

        // Find child views
        TextView text = null;
        View badge = null;
        TextView badgeText = null;
        ImageView checkImg = null;

        if (view.getId() == R.id.option_a) {
            text = findViewById(R.id.txt_option_a);
            badge = findViewById(R.id.badge_a);
            badgeText = findViewById(R.id.txt_badge_a);
            checkImg = findViewById(R.id.img_check_a);
        } else if (view.getId() == R.id.option_b) {
            text = findViewById(R.id.txt_option_b);
            badge = findViewById(R.id.badge_b);
            badgeText = findViewById(R.id.txt_badge_b);
            checkImg = findViewById(R.id.img_check_b);
        } else if (view.getId() == R.id.option_c) {
            text = findViewById(R.id.txt_option_c);
            badge = findViewById(R.id.badge_c);
            badgeText = findViewById(R.id.txt_badge_c);
            checkImg = findViewById(R.id.img_check_c);
        } else if (view.getId() == R.id.option_d) {
            text = findViewById(R.id.txt_option_d);
            badge = findViewById(R.id.badge_d);
            badgeText = findViewById(R.id.txt_badge_d);
            checkImg = findViewById(R.id.img_check_d);
        }

        if (text != null) {
            text.setTextColor(isSelected ? ContextCompat.getColor(this, R.color.white) : 0xFF374151);
        }

        if (badge != null) {
            // Set badge background (semi-transparent white when selected, light purple when not)
            if (isSelected) {
                badge.setBackgroundResource(0); // Clear to use MaterialCardView's property via code or just set color
                ((com.google.android.material.card.MaterialCardView)badge).setCardBackgroundColor(0x40FFFFFF);
            } else {
                ((com.google.android.material.card.MaterialCardView)badge).setCardBackgroundColor(0x1F8B5CF6);
            }
        }

        if (badgeText != null) {
            badgeText.setVisibility(isSelected ? View.GONE : View.VISIBLE);
        }

        if (checkImg != null) {
            checkImg.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        }
    }
}