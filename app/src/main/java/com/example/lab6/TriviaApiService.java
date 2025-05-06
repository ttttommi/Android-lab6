package com.example.lab6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Інтерфейс для взаємодії з API Trivia. Він визначає метод для отримання запитань
public interface TriviaApiService {

    // Метод для отримання запитань з API
    @GET("api.php") 
    Call<QuestionResponse> getQuestions(
            @Query("amount") int amount,
            @Query("type") String type
    );
}
