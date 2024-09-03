package com.example.autogestion.webService

import com.example.autogestion.model.NumberPlateResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Sends a multipart POST request to the plate-reader endpoint to extract number plate details from an image.
 *
 * @param token The authorization token required for the API.
 * @param imagePart The image to be processed, wrapped in a MultipartBody.Part.
 * @return A deferred response containing the extracted number plate details.
 *
 * **Note:** Ensure that the `NumberPlateResponse` data class is defined to correctly parse the API response.
 * Consider adding error handling mechanisms to catch exceptions like `IOException` or `HttpException`.
 * For more complex scenarios, you might want to implement retry logic or exponential backoff.
 */
interface ApiService {

    @Multipart
    @POST("plate-reader")
    suspend fun getNumberPlateDetails(
        @Header("Authorization") token: String,
        @Part imagePart : MultipartBody.Part
    ) : Response<NumberPlateResponse>
}