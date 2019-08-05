package clearMap.model.penner.mapBehaviors

import clearMap.model.penner.AbstractPenner
import clearMap.model.penner.ViewSpace

class MovingViewBehavior(
    penner: AbstractPenner,
    val view: ViewSpace): PennerBehavior(penner)
{
    override fun onStart() {}
    override fun onTock() {}

    override fun onMove() {
        view.offsetX -= penner.rawX - penner.oldRawX
        view.offsetY -= penner.rawY - penner.oldRawY
    }
}