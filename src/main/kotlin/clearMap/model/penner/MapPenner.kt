package clearMap.model.penner

import clearMap.model.master.IMasterModel
import clearMap.model.penner.mapBehaviors.MovingViewBehavior
import clearMap.model.penner.mapBehaviors.PennerBehavior
import clearMap.model.penner.mapBehaviors.PolygonComposingBehavior
import clearMap.model.tools.ColPolyTool
import clearMap.ui.views.mapArea.MapSection
import sgui.components.events.MouseEvent
import sgui.systems.IKeypressSystem

class MapPenner(
    master: IMasterModel,
    private val _mapContext: MapSection,
    keypressSystem: IKeypressSystem) : AbstractPenner(master, _mapContext, keypressSystem)
{
    override fun setBehavior(button: MouseEvent.MouseButton): PennerBehavior? {
        val map = _mapContext.currentMap ?: return null

        if( holdingSpace) return MovingViewBehavior(this, context.view)

        val currentTool = master.tools.collision.selectedTool

        return when {
            currentTool is ColPolyTool -> PolygonComposingBehavior(this, map)
            else -> null
        }
    }

}

//class MapPenner(
//    val master: IMasterModel,
//    val context: MapSection,
//    private val _keypressSystem: IKeypressSystem) : IPenner
//{
//    val holdingSpace get() = _keypressSystem.holdingSpace
//    var holdingShift = false
//    var holdingAlt = false
//    var holdingCtrl = false
//
//    var rawX = 0 ; private set
//    var rawY = 0 ; private set
//    var oldRawX = 0 ; private set
//    var oldRawY = 0 ; private set
//
//    var x = 0; private set
//    var y = 0; private set
//    var xf = 0f; private set
//    var yf = 0f; private set
//
//    var oldX = 0 ; private set
//    var oldY = 0 ; private set
//
//    var pressure = 1.0f ; private set
//
//    var behavior: PennerBehavior? = null
//    val pressingKeys = hashSetOf<KeypressCode>()
//
//    override fun step() {
//        if( oldX != x || oldY != y) {
//            behavior?.onMove()
//            if( behavior is DrawnPennerBehavior)
//                context.panel.redraw()
//            context.refreshCoordinates(x, y)
//        }
//
//        behavior?.onTock()
//
//        oldX = x
//        oldY = y
//        oldRawX = rawX
//        oldRawY = rawY
//
//        pressingKeys.clear()
//    }
//
//    override fun pressKey(code: KeypressCode) {
//        pressingKeys.add(code)
//    }
//
//    override fun penDown(button: MouseEvent.MouseButton) {
//        if( button == MouseEvent.MouseButton.UNKNOWN) return
//
//        val map = context.currentMap ?: return
//
//        if( behavior != null) behavior?.onPenDown(button)
//        else {
//            behavior = setBehavior(button, map)
//            behavior?.onStart()
//        }
//    }
//
//    override fun penUp(button: MouseEvent.MouseButton) {
//        behavior?.onPenUp()
//    }
//
//    override fun reset() {
//        behavior = null
//        context.panel.redraw()
//    }
//
//    override fun rawUpdateX(rawX: Int) {
//        if( this.rawX != rawX) {
//            this.rawX = rawX
//            val p = context.currentView.tScreenToView.apply(Vec2f(rawX.f, rawY.f))
//            xf = p.xf
//            yf = p.yf
//            x = p.xf.floor
//            y = p.yf.floor
//        }
//    }
//
//    override fun rawUpdateY(rawY: Int) {
//        if( this.rawY != rawY) {
//            this.rawY = rawY
//            val p = context.currentView.tScreenToView.apply(Vec2f(rawX.f, rawY.f))
//            xf = p.xf
//            yf = p.yf
//            x = p.xf.floor
//            y = p.yf.floor
//        }
//    }
//
//    override fun rawUpdatePressure(rawPressure: Float) {pressure = rawPressure }
//
//    override val drawsOverlay: Boolean get() = behavior is DrawnPennerBehavior
//    override fun drawOverlay(gc: GraphicsContext, view: ViewSpace) {
//        (behavior as? DrawnPennerBehavior)?.paintOverlay(gc, view)
//    }
//
//    private fun setBehavior(button: MouseEvent.MouseButton, map: CwMap) : PennerBehavior? {
//        if( holdingSpace) return MovingViewBehavior(this, context.currentView)
//
//        val currentTool = master.tools.collision.selectedTool
//
//        return when {
//            currentTool is ColPolyTool -> PolygonComposingBehavior(this, map)
//            else -> null
//        }
//    }
//
//    private val onToolChangeK = master.tools.collision.selectedToolBinding.addObserver { _, _ ->
//        behavior?.end()
//    }
//}