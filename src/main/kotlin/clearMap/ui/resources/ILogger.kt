package clearMap.ui.resources

import kotlin.Exception

interface ILogger {
    fun logInfo(msg: String)
    fun logWarning(msg: String, exception: Exception? = null)
    fun logError(msg: String, exception: Exception? = null)
}

object ConsoleLogger : ILogger{
    override fun logInfo(msg: String) {
        println("[I] : $msg")
    }

    override fun logWarning(msg: String, exception: Exception?) {
        println("[W] : $msg")
        exception?.printStackTrace()
    }

    override fun logError(msg: String, exception: Exception?) {
        println("[E] : $msg")
        exception?.printStackTrace()
    }

}