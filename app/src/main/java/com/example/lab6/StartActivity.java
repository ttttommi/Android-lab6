package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

// Активність, яка відображає екран старту і дозволяє користувачу перейти до активності з тестом
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Налаштування обробника кліку для кнопки "Почати"
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Перехід до QuizActivity, де проходить тест
                startActivity(new Intent(StartActivity.this, QuizActivity.class));
            }
        });
    }
}
