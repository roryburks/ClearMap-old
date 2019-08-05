package clearMap.ui.views.tools

import clearMap.model.master.IMasterModel
import clearMap.ui.systems.omniContainer.IOmniComponent
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sgui.components.crossContainer.ICrossPanel
import sguiSwing.SwIcon

class ToolPanel
private constructor(
    private val _master: IMasterModel,
    private val _ui: IComponentProvider,
    private val _imp: ICrossPanel)
    :IOmniComponent
{
    constructor(master: IMasterModel, ui:IComponentProvider) :
            this( master, ui, ui.CrossPanel())

    override val component: IComponent get() = _imp
    override val icon: SwIcon? get() = null
    override val name: String get() = "Tools"

    init {
        _imp.setLayout {
            rows.add(CollisionObjectToolPanel(_master, _ui))
        }
    }
}