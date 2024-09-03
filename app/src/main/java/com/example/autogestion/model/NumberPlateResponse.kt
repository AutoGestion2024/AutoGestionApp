package com.example.autogestion.model

import com.google.gson.annotations.SerializedName

data class NumberPlateResponse(

    /**
     * The processing time taken to extract the number plate details (in seconds).
     */
    @SerializedName("processing_time") var processingTime: Double? = null,
    /**
     * A list of detected number plate results.
     */
    @SerializedName("results") var results: ArrayList<Results> = arrayListOf(),
    /**
     * The original filename of the image that was processed.
     */
    @SerializedName("filename") var filename: String? = null,
    /**
     * The version of the plate recognition algorithm used.
     */
    @SerializedName("version") var version: Int? = null,
    /**
     * The ID of the camera that captured the image.
     */
    @SerializedName("camera_id") var cameraId: String? = null,
    /**
     * The timestamp of the image capture.
     */
    @SerializedName("timestamp") var timestamp: String? = null

)
