package com.example.autogestion.Utils

import android.app.Activity
import androidx.activity.ComponentActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.autogestion.ClientProfile
import com.example.autogestion.Home
import com.example.autogestion.VehicleProfile
import com.example.autogestion.form.ClientFormUpdate
import com.example.autogestion.form.VehicleFormAdd

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