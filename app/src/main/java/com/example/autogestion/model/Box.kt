package com.example.autogestion.model

import com.google.gson.annotations.SerializedName

data class Box(

    /**
     * Minimum x-coordinate (top-left corner) of the box.
     */
    @SerializedName("xmin") var xmin: Int? = null,
    /**
     * Minimum y-coordinate (top-left corner) of the box.
     */
    @SerializedName("ymin") var ymin: Int? = null,
    /**
     * Maximum x-coordinate (bottom-right corner) of the box.
     */
    @SerializedName("xmax") var xmax: Int? = null,
    /**
     * Maximum y-coordinate (bottom-right corner) of the box.
     */
    @SerializedName("ymax") var ymax: Int? = null
)
