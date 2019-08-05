package clearMap.ui.views.tile

import clearMap.model.IMasterModel
import clearMap.ui.systems.omniContainer.IOmniComponent
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sguiSwing.SwIcon

class TileListView(
    private val _master: IMasterModel,
    private val _ui: IComponentProvider)
    : IOmniComponent
{
    private val _panel = _ui.CrossPanel()

    override val component: IComponent get() = _panel
    override val icon: SwIcon? get() =  null
    override val name: String get() = "Tile Sheets"
}