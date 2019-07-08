package clearMap.gl

import clearMap.ui.resources.ILogger
import clearMap.ui.resources.IScriptService
import java.io.IOException
import java.util.*

class JClassScriptService(private val _logger: ILogger) : IScriptService {
    override fun loadScript(scriptName: String): String {
        try {
            var ret = ""
            JClassScriptService::class.java.classLoader.getResource(scriptName).openStream().use {
                val scanner = Scanner(it)
                scanner.useDelimiter("\\A")
                ret = scanner.next()
            }
            return ret
        }catch( e: IOException) {
            _logger.logError("Couldn't load shader script file: [$scriptName]", e)
            return ""
        }
    }
}