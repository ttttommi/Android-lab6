package com.example.lab6;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Клас для створення та надання доступу до клієнта API з використанням бібліотеки Retrofit
public class ApiClient {

    // Базова URL-адреса API для отримання запитань
    private static final String BASE_URL = "https://opentdb.com/";

    // Об'єкт Retrofit, що використовується для виконання HTTP запитів
    private static Retrofit retrofit = null;

    // Метод повертає інтерфейс TriviaApiService для взаємодії з API
    public static TriviaApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) 
                    .build();
        }
        // Створення реалізації інтерфейсу API
        return retrofit.create(TriviaApiService.class);
    }
}

