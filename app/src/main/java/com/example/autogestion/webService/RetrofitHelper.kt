package com.example.autogestion.webService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    /**
     * The base URL for the Plate Recognizer API.
     */
    const val baseUrl = "https://api.platerecognizer.com/v1/"

    /**
     * Creates and returns a Retrofit instance configured for the Plate Recognizer API.
     *
     * This method builds a Retrofit instance with the following configuration:
     *  - Base URL: Set to the `baseUrl` constant.
     *  - Converter Factory: Uses GsonConverterFactory to convert JSON responses into Kotlin objects.
     *
     * @return A Retrofit instance ready to make API calls to the Plate Recognizer API.
     */
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
