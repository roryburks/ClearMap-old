package clearMap.ui.views.mapArea

import clearMap.hybrid.Hybrid
import clearMap.model.IMasterModel
import clearMap.model.map.CwMap
import clearMap.model.penner.Penner
import clearMap.model.penner.ViewSpace
import clearMap.ui.systems.omniContainer.IOmniComponent
import rb.owl.bindable.addObserver
import rb.vectrix.linear.Vec2f
import rb.vectrix.mathUtil.f
import rb.vectrix.mathUtil.round
import sgui.Orientation
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sgui.components.crossContainer.ICrossPanel
import sguiSwing.SwIcon
import sguiSwing.components.SwPanel
import java.awt.Font
import javax.swing.SwingUtilities
import kotlin.math.min

private const val scrollRatio = 10
private const val scrollBuffer = 100

class MapSection (
    private val _master: IMasterModel,
    private val _ui : IComponentProvider)
    : IOmniComponent
{
    val panel : ICrossPanel = _ui.CrossPanel()


    override val component: IComponent get() = panel
    override val icon: SwIcon? get() = null
    override val name: String get() = "Map"

    val penner = Penner(_master, this, Hybrid.keypressSystem)

    val currentMap get() = _master.mapSpace.mapsBind.currentlySelected
    val currentView  get() = currentMap?.run { _viewMap[this]} ?: _defaultView
    private val _viewMap = mutableMapOf<CwMap, ViewSpace>()
    private val _defaultView = ViewSpace()


    // ==== UI Components ====
    private val workAreaContainer = _ui.CrossPanel()
    private val coordinateLabel = _ui.Label()
    private val messageLabel = _ui.Label()
    private val vScroll = _ui.ScrollBar(Orientation.VERTICAL, panel)
    private val hScroll = _ui.ScrollBar(Orientation.HORIZONTAL, panel)
    private val zoomPanel = SwPanel { g ->
        val view = currentView
        when {
            view.zoomLevel >= 0 -> {
                g.font = Font("Tahoma", Font.PLAIN, 12)
                g.drawString(Integer.toString(view.zoomLevel + 1), width - if (view.zoomLevel > 8) 16 else 12, height - 5)
            }
            else -> {
                g.font = Font("Tahoma", Font.PLAIN, 8)
                g.drawString("1/", this.width - 15, this.height - 10)
                g.font = Font("Tahoma", Font.PLAIN, 10)
                g.drawString(Integer.toString(-view.zoomLevel + 1), width - if (view.zoomLevel < -8) 10 else 8, height - 4)
            }
        }
    }

    init /* Components */ {
        vScroll.scrollWidth = 50
        hScroll.scrollWidth = 50

        val glWorkArea = JoglMapAreaPanel(penner, this, _master)
        workAreaContainer.setLayout { rows.add(glWorkArea) }

        hScroll.scrollBind.addObserver { new, _ -> currentView.offsetX = new * scrollRatio}
        vScroll.scrollBind.addObserver { new, _ -> currentView.offsetY = new * scrollRatio}
        workAreaContainer.onMouseWheelMoved = {
            doPreservingMousePoint(Vec2f(it.point.x.f, it.point.y.f)) {
                if( it.moveAmount > 0) currentView.zoomOut()
                if( it.moveAmount < 0) currentView.zoomIn()
                calibrateScrolls()
            }
        }

        workAreaContainer.onResize += {calibrateScrolls()}
        Hybrid.timing.createTimer(15, true) {Hybrid.gle.runInGLContext { penner.step() }}

        coordinateLabel.text = "Coordinate Label"
        messageLabel.text = "Message Label"

        panel.onResize += {
            calibrateScrolls()
            panel.redraw()
        }


        val barSize = 16
        panel.setLayout {
            rows += {
                add(workAreaContainer, flex = 200f)
                add(vScroll, width = barSize)
                flex = 200f
            }
            rows += {
                add(hScroll, flex = 200f)
                add(zoomPanel, width = barSize)
                height = barSize
            }
            rows += {
                add(coordinateLabel)
                addGap(0, 3, Int.MAX_VALUE)
                add(messageLabel)
                height = 24
            }
        }

    }

    // ==== Exposed Methods ====
    fun refreshCoordinates( x: Int, y: Int) {
        coordinateLabel.text = "$x,$y"
//        val map = currentMap
//        if( map  != null && RectD(0.0,0.0,map.width.d, map.height.d).contains(x.d, y.d))
//        else coordinateLabel.text = ""
    }

    // ==== Private Methods ====
    private fun calibrateScrolls(hs: Int = hScroll.scroll, vs: Int = vScroll.scroll) {
        val map = currentMap ?: return
        val view = currentView

        val viewWidth = workAreaContainer.width
        val viewHeight = workAreaContainer.height
        val hMin  = -viewWidth + scrollBuffer
        val vMin = -viewHeight + scrollBuffer
        val hMax = map.width * (view.zoom ) - scrollBuffer
        val vMax = map.height * (view.zoom) - scrollBuffer

        val ratio = scrollRatio.f
        hScroll.minScroll = Math.round(hMin / ratio)
        vScroll.minScroll = Math.round(vMin / ratio)
        hScroll.maxScroll = Math.round(hMax / ratio) + hScroll.scrollWidth
        vScroll.maxScroll = Math.round(vMax / ratio) + vScroll.scrollWidth

        hScroll.scroll = hs
        vScroll.scroll = vs
    }

    private fun doPreservingMousePoint(point: Vec2f, lambda: () -> Unit) {
        val view = currentView
        val pointInWorkspace = view.tScreenToView.apply(point)
        lambda.invoke()
        hScroll.scroll = ((pointInWorkspace.xf * view.zoom - point.xf ) / scrollRatio).round
        vScroll.scroll = ((pointInWorkspace.yf * view.zoom - point.yf ) / scrollRatio).round
    }

    private fun makeDefaultView(map: CwMap) : ViewSpace {
        val view = ViewSpace()
        view.offsetX = min(map.width/2 - workAreaContainer.width/2, workAreaContainer.width / 2)
        view.offsetY = min(map.height/2 - workAreaContainer.height/2, workAreaContainer.height / 2)
        _viewMap[map] = view
        return view
    }

    // ==== Bindings ====
    private val _currentMapK = _master.mapSpace.mapsBind.currentlySelectedBind.addObserver { new, old ->
        val view = if( new == null) _defaultView else _viewMap[new] ?: makeDefaultView(new)
        //_viewObservable.trigger { it.invoke() }

        SwingUtilities.invokeLater {  // TODO: Bad.  There should be a better solution.
            calibrateScrolls(view.offsetX / scrollRatio, view.offsetY / scrollRatio)
        }
    }
}