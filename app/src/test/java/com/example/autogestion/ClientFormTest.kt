/*
package com.example.autogestion

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.autogestion.data.Client
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


class ClientFormTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testFormValidation_EmptyFields_ShowErrors() {
        var submittedClient: Client? = null

        composeTestRule.setContent {
            ClientForm(onSubmit = { client -> submittedClient = client })
        }

        composeTestRule.onNodeWithText("Ajouter le client").performClick()

        composeTestRule.onNodeWithText("Le nom est obligatoire").assertExists()
        composeTestRule.onNodeWithText("Le prénom est obligatoire").assertExists()
        composeTestRule.onNodeWithText("Le téléphone est obligatoire").assertExists()

        assertTrue(submittedClient == null)
    }

    @Test
    fun testFormSubmission_AllFieldsFilled_SubmitsCorrectly() {
        var submittedClient: Client? = null

        composeTestRule.setContent {
            ClientForm(onSubmit = { client -> submittedClient = client })
        }

        composeTestRule.onNodeWithText("Nom *").performTextInput("Doe")
        composeTestRule.onNodeWithText("Prénom *").performTextInput("John")
        composeTestRule.onNodeWithText("Téléphone *").performTextInput("1234567890")
        composeTestRule.onNodeWithText("Date de naissance (jj.mm.aaaa)").performTextInput("12.12.2000")
        composeTestRule.onNodeWithText("Email").performTextInput("john.doe@example.com")
        composeTestRule.onNodeWithText("Adresse").performTextInput("Rue de la rue")

        composeTestRule.onNodeWithText("Ajouter le client").performClick()

        composeTestRule.onNodeWithText("Le nom est obligatoire").assertDoesNotExist()
        composeTestRule.onNodeWithText("Le prénom est obligatoire").assertDoesNotExist()
        composeTestRule.onNodeWithText("Le téléphone est obligatoire").assertDoesNotExist()

        assertTrue(submittedClient != null)
        assertTrue(submittedClient?.firstName == "John")
        assertTrue(submittedClient?.lastName == "Doe")
        assertTrue(submittedClient?.phone == "1234567890")
        assertTrue(submittedClient?.birthDate == 976579200000)
        assertTrue(submittedClient?.email == "john.doe@example.com")
        assertTrue(submittedClient?.address == "Rue de la rue")
    }
}
*/
