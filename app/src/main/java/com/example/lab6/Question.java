package com.example.lab6;

import java.util.List;

// Клас, що представляє питання вікторини, яке містить текст питання, правильну відповідь і список неправильних відповідей
public class Question {
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrect_answers;
    }
}
