package clearMap.ui.resources

import java.io.IOException
import java.util.*

interface IScriptService {
    fun loadScript( scriptName: String) : String
}

class JClassScriptService(private val _logger: ILogger) : IScriptService {
    override fun loadScript(scriptName: String): String {
        var ret = ""
        try {
            JClassScriptService::class.java.classLoader.getResource(scriptName).openStream().use {
                val scanner = Scanner(it)
                scanner.useDelimiter("\\A")
                ret = scanner.next()
            }
        }catch( e: IOException) {
            _logger.logError("Couldn't load shader script file: [$scriptName]", e)
        }
        return ret
    }
}