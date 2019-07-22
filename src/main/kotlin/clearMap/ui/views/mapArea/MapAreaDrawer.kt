package clearMap.ui.views.mapArea

import clearMap.gl.SpecialDrawerFactory
import clearMap.model.IMasterModel
import clearMap.model.etc.draw
import clearMap.model.penner.ViewSpace
import rb.extendo.extensions.step
import rb.glow.GraphicsContext
import rb.glow.IGraphicsContext
import rb.glow.color.Colors
import rb.vectrix.linear.*
import rb.vectrix.mathUtil.MathUtil
import rb.vectrix.mathUtil.f
import rb.vectrix.shapes.RectI
import sguiSwing.skin.Skin

object MapAreaDrawer  {
    fun drawGrid( gc: GraphicsContext, area: RectI, view: ViewSpace, gridW: Int, gridH: Int ) {
        val origin = view.tViewToScreen.apply(Vec2.Zero)
        val gridCoord = view.tViewToScreen.apply(Vec2i(gridW, gridH))
        val gw = gridCoord.x - origin.x
        val gh = gridCoord.y - origin.y
        if( gw < 2 || gh < 2)
            return

        val startX = MathUtil.cycle( area.x1, gw, origin.x)
        val startY = MathUtil.cycle( area.y1, gh, origin.y)

        gc.color = Colors.LIGHT_GRAY
        gc.alpha = 0.5f

        (startX..area.w step gw).forEach {gc.drawLine(it.f, area.y1i.f, it.f, area.y2i.f)}
        (startY..area.h step gh).forEach {gc.drawLine(area.x1i.f, it.f, area.x2i.f, it.f)}

    }

    fun drawMap(
        gc: GraphicsContext,
        context: MapSection,
        master: IMasterModel,
        width: Int,
        height: Int)
    {
        gc.clear( Skin.Global.Bg.scolor)
        gc.color = Colors.RED

        val map = context.currentMap
        val view = context.currentView
        val schema = master.viewSchema

        if( map != null) {
            gc.pushState()
            gc.transform = view.tViewToScreen

            val drawer = SpecialDrawerFactory.makeSpecialDrawer(gc)
            drawer.drawTransparencyBg(0,0,map.width, map.height, 8)

            gc.color = Colors.RED
            map.collisionSpace.collisionObjects.forEach { it.draw(gc) }

            gc.popState()
        }
        if( schema.gridVis) {
            gc.transform = view.tViewToScreen
            drawGrid(gc, RectI(0,0,width, height), view, schema.gridX, schema.gridY)
        }
        if( context.penner.drawsOverlay) {
            gc.transform = view.tViewToScreen
            context.penner.drawOverlay(gc, view)
        }
    }
}