package com.example.autogestion.model

import com.google.gson.annotations.SerializedName

data class Candidates(

    /**
     * Confidence score associated with the detected license plate.
     */
    @SerializedName("score") var score: Double? = null,
    /**
     * Detected license plate.
     */
    @SerializedName("plate") var plate: String? = null

)
