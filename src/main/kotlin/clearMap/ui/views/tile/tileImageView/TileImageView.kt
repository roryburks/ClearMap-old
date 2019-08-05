package clearMap.ui.views.tile.tileImageView

import clearMap.gl.SpecialDrawerFactory
import clearMap.hybrid.Hybrid
import clearMap.model.map.CwTilePolySegment
import clearMap.model.map.CwTileRectSegment
import clearMap.model.master.IMasterModel
import clearMap.model.penner.IPennerContext
import clearMap.model.penner.TilePenner
import clearMap.model.penner.ViewSpace
import clearMap.ui.systems.omniContainer.IOmniComponent
import clearMap.ui.views.mapArea.IJoglAreaPanelContext
import clearMap.ui.views.mapArea.JoglAreaPanel
import rb.glow.color.Colors
import rb.glow.gle.GLGraphicsContext
import rb.vectrix.mathUtil.f
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sguiSwing.SwIcon

class TileImageView (
    private val _master: IMasterModel,
    private val _ui: IComponentProvider
)
    : IOmniComponent, IPennerContext
{
    val penner = TilePenner(_master, this, Hybrid.keypressSystem)
    private var _view = ViewSpace()

    private val _panel = _ui.CrossPanel()
    private val _drawPanel = JoglAreaPanel(
        penner,
        object : IJoglAreaPanelContext {
            override fun draw(glgc: GLGraphicsContext, w: Int, h: Int) {
                val drawer = SpecialDrawerFactory.makeSpecialDrawer(glgc)
                drawer.drawTransparencyBg(0,0,w, h, 8)

                glgc.color = Colors.RED

                glgc.transform = _view.tViewToScreen

                val map = _master.mapSpace.mapsBind.currentlySelected
                val tile = map?.tilesBind?.currentlySelected
                if( tile != null)
                {
                    glgc.renderImage(tile.img, 0, 0)
                    glgc.drawRect(0,0,tile.img.width, tile.img.height)

                    glgc.color = Colors.BLUE
                    tile.tileSegments.forEach {
                        when(it) {
                            is CwTileRectSegment -> {
                                glgc.drawRect(it.bounds.x1i,it.bounds.y1i, it.bounds.wi, it.bounds.hi)
                            }
                            is CwTilePolySegment -> {
                                TODO()
                            }
                        }
                    }
                }


                if( penner.drawsOverlay) {
                    glgc.transform = view.tViewToScreen
                    penner.drawOverlay(glgc, view)
                }
            }
        }
    )

    init {
        _panel.setLayout {
            rows.add(_drawPanel, flex = 1f)
        }
    }


    override val view: ViewSpace get() = _view
    override fun redraw() {_drawPanel.redraw()}
    override fun refreshCoordinates(x: Int, y: Int) {}

    override val component: IComponent get() = _panel
    override val icon: SwIcon? get() =  null
    override val name: String get() = "Tile Editor"
}