package clearMap.model

import clearMap.hybrid.Hybrid
import clearMap.model.commands.CentralCommandExecutor
import clearMap.model.commands.ICentralCommandExecutor
import clearMap.model.map.CwMapSpace
import clearMap.model.map.ICwMapSpace
import clearMap.model.view.ViewSchemaModel
import clearMap.ui.resources.ILogger
import clearMap.ui.systems.HotkeyManager
import clearMap.ui.systems.IHotkeyManager
import clearMap.ui.systems.IPreferences
import clearMap.ui.systems.JPreferences

interface  IMasterModel {
    val preferences: IPreferences
    val mapSpace : ICwMapSpace
    val commandExecutor: ICentralCommandExecutor
    val viewSchema : ViewSchemaModel
    val hotkeyManager : IHotkeyManager
    //val dialog : IDialog
}

class MasterModel(
) : IMasterModel{
    override val preferences = JPreferences(MasterModel::class.java)
    override val mapSpace: ICwMapSpace = CwMapSpace()
    override val commandExecutor: ICentralCommandExecutor = CentralCommandExecutor(this)
    override val viewSchema: ViewSchemaModel = ViewSchemaModel()
    override val hotkeyManager : IHotkeyManager = HotkeyManager(preferences)
}