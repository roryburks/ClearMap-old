package clearMap.ui.views.tile

import clearMap.model.master.IMasterModel
import clearMap.ui.systems.omniContainer.IOmniComponent
import rb.owl.bindable.addObserver
import sgui.components.IBoxList
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sguiSwing.SwIcon

private const val BUTTON_WIDTH = 24

class TileToolView (
    private val _master: IMasterModel,
    private val _ui: IComponentProvider)
: IOmniComponent
{
    override val component: IComponent get() = _panel
    override val icon: SwIcon? get() =  null
    override val name: String get() = "Tools"

    private val _panel = _ui.CrossPanel()
    private val _boxList = _ui.BoxList(BUTTON_WIDTH, BUTTON_WIDTH, _master.tools.tile.tools)


    init {
        _boxList.enabled = false
        _boxList.renderer = {tool ->
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

        _panel.setLayout {
            rows.add(_boxList, flex = 1f)
            rows.flex = 1f
        }
    }

    // Bindings
    private val _modelToViewK = _master.tools.tile.selectedToolBinding.addObserver { new, _ ->
        _boxList.data.selected = new
    }

    override fun close() {
        _modelToViewK.void()
    }
}