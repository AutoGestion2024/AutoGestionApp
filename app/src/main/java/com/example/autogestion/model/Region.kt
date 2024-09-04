package com.example.autogestion.model

import com.google.gson.annotations.SerializedName

data class Region(

    /**
     * The ISO 3166-2 code of the detected region.
     */
    @SerializedName("code") var code: String? = null,
    /**
     * The confidence score associated with the detected region.
     */
    @SerializedName("score") var score: Double? = null

)
