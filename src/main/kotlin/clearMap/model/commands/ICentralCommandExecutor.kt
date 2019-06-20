package clearMap.model.commands

import clearMap.model.IMasterModel

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