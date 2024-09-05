package com.example.autogestion.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.autogestion.ui.profiles.ClientProfile
import com.example.autogestion.ui.Home
import com.example.autogestion.ui.profiles.VehicleProfile
import com.example.autogestion.ui.form.ClientFormUpdate
import com.example.autogestion.ui.form.VehicleFormAdd

object NavigationUtils {
    fun navigateTo(context: Context, destination: Class<*>, shouldFinish: Boolean = false, extras: Bundle? = null) {
        val intent = Intent(context, destination)
        extras?.let {
            intent.putExtras(it)
        }
        context.startActivity(intent)
        if (context is Activity && shouldFinish) {
            context.finish()
        }
    }

    fun navigateToHome(context: Context) {
        navigateTo(context, Home::class.java,true)
    }

    fun navigateToClientProfile(context: Context, clientId: Int) {
        navigateTo(context, ClientProfile::class.java, false, Bundle().apply {
            putInt("clientId", clientId)
        })
    }

    fun navigateToClientFormUpdate(context: Context, clientId: Int ){
        navigateTo(context, ClientFormUpdate::class.java, false, Bundle().apply {
            putInt("clientId", clientId)
        })
    }

    fun navigateToVehicleProfile(context: Context, vehicleId: Int) {
        navigateTo(context, VehicleProfile::class.java, false, Bundle().apply{
            putInt("vehicleId", vehicleId)
        })
    }

    fun navigateToVehicleFormAdd(context: Context, clientId: Int){
        navigateTo(context, VehicleFormAdd::class.java, false, Bundle().apply{
            putInt("clientId", clientId)
        })
    }
}