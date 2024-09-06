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
import com.example.autogestion.ui.forms.VehicleFormUpdate


/**
 * NavigationUtils is a utility object that simplifies navigation between different screens (activities) in an Android app.
 * It provides various methods to navigate to specific screens and pass additional data (via extras) when needed.
 */
object NavigationUtils {

    /**
     * General function to handle navigation between activities.
     *
     * @param context The context from which the navigation is initiated.
     * @param destination The target activity class to which we are navigating.
     * @param shouldFinish A boolean flag indicating whether the current activity should be finished after navigation (default is false).
     * @param extras Optional Bundle of additional data to be passed to the target activity (default is null).
     */
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

    /**
     * Navigates to the Home screen and finishes the current activity.
     *
     * @param context The context from which navigation is initiated.
     */
    fun navigateToHome(context: Context) {
        navigateTo(context, Home::class.java,true)
    }

    /**
     * Navigates to the Client Profile screen for a specific client.
     *
     * @param context The context from which navigation is initiated.
     * @param clientId The ID of the client whose profile will be displayed.
     */
    fun navigateToClientProfile(context: Context, clientId: Int) {
        navigateTo(context, ClientProfile::class.java, false, Bundle().apply {
            putInt("clientId", clientId)
        })
    }

    /**
     * Navigates to the Client Form Update screen for a specific client.
     *
     * @param context The context from which navigation is initiated.
     * @param clientId The ID of the client whose information will be updated.
     */
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

    /**
     * Navigates to the Vehicle Profile screen for a specific vehicle.
     *
     * @param context The context from which navigation is initiated.
     * @param vehicleId The ID of the vehicle whose profile will be displayed.
     */
    fun navigateToVehicleProfile(context: Context, vehicleId: Int) {
        navigateTo(context, VehicleProfile::class.java, false, Bundle().apply{
            putInt("vehicleId", vehicleId)
        })
    }

    /**
     * Navigates to the Vehicle Form Add screen for adding a new vehicle for a specific client.
     *
     * @param context The context from which navigation is initiated.
     * @param clientId The ID of the client for whom the new vehicle will be added.
     */
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

    fun navigateToVehicleUpdateForm(context: Context, vehicleId: Int){
        navigateTo(context, VehicleFormUpdate::class.java, false, Bundle().apply {
            putInt("vehicleId", vehicleId)
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