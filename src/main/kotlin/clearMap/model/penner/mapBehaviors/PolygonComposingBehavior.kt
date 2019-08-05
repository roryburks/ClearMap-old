package clearMap.model.penner.mapBehaviors

import clearMap.model.map.CwMap
import clearMap.model.penner.AbstractPenner
import clearMap.model.penner.ViewSpace
import clearMap.model.tools.collisionObjectBuilding.ColPolygonBuilder
import rb.glow.GraphicsContext
import rb.vectrix.intersect.CollisionPolygon
import rb.vectrix.shapes.PolygonD
import sgui.components.events.MouseEvent
import sgui.systems.KeypressCode

class PolygonComposingBehavior (
    penner: AbstractPenner,
    val map: CwMap)
    :DrawnPennerBehavior(penner)
{
    val builder = ColPolygonBuilder(map)


    override fun onEnd() {
        map.collisionSpace.collisionObjects.add(CollisionPolygon(PolygonD.Make(builder.points)))
    }


    override fun onStart() {
        builder.doStart(penner.x, penner.y)
    }
    override fun onPenUp() {
        builder.handleRelease(penner.x, penner.y)
    }

    override fun onPenDown(button: MouseEvent.MouseButton) {
        builder.handlePress(penner.x, penner.y, button)
    }

    override fun onTock() {
        if(penner.pressingKeys.contains(KeypressCode.KC_ESC))
            end()
    }

    override fun onMove() {
        builder.handleMove(penner.x, penner.y, penner.holdingShift,penner.holdingCtrl)
    }

    override fun paintOverlay(gc: GraphicsContext, view: ViewSpace) {
        builder.draw(gc)
    }

}