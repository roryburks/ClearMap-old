package clearMap.model.penner

import clearMap.model.master.IMasterModel
import clearMap.model.penner.mapBehaviors.MovingViewBehavior
import clearMap.model.penner.mapBehaviors.PennerBehavior
import clearMap.model.penner.mapBehaviors.PolygonComposingBehavior
import clearMap.model.penner.tileBehaviors.TileRectBehavior
import clearMap.model.tools.ColPolyTool
import clearMap.model.tools.TileRectTool
import clearMap.ui.views.mapArea.MapSection
import clearMap.ui.views.tile.tileImageView.TileImageView
import sgui.components.events.MouseEvent
import sgui.systems.IKeypressSystem


class TilePenner(
    master: IMasterModel,
    private val _tileContext: TileImageView,
    keypressSystem: IKeypressSystem
) : AbstractPenner(master, _tileContext, keypressSystem)
{
    override fun setBehavior(button: MouseEvent.MouseButton): PennerBehavior? {
        if( holdingSpace) return MovingViewBehavior(this, context.view)

        val map = master.mapSpace.mapsBind.currentlySelected ?: return null
        val tile = map.tilesBind.currentlySelected ?: return null

        val currentTool = master.tools.tile.selectedTool

        return when {
            currentTool is TileRectTool -> TileRectBehavior(tile, this)
            else -> null
        }
    }

}