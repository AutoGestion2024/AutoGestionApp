package com.example.autogestion.model

import com.google.gson.annotations.SerializedName

data class Results(

    /**
     * The bounding box of the detected number plate.
     */
    @SerializedName("box") var box: Box? = Box(),
    /**
     * The detected number plate text.
     */
    @SerializedName("plate") var plate: String? = null,
    /**
     * The detected region associated with the number plate.
     */
    @SerializedName("region") var region: Region? = Region(),
    /**
     * The confidence score associated with the detected number plate.
     */
    @SerializedName("score") var score: Double? = null,
    /**
     * A list of candidate number plate texts and their corresponding confidence scores.
     */
    @SerializedName("candidates") var candidates: ArrayList<Candidates> = arrayListOf(),
    /**
     * The confidence score associated with the detected vehicle type.
     */
    @SerializedName("dscore") var dscore: Double? = null,
    /**
     * The detected vehicle type.
     */
    @SerializedName("vehicle") var vehicle: Vehicle? = Vehicle()

)
