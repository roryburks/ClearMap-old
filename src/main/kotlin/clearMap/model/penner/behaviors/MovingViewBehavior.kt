package clearMap.model.penner.behaviors

import clearMap.model.penner.Penner
import clearMap.model.penner.ViewSpace

class MovingViewBehavior(
    penner: Penner,
    val view: ViewSpace): PennerBehavior(penner)
{
    override fun onStart() {}
    override fun onTock() {}

    override fun onMove() {
        view.offsetX -= penner.rawX - penner.oldRawX
        view.offsetY -= penner.rawY - penner.oldRawY
    }
}