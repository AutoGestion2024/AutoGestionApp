import android.content.Context
import com.example.autogestion.webService.ApiService
import android.net.Uri
import android.util.Log

import com.example.autogestion.webService.RetrofitHelper
import com.example.autogestion.webService.getFileFromUri

import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import kotlinx.coroutines.withContext
import java.io.File

class RetrofitCallClass (private val scope: CoroutineScope){

    private val API_TOKEN = "8e9dab6d5db9ea25c8adc6ca6419a44d27a12948"
    private  var vrn: String = ""

    @OptIn(DelicateCoroutinesApi::class)
    fun uploadImageToServerAndGetResults(context: Context, savedUri: Uri, onResult: (String?) -> Unit) {
        val apiService: ApiService = RetrofitHelper.getInstance().create(ApiService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Convert the URI to a File object
                val imgFile = File(savedUri.path ?: return@launch)

                // Compress the image file if necessary
                val compressedImageFile = Compressor.compress(context, imgFile)

                // Create a MultipartBody.Part for sending the image
                val imageFilePart = MultipartBody.Part.createFormData(
                    "upload",
                    compressedImageFile.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), compressedImageFile)
                )

                // Call the API to upload the image and get the results
                val response = apiService.getNumberPlateDetails(
                    token = "TOKEN $API_TOKEN",
                    imagePart = imageFilePart
                )

                // Check if the response is successful and contains results
                if(response.isSuccessful && response.body() != null && (response.body()?.results?.size ?: 0) > 0 ) {
                    vrn = response.body()?.results?.get(0)?.plate.toString().uppercase()
                    // Pass the result back to the main thread
                    withContext(Dispatchers.Main) {
                        onResult(vrn)
                    }
                }else{
                    // Pass null if there are no results or if the response is not successful
                    withContext(Dispatchers.Main) {
                        onResult(null)
                    }
                }

            } catch (e: Exception) {
                // Log any exceptions that occur during the upload
                Log.e("Upload", "Failed to upload image", e)
            } finally {
                // Delete the temporary file after use
                File(savedUri.path ?: return@launch).delete()
            }
        }
    }
}
