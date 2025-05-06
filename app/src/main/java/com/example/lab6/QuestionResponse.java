package com.example.lab6;

import java.util.List;

// Клас, що представляє відповідь від API з кодом відповіді та списком питань вікторини
public class QuestionResponse {
    private int response_code;
    private List<Question> results;

    public int getResponseCode() {
        return response_code;
    }

    public List<Question> getResults() {
        return results;
    }
}
