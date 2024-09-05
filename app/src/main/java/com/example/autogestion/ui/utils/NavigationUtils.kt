package com.example.autogestion.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.autogestion.ui.profiles.ClientProfile
import com.example.autogestion.ui.Home
import com.example.autogestion.ui.forms.ClientForm
import com.example.autogestion.ui.profiles.VehicleProfile
import com.example.autogestion.ui.forms.ClientFormUpdate
import com.example.autogestion.ui.forms.RepairForm
import com.example.autogestion.ui.forms.VehicleForm
import com.example.autogestion.ui.forms.VehicleFormAdd

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

    fun navigateToClientForm(context: Context, firstName: String, lastName: String, phoneNumber: String, birthDate: String, address: String, email: String){
        navigateTo(context, ClientForm::class.java, false, Bundle().apply {
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("phoneNumber", phoneNumber)
            putString("birthDate", birthDate)
            putString("email", email)
            putString("address", address)
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

    fun navigateToVehicleForm(context: Context, firstName: String, lastName: String, phoneNumber: String, birthDate: String, address: String, email: String, registrationPlate: String = "",
                              greyCard: String = "", chassisNum: String = "", brand: String = "", model: String = "", color: String = "", clientId: Int = 0){

        navigateTo(context, VehicleForm::class.java, false, Bundle().apply {
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("phoneNumber", phoneNumber)
            putString("birthDate", birthDate)
            putString("address", address)
            putString("email", email)

            putString("registrationPlate", registrationPlate)
            putString("greyCard", greyCard)
            putString("chassisNum", chassisNum)
            putString("brand", brand)
            putString("model", model)
            putString("color", color)
            putInt("clientId", clientId)
        })
    }

    fun navigateToRepairForm(context: Context, firstName: String, lastName: String, phoneNumber: String, birthDate: String, address: String, email: String, registrationPlate: String = "",
                             greyCard: String = "", chassisNum: String = "", brand: String = "", model: String = "", color: String = ""){
        navigateTo(context, RepairForm::class.java, false, Bundle().apply {
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("phoneNumber", phoneNumber)
            putString("birthDate", birthDate)
            putString("address", address)
            putString("email", email)

            putString("registrationPlate", registrationPlate)
            putString("greyCard", greyCard)
            putString("chassisNum", chassisNum)
            putString("brand", brand)
            putString("model", model)
            putString("color", color)
        })
    }
}