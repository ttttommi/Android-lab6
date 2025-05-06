package com.example.lab6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Активність, яка показує результат гри та найкращий результат користувача
public class ResultActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView bestScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Ініціалізація текстових полів для результату та найкращого результату
        resultTextView = findViewById(R.id.result_text);
        bestScoreTextView = findViewById(R.id.best_score_text);

        // Отримання результату гри з переданих даних
        int score = getIntent().getIntExtra("score", 0);
        resultTextView.setText("Ваш результат: " + score);

        // Отримання найкращого результату з SharedPreferences
        SharedPreferences prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        int bestScore = prefs.getInt("best_score", 0);
        bestScoreTextView.setText("Найкращий результат: " + bestScore);

        // Якщо поточний результат кращий за найкращий, оновлюємо найкращий результат
        if (score > bestScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("best_score", score);
            editor.apply();
        }
    }
}
