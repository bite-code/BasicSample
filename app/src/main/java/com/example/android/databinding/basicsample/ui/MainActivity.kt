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

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.android.databinding.basicsample.R
import com.example.android.databinding.basicsample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.time.LocalDateTime

/**
 * There are lots of ways call coroutine:
 * @GlobalScope{} It is used when you want to have a application scoped lifecycler job
 * @runBlocking{} When you want to block current thread with this corountin and good for start of jobs in libs or classes
 * @CoroutinScope{} good for do parallel jobs and scope is heriarichied from parent and block till it own is ends
 * @lucnh{} One new coroutin without blocking others.
 * @Async{} Like lunch but has deffered return type like features!
 * @withContext{} Like Async has result but blocks the thread.
 */
class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The layout for this activity is a Data Binding layout so it needs to be inflated using
        // DataBindingUtil.
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main)

        // The returned binding has references to all the Views with an ID.
        binding.observableFieldsActivityButton.setOnClickListener {
            startActivity(Intent(this, ObservableFieldActivity::class.java))
        }
        binding.viewmodelActivityButton.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }


//        val job1 = GlobalScope.launch { // launch a new coroutine and keep a reference to its Job
//            delay(1000L)
//            println("Mahdi:World!")
//        }
//        val job2 = GlobalScope.launch { // launch a new coroutine and keep a reference to its Job
//            for (i in 1..10){
//                delay(500L)
//                println("Mahdi:from loop! "+i)
//
//            }
//        }

//        for (i in 1..1000_0000) coroutineTest(i)

//        cancelTest()
//        customScopeTest()
//        testAsync()


        val mahdi = Person("Mahdi")

        with(mahdi) {
            name = "ALi"
        }
        println("ALi $mahdi")

        val hassan = mahdi.apply {
            name = "Asghar"
        }.also { it.name = "Akbar" }.also { it.name = "Hassan" }

        println("mahdi: $mahdi")
        println("hassan: $hassan")

        mahdi.run {
            name = "Mahdi"

        }

        //result
        val name = mahdi.let {

            "dsd"
        }

        val name2 = with(mahdi) {
            "with"
        }



        println("name $name")
        println("name 2 $name2")
        println("ALi $mahdi")

        mahdi.also {
            it.name = "Aliakbar"
        }.also {
            it.name = it.name[0] + ""
        }

        mahdi.apply { }

        println("ALi $mahdi")


//        coroutineBlockingTest2()
//        coroutineBlockingTest1(1)
//        cancelTest()
//        testCoroutinScoopes()


    }

    val TAG = "Aliakbar"


    private fun testCoroutinScoopes() {
        runBlocking {


            coroutineScope {

                System.out.println("$TAG 1- enter")
                delay(1000)
                System.out.println("$TAG 1- finish")


            }
            coroutineScope {

                System.out.println("$TAG 2- enter")
                delay(500)
                System.out.println("$TAG 2- finish")


            }

            launch {
                System.out.println("$TAG 3- enter")
                delay(300)
                System.out.println("$TAG 3- finish")
            }
            launch {
                System.out.println("$TAG 4- enter")
                delay(200)
                System.out.println("$TAG 4- finish")
            }
            launch {
                System.out.println("$TAG 5- enter")
                delay(100)
                System.out.println("$TAG 5- finish")
            }



            launch {
                delay(10000)

                System.out.println("$TAG ***** all finish")

            }
        }
    }

    private fun coroutineBlockingTest2() {

        GlobalScope.launch(Dispatchers.IO) {


            val result1 = withContext(Dispatchers.Default) {
                delay(1000)
                println("Mahdi 1-Thread: ${Thread.currentThread().name}")
                1000
            }


            val result4 = launch {
                for (i in 1..10) {
                    delay(500)
                    println("Mahdi 4-inside launch $i")
                }
                2
            }

            println("Mahdi point zero +result4=$result4")


            //With context suspend blocks parent till this done ( it can block from this line not before lines!) SO you can see point 4 and point 2 runing with each other but point 3 with happens just after this block ends.
            val result2 = withContext(Dispatchers.Default) {
                for (i in 1..10) {
                    delay(500)
                    println("Mahdi 2-Thread: ${Thread.currentThread().name}")

                }
                500
            }

            val result3 = async {
                delay(250)
                println("Mahdi 3-Thread: ${Thread.currentThread().name}")
                50
            }


            println("*** Mahdi result =$result1 , $result2 ")
            println("*** Mahdi result =$result1 , $result2 ,${result3.await()}")

        }
    }

    data class Person(var name: String)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun testAsync() {

        runBlocking {


            coroutineScope {

                val firstValue = async(Dispatchers.IO) {

                    delay(20000)
                    println("Mahdi 1-done!")
                    5
                }

                val secondValue = async {

                    delay(500)
                    println("Mahdi 2-done!")
                    10
                }


//                firstValue.cancel()
                //Await blocks coroutine till responce comes
                println("Mahdi first:${firstValue.await()}-" + LocalDateTime.now())
                println("Mahdi second:${secondValue.await()}-" + LocalDateTime.now())
                println("Mahdi sum" + (firstValue.await() + secondValue.await()) + " " + LocalDateTime.now())
            }

        }

    }

    fun functiinSample(){

    }


    private fun cancelTest() {

        runBlocking {

            val job = launch(Dispatchers.Default + handler) {
                repeat(10) { i ->
                    println("Mahdi job: I'm sleeping $i...")
                    delay(50L)
                }
                coroutineBlockingTest2()
            }

            try {

                delay(130)
                println("Mahdi main: I'm tired of waiting")
                job.cancel()
//                job.cancelAndJoin()
//            job.join()  //It is like you used coroutine scoope, it will wait till it is done.
                println("Mahdi main: Exit")

            } catch (e: CancellationException) {
                println("Mahdi CancellationException")

            } catch (e: Exception) {
                println("Mahdi Exception")

            }


        }
    }

    fun coroutineBlockingTest1(i: Int) = runBlocking {

        //This block , blocks till all sub task is overed
        coroutineScope {
            // Creates a coroutine scope
            launch {
                delay(50L)
                println("Mahdi:Task from nested launch -----1")
            }

            delay(10L)
            println("Mahdi:Task from coroutine scope -----0") // This line will be printed before the nested launch
        }

        coroutineScope {
            delay(10L)
            println("Mahdi:Task from coroutine scope -----2") // This line will be printed before the nested launch

        }

        //Non blocking
        launch {
            delay(10L)
            println("Mahdi:Task from runBlocking ----5")
        }

        runBlocking {
            println("Mahdi:Task from runBlocking ----3")

        }

        println("Mahdi:Coroutine scope is over ------4" + i) // This line is not printed until the nested launch completes
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
}
