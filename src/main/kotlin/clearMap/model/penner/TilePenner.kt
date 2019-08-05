package clearMap.model.penner

import clearMap.model.master.IMasterModel
import clearMap.model.penner.mapBehaviors.MovingViewBehavior
import clearMap.model.penner.mapBehaviors.PennerBehavior
import clearMap.model.penner.mapBehaviors.PolygonComposingBehavior
import clearMap.model.tools.ColPolyTool
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
//        val map = _mapContext.currentMap ?: return null
//
//        if( holdingSpace) return MovingViewBehavior(this, context.view)
//
//        val currentTool = master.tools.collision.selectedTool
//
//        return when {
//            currentTool is ColPolyTool -> PolygonComposingBehavior(this, map)
//            else -> null
//        }
        return null
    }

}