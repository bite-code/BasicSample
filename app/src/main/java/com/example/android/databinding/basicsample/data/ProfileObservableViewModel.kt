/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.databinding.basicsample.data

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.databinding.basicsample.util.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * This class is used as a variable in the XML layout and it's fully observable, meaning that
 * changes to any of the exposed observables automatically refresh the UI. *
 */
class ProfileLiveDataViewModel : ViewModel() {
    private val _name = MutableLiveData("Ada")
    private val _lastName = MutableLiveData("Lovelace")
    private val _likes = MutableLiveData(0)
    private val _nameLiveData = Transformations.map(_likes) {
        "Mahdi $it SAMA"
    }

    //    private val _nameLiveData = MutableLiveData<String>().apply {
//        value="Mahdi  SAMA"
//    }
    val name: LiveData<String> = _name
    val lastName: LiveData<String> = _lastName
    val likes: LiveData<Int> = _likes
    val nameWithSama: LiveData<String> = Transformations.map(_nameLiveData) {
        addNameSama(it)
    }

    // popularity is exposed as LiveData using a Transformation instead of a @Bindable property.
    val popularity: LiveData<Popularity> = Transformations.map(_likes) {
        when {
            it > 9 -> Popularity.STAR
            it > 4 -> Popularity.POPULAR
            else -> Popularity.NORMAL
        }
    }

    val pupularityNameLieData: LiveData<String> = Transformations.map(_likes) {
        when {

            it > 9 -> "Balaye 9"
            it > 4 -> "Balaye 4"
            else -> "*_*"
        }

    }


    fun onLike() {
        _likes.value = (_likes.value ?: 0) + 1
    }


    fun addNameSama(name: String): String = "$name sama"

    val TAG = "Aliakbar"

    val handler = CoroutineExceptionHandler { _, exception ->
        println("$TAG  exception: $exception")

    }

    fun doSomeCoroutineTest(token: String) {

        //This will block ui thread
//        runBlocking {
//        insideMethod(token)
//
//        }

        //This will not block any thingh
        GlobalScope.launch {
//            insideMethod(token)

            System.out.println("$TAG  enter_viewMdeol $token")
            delay(1000)
            launch {
                System.out.println("$TAG  * enter $token")
                delay(5000)
                System.out.println("$TAG  * end $token")
            }
            System.out.println("$TAG  end_viewMdeol $token")


        }


    }

    override fun onCleared() {
        println("$TAG  ----- onCleared")
        super.onCleared()
    }

    private suspend fun insideMethod(token: String) {
        System.out.println("$TAG  enter_viewMdeol $token")
        delay(1000)


        //same to with context both locks
//        coroutineScope {
//            System.out.println("$TAG  * enter $token")
//            delay(5000)
//            System.out.println("$TAG  * end $token")
//        }

//        withContext(Dispatchers.IO) {
//
//            System.out.println("$TAG  * enter $token")
//            delay(5000)
//            System.out.println("$TAG  * end $token")
//        }
        System.out.println("$TAG  end_viewMdeol $token")

    }

}

/**
 * As an alternative to LiveData, you can use Observable Fields and binding properties.
 *
 * `Popularity` is exposed here as a `@Bindable` property so it's necessary to call
 * `notifyPropertyChanged` when any of the dependent properties change (`likes` in this case).
 */
class ProfileObservableViewModel : ObservableViewModel() {
    val name = ObservableField("Ada")
    val lastName = ObservableField("Lovelace")
    val likes = ObservableInt(0)

    fun onLike() {
        likes.increment()
        // You control when the @Bindable properties are updated using `notifyPropertyChanged()`.
//        notifyPropertyChanged(BR.popularity)
    }

    @Bindable
    fun getPopularity(): Popularity {
        return likes.get().let {
            when {
                it > 9 -> Popularity.STAR
                it > 4 -> Popularity.POPULAR
                else -> Popularity.NORMAL
            }
        }
    }
}

enum class Popularity {
    NORMAL,
    POPULAR,
    STAR
}


private fun ObservableInt.increment() {
    set(get() + 1)
}


private fun ObservableInt.dotaDota() {
    set(get() + 10)
}
