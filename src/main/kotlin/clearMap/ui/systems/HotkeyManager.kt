package clearMap.ui.systems

import clearMap.model.commands.KeyCommand
import rb.extendo.dataStructures.MutableOneToManyMap
import java.awt.event.InputEvent


data class Hotkey(
    val key: Int,
    val modifier: Int
)


interface IHotkeyManager {
    fun getCommand( key: Hotkey) : KeyCommand?
    fun getHotkeysForCommand( command: String) : List<Hotkey>?

    fun setCommand( key: Hotkey, command : KeyCommand)
    fun removeHotkey( key: Hotkey)
}

private const val ALT = InputEvent.ALT_DOWN_MASK
private const val CTRL = InputEvent.CTRL_DOWN_MASK
private const val SHIFT = InputEvent.SHIFT_DOWN_MASK

private val defaultHotkeys = mapOf<KeyCommand,Hotkey>()

class HotkeyManager(
    val preferences: IPreferences,
    defaultHotkeys: Map<KeyCommand, Hotkey>) : IHotkeyManager {
    private val hotkeyMap = MutableOneToManyMap<KeyCommand, Hotkey>()

    init {
        // Load either Default or saved preferences
        defaultHotkeys.entries.forEach {
            val command = it.key
            val defaultHotkey = it.value

            val preferenceString = preferences.getString(command.commandString)
            if( preferenceString == null) {
                hotkeyMap.assosciate(defaultHotkey, command)
            }
            else if( !preferenceString.isBlank()) {
                preferenceStringToHotkeys(preferenceString)
                    .forEach { hotkey -> hotkeyMap.assosciate(hotkey, command) }
            }
        }
    }

    override fun getCommand(key: Hotkey): KeyCommand? = hotkeyMap.getOne(key)
    override fun getHotkeysForCommand(command: String): List<Hotkey>? = hotkeyMap.getMany(KeyCommand(command))
    override fun setCommand(key: Hotkey, command: KeyCommand) {
        val oldCommand = hotkeyMap.getOne(key)

        hotkeyMap.assosciate(key, command)

        // Change Old
        if( oldCommand != null) {
            val oldMany = hotkeyMap.getMany(oldCommand)
            if( oldMany == null)
                preferences.putString(oldCommand.commandString,"")
            else
                preferences.putString(oldCommand.commandString, hotkeysToPreferenceString(oldMany))
        }

        // Change New
        val newCommandsRewritten = hotkeyMap.getMany(command)
        if( newCommandsRewritten != null)
            preferences.putString(command.commandString, hotkeysToPreferenceString(newCommandsRewritten))

    }
    override fun removeHotkey(key: Hotkey) {
        val command = hotkeyMap.many_to_one[key]
        hotkeyMap.dissociate(key)

        if( command != null) {
            val hotkeysToRewrite = hotkeyMap.getMany( command)
            if( hotkeysToRewrite == null)
                preferences.putString(command.commandString,"")
            else
                preferences.putString(command.commandString, hotkeysToPreferenceString(hotkeysToRewrite))
        }
    }

    private fun hotkeysToPreferenceString( keys: List<Hotkey>) : String {
        return keys.map { "${it.key},${it.modifier}" }
            .joinToString (";")
    }

    private fun preferenceStringToHotkeys( prefString : String) : List<Hotkey> {
        return prefString.split(";")
            .map {
                val params = it.split(",")
                Hotkey( params[0].toInt(10), params[1].toInt(10))
            }
    }
}

