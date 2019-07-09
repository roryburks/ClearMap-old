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

class CentralCommandExecutor(val master: IMasterModel) : ICentralCommandExecutor{
    override val commandDomains: List<String>
        get() = TODO("not implemented")
    override val validCommands: List<String>
        get() = TODO("not implemented")

    override fun executeCommand(command: String, extra: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}