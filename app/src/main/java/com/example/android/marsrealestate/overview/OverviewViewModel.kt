/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsAPI
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//create MarsAPIStatus enum with the LOADING,ERROR and Done states
enum class MarsAPIStatus{LOADING,ERROR,DONE}
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsAPIStatus>()

    // The external immutable LiveData for the request status String
    val response: LiveData<MarsAPIStatus>
        get() = _status

    //add the LiveData MarsProperty property with an internal Mutable and an external LiveData
    private val _properties = MutableLiveData<List<MarsProperty>>()

    val properties:LiveData<List<MarsProperty>>
    get() = _properties


    //add a _navigationtoSelectedProperty MutableLiveData externalized as LiveData
    private val _nagivateToSelectedProperty = MutableLiveData<MarsProperty>()
    val nagivateToSelectedProperty:LiveData<MarsProperty>
    get() = _nagivateToSelectedProperty

    //Create a coroutine job and a coroutineScope using the Main Dispatcher
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob+ Dispatchers.Main)



    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {

        //call coroutinescope.launch and place the rest of the code in it
        coroutineScope.launch {

            //Get the Deferred object for our retrofit request
            //call the MarsAPI to get data
            var getPropertiesDeferred = MarsAPI.retrofitService.getProperties()

            try {

                _status.value = MarsAPIStatus.LOADING

                var listResult = getPropertiesDeferred.await()
                _status.value = MarsAPIStatus.DONE
                _properties.value = listResult


            }catch (t:Throwable){
                _status.value = MarsAPIStatus.ERROR
                //to clear recyclerview item
                _properties.value = ArrayList()
            }
        }

        //_response.value = "Set the Mars API Response here!"
    }

    // add displayPropertyDetails and displayPropertyDetailComplete Methods
    fun displayPropertyDetails(marsProperty: MarsProperty){
        _nagivateToSelectedProperty.value = marsProperty
    }

    fun displayPropertyDetailsComplete(){
        _nagivateToSelectedProperty.value = null
    }

    override fun onCleared() {
        super.onCleared()
        //cancel job when viewmodel is distroyed
        viewModelJob.cancel()
    }
}
