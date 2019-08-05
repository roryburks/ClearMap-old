package clearMap.model.penner.tileBehaviors

import clearMap.model.map.CwTileModel
import clearMap.model.map.CwTileRectSegment
import clearMap.model.penner.AbstractPenner
import clearMap.model.penner.ViewSpace
import clearMap.model.penner.mapBehaviors.DrawnPennerBehavior
import clearMap.model.penner.mapBehaviors.PennerBehavior
import rb.glow.GraphicsContext
import rb.glow.color.Colors
import rb.vectrix.shapes.RectI
import kotlin.math.abs
import kotlin.math.min

class TileRectBehavior (
    val tile: CwTileModel,
    penner: AbstractPenner) : DrawnPennerBehavior(penner)
{
    var startX : Int = 0
    var startY: Int = 0

    override fun onStart() {
        startX = penner.x
        startY = penner.y
    }

    override fun onTock() {}

    override fun onMove() {}


    override fun paintOverlay(gc: GraphicsContext, view: ViewSpace) {
        gc.color = Colors.GREEN

        gc.drawRect(
            min(startX, penner.x),
            min(startY, penner.y),
            abs(penner.x - startX),
            abs(penner.y - startY))
    }

    override fun onEnd() {
        tile.tileSegments.add(CwTileRectSegment(RectI.FromEndpoints(startX, startY, penner.x, penner.y),tile))
    }
}