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

//    @OptIn(DelicateCoroutinesApi::class)
//    suspend fun uploadImageToServerAndGetResults(context: Context, savedUri: Uri?) : String {
//        return if (savedUri != null) {
//            withContext(Dispatchers.IO) {
//
//                val apiService: ApiService = RetrofitHelper.getInstance().create(ApiService::class.java)
//                val imgFile = context.getFileFromUri(savedUri)
//                val compressedImageFile = Compressor.compress(context, imgFile)
//                val imageFilePart = MultipartBody.Part.createFormData(
//                    "upload",
//                    compressedImageFile.name,
//                    RequestBody.create(
//                        "image/*".toMediaTypeOrNull(),
//                        compressedImageFile
//                    )
//                )
//
//                val response = apiService.getNumberPlateDetails(
//                    token = "TOKEN $API_TOKEN",
//                    imagePart = imageFilePart
//                )
//
//                if(response.isSuccessful && response.body() != null && (response.body()?.results?.size ?: 0) > 0 ) {
//                    vrn = response.body()?.results?.get(0)?.plate.toString().uppercase()
//                    vrn
//                }
//                else{
//                    "No Plate Found !"
//                }
//            }
//        } else {
//            "No Uri found !"
//        }
//
//    }
//}

    @OptIn(DelicateCoroutinesApi::class)
    fun uploadImageToServerAndGetResults(context: Context, savedUri: Uri, onResult: (String?) -> Unit) {
        val apiService: ApiService = RetrofitHelper.getInstance().create(ApiService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Convertir l'URI en fichier
                val imgFile = File(savedUri.path ?: return@launch)

                // Compresser l'image (si nécessaire)
                val compressedImageFile = Compressor.compress(context, imgFile)

                // Créer un MultipartBody.Part pour l'envoi
                val imageFilePart = MultipartBody.Part.createFormData(
                    "upload",
                    compressedImageFile.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), compressedImageFile)
                )

                // Appeler l'API pour envoyer l'image
                val response = apiService.getNumberPlateDetails(
                    token = "TOKEN $API_TOKEN",
                    imagePart = imageFilePart
                )

                if(response.isSuccessful && response.body() != null && (response.body()?.results?.size ?: 0) > 0 ) {
                    vrn = response.body()?.results?.get(0)?.plate.toString().uppercase()
                    withContext(Dispatchers.Main) {
                        onResult(vrn)
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        onResult(null)
                    }
                }

            } catch (e: Exception) {
                Log.e("Upload", "Failed to upload image", e)
            } finally {
                // Supprimer le fichier temporaire après utilisation
                File(savedUri.path ?: return@launch).delete()
            }
        }
    }
}
