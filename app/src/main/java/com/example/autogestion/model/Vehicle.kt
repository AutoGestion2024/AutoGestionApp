package com.example.autogestion.model

import com.google.gson.annotations.SerializedName

data class Vehicle(

    /**
     * Confidence score associated with the detected vehicle.
     */
    @SerializedName("score") var score: Double? = null,
    /**
     * Detected vehicle type.
     */
    @SerializedName("type") var type: String? = null,
    /**
     * Bounding box of the detected vehicle.
     */
    @SerializedName("box") var box: Box? = Box()

)