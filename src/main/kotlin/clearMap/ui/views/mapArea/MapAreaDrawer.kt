package clearMap.ui.views.mapArea

import clearMap.gl.SpecialDrawerFactory
import rb.glow.GraphicsContext
import rb.glow.IGraphicsContext
import rb.glow.color.Colors
import sguiSwing.skin.Skin

object MapAreaDrawer  {
    fun drawMap(gc: GraphicsContext, context: MapSection) {
        gc.clear( Skin.Global.Bg.scolor)
        gc.color = Colors.RED

        val map = context.currentMap
        val view = context.currentView

        if( map != null) {
            gc.transform = view.tViewToScreen

            val drawer = SpecialDrawerFactory.makeSpecialDrawer(gc)
            drawer.drawTransparencyBg(0,0,map.width, map.height, 8)
        }
    }
}