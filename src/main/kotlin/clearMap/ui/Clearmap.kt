package clearMap.ui

import clearMap.ui.resources.setupSwGuiStuff
import rbJvm.vectrix.SetupVectrixForJvm


fun main( args: Array<String>) {
    try {
        SetupVectrixForJvm()
        setupSwGuiStuff()
        println("Hi")
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
}