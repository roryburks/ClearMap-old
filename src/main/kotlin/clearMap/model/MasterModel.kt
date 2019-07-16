package clearMap.model

import clearMap.hybrid.Hybrid
import clearMap.model.commands.CentralCommandExecutor
import clearMap.model.commands.ICentralCommandExecutor
import clearMap.model.map.CwMapSpace
import clearMap.model.map.ICwMapSpace
import clearMap.model.tools.ToolsModel
import clearMap.model.view.ViewSchemaModel
import clearMap.ui.dialog.Dialog
import clearMap.ui.dialog.IDialog
import clearMap.ui.resources.ILogger
import clearMap.ui.systems.HotkeyManager
import clearMap.ui.systems.IHotkeyManager
import clearMap.ui.systems.IPreferences
import clearMap.ui.systems.JPreferences
import sgui.components.IComponentProvider

interface  IMasterModel {
    val preferences: IPreferences

    val viewSchema : ViewSchemaModel
    val tools: ToolsModel

    val mapSpace : ICwMapSpace
    val commandExecutor: ICentralCommandExecutor
    val hotkeyManager : IHotkeyManager
    val dialog : IDialog
}

class MasterModel(
    logger: ILogger = Hybrid.logger,
    ui: IComponentProvider = Hybrid.ui)
    : IMasterModel
{
    override val preferences = JPreferences(MasterModel::class.java)

    override val viewSchema: ViewSchemaModel = ViewSchemaModel()
    override val tools: ToolsModel = ToolsModel()

    override val mapSpace: ICwMapSpace = CwMapSpace()
    override val commandExecutor: ICentralCommandExecutor = CentralCommandExecutor(this, logger)
    override val hotkeyManager : IHotkeyManager = HotkeyManager(preferences)
    override val dialog: IDialog = Dialog(this, ui)
}