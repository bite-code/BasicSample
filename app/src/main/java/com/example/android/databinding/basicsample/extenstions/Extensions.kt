package com.example.android.databinding.basicsample.extenstions

fun String.toString() : String {

    if(this == null) return "null"
    System.out.println("toString from extension method!")
    return toString()

}

fun String.esmesh() : String {

    if(this == null) return "null"
    System.out.println("toString from extension method!")
    return toString()

}

val <T> List<T>.lastItem: Int
    get() = size -1

class Host(val hostname: String) {
    fun printHostname() { print(hostname) }
}

class Connection(val host: Host, val port: Int) {
    fun printPort() { print(port) }

    fun Host.printConnectionString() {
        printHostname()   // calls Host.printHostname()
        print(":")
        printPort()   // calls Connection.printPort()
    }

    fun Host.getConnectionToString(): String {
        toString()
        //If we want to call a method that has same name in class we can call upper method like this.
        this@Connection.toString()
        return ""
    }


    fun connect() {
        /*...*/
        host.printConnectionString()   // calls the extension function
    }
}

fun Connection.toString():String {
    return "connection"

}

fun Host.toString(): String {
    return "Host"
}


fun main() {
    Connection(Host("kotl.in"), 443).connect()
//    Host("kotl.in").printConnectionString(443)  // error, the extension function is unavailable outside Connection
}