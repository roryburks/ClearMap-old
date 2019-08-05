package clearMap.model.penner

import clearMap.model.master.IMasterModel
import clearMap.model.penner.behaviors.DrawnPennerBehavior
import clearMap.model.penner.behaviors.PennerBehavior
import rb.glow.GraphicsContext
import rb.vectrix.linear.Vec2f
import rb.vectrix.mathUtil.f
import rb.vectrix.mathUtil.floor
import sgui.components.events.MouseEvent
import sgui.systems.IKeypressSystem
import sgui.systems.KeypressCode


interface IPenner{
    fun step()
    fun penDown(button: MouseEvent.MouseButton)
    fun penUp(button: MouseEvent.MouseButton)
    fun reset()

    fun rawUpdateX(rawX: Int)
    fun rawUpdateY(rawY: Int)
    fun rawUpdatePressure(rawPressure: Float)


    val drawsOverlay : Boolean
    fun drawOverlay(gc: GraphicsContext, view: ViewSpace)

    fun pressKey(code: KeypressCode)
}

interface IPennerContext {
    val view: ViewSpace
    fun redraw()
    fun refreshCoordinates(x: Int, y: Int)
}

abstract class AbstractPenner(
    val master: IMasterModel,
    val context : IPennerContext,
    private val _keypressSystem: IKeypressSystem)
    : IPenner
{
    val holdingSpace get() = _keypressSystem.holdingSpace
    var holdingShift = false
    var holdingAlt = false
    var holdingCtrl = false

    var rawX = 0 ; private set
    var rawY = 0 ; private set
    var oldRawX = 0 ; private set
    var oldRawY = 0 ; private set

    var x = 0; private set
    var y = 0; private set
    var xf = 0f; private set
    var yf = 0f; private set

    var oldX = 0 ; private set
    var oldY = 0 ; private set

    var pressure = 1.0f ; private set

    var behavior: PennerBehavior? = null
    val pressingKeys = hashSetOf<KeypressCode>()

    override fun step() {
        if( oldX != x || oldY != y) {
            behavior?.onMove()
            if( behavior is DrawnPennerBehavior)
                context.redraw()
            context.refreshCoordinates(x,y )
        }

        behavior?.onTock()

        oldX = x
        oldY = y
        oldRawX = rawX
        oldRawY = rawY

        pressingKeys.clear()
    }

    override fun pressKey(code: KeypressCode) {
        pressingKeys.add(code)
    }

    override fun penDown(button: MouseEvent.MouseButton) {
        if( button == MouseEvent.MouseButton.UNKNOWN) return

        if( behavior != null) behavior?.onPenDown(button)
        else {
            behavior = setBehavior(button)
            behavior?.onStart()
        }
    }

    override fun penUp(button: MouseEvent.MouseButton) {
        behavior?.onPenUp()
    }

    override fun reset() {
        behavior = null
        context.redraw()
    }

    override fun rawUpdateX(rawX: Int) {
        if( this.rawX != rawX) {
            this.rawX = rawX
            val p = context.view.tScreenToView.apply(Vec2f(rawX.f, rawY.f))
            xf = p.xf
            yf = p.yf
            x = p.xf.floor
            y = p.yf.floor
        }
    }

    override fun rawUpdateY(rawY: Int) {
        if( this.rawY != rawY) {
            this.rawY = rawY
            val p = context.view.tScreenToView.apply(Vec2f(rawX.f, rawY.f))
            xf = p.xf
            yf = p.yf
            x = p.xf.floor
            y = p.yf.floor
        }
    }

    override fun rawUpdatePressure(rawPressure: Float) {pressure = rawPressure }

    override val drawsOverlay: Boolean get() = behavior is DrawnPennerBehavior
    override fun drawOverlay(gc: GraphicsContext, view: ViewSpace) {
        (behavior as? DrawnPennerBehavior)?.paintOverlay(gc, view)
    }

    abstract fun setBehavior(button: MouseEvent.MouseButton) : PennerBehavior?
}