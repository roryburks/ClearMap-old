package clearMap.model.penner.behaviors

import clearMap.model.penner.Penner
import clearMap.model.penner.ViewSpace
import rb.glow.GraphicsContext
import sgui.components.events.MouseEvent

abstract class PennerBehavior(
    val penner: Penner
)
{
    abstract fun onStart()
    abstract fun onTock()
    abstract fun onMove()

    // For most StateBehavior, onPenDown will be irrelevant/not make sense
    //	because their penUp action is to cancel the state.
    open fun onPenDown(button: MouseEvent.MouseButton) {}
    open fun onPenUp() {end()}
    open fun onEnd() {}

    fun end() {
        // This effectively ends the state behavior
        penner.behavior = null
    }
}

abstract class DrawnPennerBehavior(penner: Penner) : PennerBehavior(penner) {

    override fun onEnd() {
        this.penner.context.panel.redraw()
    }

    abstract fun paintOverlay(gc: GraphicsContext, view: ViewSpace)
}