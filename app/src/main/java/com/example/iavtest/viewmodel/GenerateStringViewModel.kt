package com.example.iavtest.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import com.example.iavtest.data.model.StringModel
import com.example.iavtest.data.repository.GenerateStringRepository
import kotlinx.coroutines.delay


class GenerateStringViewModel(private val repository: GenerateStringRepository) : ViewModel() {

    /**
     *  List of random strings to be displayed in the UI
     *  This is private because we don't want other classes to modify it directly.
     * It is mutable because we are adding new items when fetching from the Content Provider.
     */

    private val _randomStrings = mutableStateListOf<StringModel>()
    val randomStrings: State<List<StringModel>> = derivedStateOf { _randomStrings }



    /**
     * It directly holds a single value like Int, String, or Object.
     * Whenever the value changes, Compose will recompose.
     */
    private val _selectedItem = mutableStateOf<StringModel?>(null)
    val selectedItem : State<StringModel?> = _selectedItem


    /**
     *  State for managing loading status
     */
    var isLoading = mutableStateOf(false)

    /**
     * State for handling error messages
     */
    var errorMessage = mutableStateOf("")



    // Fetch a random string from the content provider with maxlenth
    fun fetchRandomString(currentMaxLength: Int) {
        isLoading.value = true
        errorMessage.value = ""


        /**
         * launching the couroutine since its long running operation and
         * fetching the data from the Content Provider
         * viewModelScope : because we are in the ViewModel and associtaed with
         * lifec=Cycle Aware
         *
         */
        viewModelScope.launch {
            try {
                val result = repository.queryContentProvider(currentMaxLength)
                if (result != null) {
                    _randomStrings.add(result)
                } else {
                    isLoading.value = false
                    errorMessage.value = "No data returned."
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Failed to fetch string. Try again."
            } finally {
                delay(500)
                isLoading.value = false
            }
        }
    }


    /**
     * function to handle deleted Items
     */
    fun deleteString(item: StringModel) {
        _randomStrings.remove(item)
    }

    /**
     * function to handle deleted all Items
     */
    fun clearAll() {
        _randomStrings.clear()
    }

    fun setSelectedItem(item: StringModel) {
        _selectedItem.value = item
    }
}