package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// QuizActivity відповідає за реалізацію активності, де проводиться вікторина
// Включає в себе завантаження запитань, обробку натискання кнопок, таймер і показ результатів
public class QuizActivity extends AppCompatActivity {
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView questionTextView;
    private Button[] optionButtons;
    private ProgressBar progressBar;
    private TextView timerTextView;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Ініціалізація елементів інтерфейсу
        questionTextView = findViewById(R.id.question_text);
        optionButtons = new Button[] {
                findViewById(R.id.option1),
                findViewById(R.id.option2),
                findViewById(R.id.option3)
        };
        progressBar = findViewById(R.id.progress_bar);
        timerTextView = findViewById(R.id.timer_text);

        // Ініціалізація таймера
        timer = new CountDownTimer(30000, 1000) {  // Таймер на 30 секунд
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000)); // Оновлюємо відлік часу
            }

            @Override
            public void onFinish() {
                checkAnswer(-1); // Якщо час вийшов, перевіряємо відповідь
            }
        };

        // Завантажуємо запитання з API
        loadQuestions();

        // Обробка натискання кнопок для варіантів
        for (int i = 0; i < optionButtons.length; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(index); 
                }
            });
        }
    }

    // Завантаження запитань з API
    private void loadQuestions() {
        TriviaApiService apiService = ApiClient.getApiService();
        Call<QuestionResponse> call = apiService.getQuestions(10, "multiple"); // Запит на 10 питань
        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body().getResults(); 
                    showNextQuestion(); 
                } else {
                    Toast.makeText(QuizActivity.this, "Не вдалося завантажити запитання", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Показати наступне питання
    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);

            // Розкодовуємо HTML символи у тексті питання
            String decodedQuestion = StringEscapeUtils.unescapeHtml4(currentQuestion.getQuestion());
            questionTextView.setText(decodedQuestion);

            // Варіанти відповідей
            List<String> options = new ArrayList<>(currentQuestion.getIncorrectAnswers());
            options.add(currentQuestion.getCorrectAnswer());
            Collections.shuffle(options); 

            for (int i = 0; i < optionButtons.length; i++) {
                optionButtons[i].setText(options.get(i));
                optionButtons[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Встановлюємо початковий колір
            }

            progressBar.setProgress(100 * currentQuestionIndex / questions.size()); // Оновлюємо прогрес
            currentQuestionIndex++;

            timer.cancel();
            timer.start(); // Перезапускаємо таймер
        } else {
            // Якщо всі питання пройдені, показуємо результат
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
    }

    // Перевірка вибраної відповіді
    private void checkAnswer(int selectedOptionIndex) {
        timer.cancel();
        Question currentQuestion = questions.get(currentQuestionIndex - 1);
        String correctAnswer = currentQuestion.getCorrectAnswer();

        // Оновлюємо кольори кнопок залежно від вибраної відповіді
        for (int i = 0; i < optionButtons.length; i++) {
            String buttonText = optionButtons[i].getText().toString();
            if (buttonText.equals(correctAnswer)) {
                optionButtons[i].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Правильна відповідь
            } else if (i == selectedOptionIndex) {
                optionButtons[i].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Неправильна відповідь
            } else {
                optionButtons[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Початковий колір
            }
        }

        // Якщо відповідь правильна, збільшуємо рахунок
        if (selectedOptionIndex != -1 && optionButtons[selectedOptionIndex].getText().toString().equals(correctAnswer)) {
            score++;
        }

        progressBar.setProgress(100 * currentQuestionIndex / questions.size());

        // Перехід до наступного питання після затримки
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNextQuestion();
            }
        }, 1000);
    }
}
