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

package com.example.android.databinding.basicsample.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import com.example.android.databinding.basicsample.R
import com.example.android.databinding.basicsample.data.ProfileLiveDataViewModel
import com.example.android.databinding.basicsample.databinding.ViewmodelProfileBinding

/**
 * This activity uses a [android.arch.lifecycle.ViewModel] to hold the data and respond to user
 * actions. Also, the layout uses [android.databinding.BindingAdapter]s instead of expressions
 * which are much more powerful.
 *
 * @see com.example.android.databinding.basicsample.util.BindingAdapters
 */
class ViewModelActivity : AppCompatActivity() {

    val nameLiveData = MutableLiveData("Shabi")
    val nameWithSamaLiveData = Transformations.map(nameLiveData) {
        //        addNameSama(it)
        //todo: If my functions had defalt value we can leav it if it come after needed parameters
        //todo: there is a convention in kotlin if last input is a function we can write is like bellow:
        customFunction(it) {
            addNameSama(it)
        }

        //Todo: Or write like this as old way
        //ok customFunction(text = it,a = 0,qux = {addNameSama(it)})
        //Ok customFunction(it,a = 0,qux = {addNameSama(it)})
        //No! customFunction(text = it,0,qux = {addNameSama(it)}) //First item is named so second should be too
        //Ok customFunction(it){ addNameSama(it) }
        //Ok customFunction(){ addNameSama(it) }
        //No!
    }

    //todo Switch map is for condition we need get live data from a source like repository. But as you can see usage here is same. We can map or switch map

    val switchMapLiveData = Transformations.switchMap(nameLiveData) {
        MutableLiveData(addNameSama(it))
    }
    val nameList = arrayListOf("Mahdi", "Ali", "shaby")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain ViewModel from ViewModelProviders
        val viewModel = ViewModelProviders.of(this).get(ProfileLiveDataViewModel::class.java)

        // An alternative ViewModel using Observable fields and @Bindable properties can be used:
        // val viewModel = ViewModelProviders.of(this).get(ProfileObservableViewModel::class.java)

        // Obtain binding
        val binding: ViewmodelProfileBinding =
                DataBindingUtil.setContentView(this, R.layout.viewmodel_profile)

        // Bind layout with ViewModel
        binding.viewmodel = viewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        nameWithSamaLiveData.observe(this, Observer<String> { t -> System.out.println("Mahdi Observer = " + t) })

        //todo : just last item will emmit.
//        nameLiveData.postValue("Mahdi")
//        nameLiveData.value = "Aliakbar"
//        nameLiveData.value = "Parviz"

        //todo Remember! liveData has a threshold that can setValue too fast and will get last item. It should be some time even 0.001 second!
        nameList.forEach {
            Handler().postDelayed({

                nameLiveData.value = it
            }, 500)
        }

        foo(x = "dsd", y = 0)
        val list = arrayOf("Peyman","Hamid","Mammad")
        variableInout("Mahdi","Shaby",*list,"Ali","Maman") //In vararg if we want to add an existing list of that type, we can use by *

        viewModel.doSomeCoroutineTest("Mahdi")

    }

    fun addNameSama(name: String): String = name + " sama"

    //todo: Function with function input
    fun customFunction(text: String = "Mahdi", a: Int = 0, qux: (text: String) -> String): String {

        return qux(text)
    }

    fun foo(x: String, y: Int) {

    }

    override fun onStop() {
        println("Aliakbar onActivity stop")
        super.onStop()
    }
    fun variableInout(vararg strings: String) {
        for (string in strings) {
            System.out.println("varargs: $string")

        }
    }

    fun <T> asList(vararg ts: T): List<T> {
        val result = ArrayList<T>()
        for (t in ts) // ts is an Array
            result.add(t)
        return result
    }
}
