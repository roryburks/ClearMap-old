package clearMap.ui.views.tools

import clearMap.hybrid.Hybrid
import clearMap.model.IMasterModel
import clearMap.ui.systems.omniContainer.IOmniComponent
import rb.owl.bindable.addObserver
import sgui.components.IBoxList
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sguiSwing.SwIcon

private const val BUTTON_WIDTH = 24

class CollisionObjectToolPanel
private constructor(
    private val _master: IMasterModel,
    private val _ui : IComponentProvider,
    private val _imp : IBoxList<Tool>)
    : IComponent by _imp
{
    constructor(master: IMasterModel, ui: IComponentProvider) : this(
        master,
        ui,
        ui.BoxList(BUTTON_WIDTH, BUTTON_WIDTH, master.tools.collision.tools))

    init {
        _imp.enabled = false
        _imp.renderer = {tool ->
            object : IBoxList.IBoxComponent {
                override val component: IComponent = _ui.Button(tool.description).also {
                    it.action = {_master.tools.collision.selectedTool = tool}
                }

                override fun setSelected(selected: Boolean) {
                    component.setBasicBorder(
                        if( selected) IComponent.BasicBorder.BEVELED_RAISED
                        else IComponent.BasicBorder.BEVELED_LOWERED)
                }
            }
        }
    }

    private val _modelToViewK = _master.tools.collision.selectedToolBinding.addObserver { new, _ ->
        _imp.data.selected = new
    }
}