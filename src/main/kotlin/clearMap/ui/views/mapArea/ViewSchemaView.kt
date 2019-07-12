package clearMap.ui.views.mapArea

import clearMap.model.IMasterModel
import clearMap.ui.resources.SpiriteIcons
import clearMap.ui.systems.omniContainer.IOmniComponent
import rb.IContract
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sgui.components.IToggleButton
import sgui.components.crossContainer.CrossInitializer
import sguiSwing.SwIcon

class ViewSchemaView (
    private val _master: IMasterModel,
    val ui: IComponentProvider) : IOmniComponent
{
    private val _panel = ui.CrossPanel()
    private val _model get() = _master.viewSchema

    override val component: IComponent get() = _panel
    override val icon: SwIcon? get() = null
    override val name: String get() = "View Schema"

    private val _btnCollision = ui.ToggleButton(_model.collisionVis)
    private val _btnTiles = ui.ToggleButton(_model.tilesVis)
    private val _btnZones = ui.ToggleButton(_model.zonesVis)
    private val _btnActors = ui.ToggleButton(_model.actorsVis)
    private val _btnGrid = ui.ToggleButton(_model.gridVis)
    private val _tfGridX = ui.IntField(allowsNegative = false)
    private val _tfGridY = ui.IntField(allowsNegative = false)

    init  /* Look */{
        _btnGrid.setOnIcon(SpiriteIcons.SmallIcons.Rig_VisibleOn)
        _btnGrid.setOffIcon(SpiriteIcons.SmallIcons.Rig_VisibleOff)
        _btnGrid.setBasicBorder(IComponent.BasicBorder.BEVELED_RAISED)

        fun addNormalButton(button: IToggleButton, label: String, init: CrossInitializer) {
            button.setOnIcon(SpiriteIcons.BigIcons.VisibleOn)
            button.setOffIcon(SpiriteIcons.BigIcons.VisibleOff)
            button.setBasicBorder(IComponent.BasicBorder.BEVELED_RAISED)

            init.rows += {
                add(button, 24, 24)
                addGap(3)
                add(ui.Label(label))
                height = 24
            }
        }

        _panel.setLayout {
            addNormalButton(_btnCollision, "Collision", this)
            addNormalButton(_btnTiles, "Tiles", this)
            addNormalButton(_btnZones, "Zones", this)
            addNormalButton(_btnActors, "Actors", this)

            rows += {
                add(_btnGrid, 12,12)
                addGap(1)
                add(ui.Label("Grid"))
            }
            rows += {
                add(_tfGridX, 48, 16)
                add(ui.Label("x"))
                add(_tfGridY, 48, 16)
                add(ui.Label("y"))
            }

        }
    }

    // Bindings
    private val _ks : List<IContract>
    init {
        val ks = mutableListOf<IContract>()

        ks.add(_btnCollision.checkBind.bindTo(_model.collisionVisBind))
        ks.add(_btnTiles.checkBind.bindTo(_model.tilesVisBind))
        ks.add(_btnZones.checkBind.bindTo(_model.zonesVisBind))
        ks.add(_btnActors.checkBind.bindTo(_model.actorsVisBind))
        ks.add(_btnGrid.checkBind.bindTo(_model.gridVisBind))
        ks.add(_tfGridX.valueBind.bindTo(_model.gridXBind))
        ks.add(_tfGridY.valueBind.bindTo(_model.gridYBind))

        _ks = ks
    }

    override fun close() {
        _ks.forEach { it.void() }
    }
}