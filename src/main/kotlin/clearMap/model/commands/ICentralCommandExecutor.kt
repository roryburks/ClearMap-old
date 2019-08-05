package clearMap.model.commands

import clearMap.hybrid.Hybrid
import clearMap.model.master.IMasterModel
import clearMap.ui.resources.ILogger
import rb.extendo.extensions.toHashMap

interface ICommand {
    val commandString : String
    val keyCommand: KeyCommand
}


class KeyCommand(
    val commandString : String,
    val objectCreator: ((IMasterModel)->Any?)?  = null
)
{
    // region generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KeyCommand
        if (commandString != other.commandString) return false
        return true
    }

    override fun hashCode(): Int {
        return commandString.hashCode()
    }
    // endregion
}

interface ICentralCommandExecutor {
    val commandDomains : List<String>
    val validCommands: List<String>
    fun executeCommand( command: String, extra: Any?) : Boolean
}

class CentralCommandExecutor(
    master: IMasterModel,
    private val _logger: ILogger = Hybrid.logger
)
    : ICentralCommandExecutor {

    private val commandExecutors: Map<String, ICommandExecutor> =
        listOf<ICommandExecutor>(GlobalCommandExecutor(master))
            .toHashMap { it.domain }

    override val commandDomains: List<String> get() = commandExecutors.keys.toList()
    override val validCommands: List<String> get() = commandExecutors.values.flatMap { it.validCommands }

    override fun executeCommand(command: String, extra: Any?): Boolean {
        var executed = false
        Hybrid.gle.runInGLContext {
            val space = command.substring(0, command.indexOf("."))
            val subCommand = command.substring(space.length + 1)

            var attempted = false

            commandExecutors[space]?.run {
                attempted = true
                if (executeCommand(subCommand, extra))
                    executed = true
            }

            if (!attempted) {
                Hybrid.logger.logWarning("Unrecognized command domain: $space")
            }
        }
        return executed
    }
}
