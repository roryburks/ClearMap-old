package clearMap.model.tools.collisionObjectBuilding

import clearMap.model.map.CwMap
import rb.extendo.extensions.minByWith
import rb.glow.GraphicsContext
import rb.glow.LineAttributes
import rb.glow.color.Colors
import rb.vectrix.intersect.distanceFromPoint
import rb.vectrix.linear.Vec2i
import rb.vectrix.mathUtil.MathUtil
import rb.vectrix.mathUtil.d
import rb.vectrix.mathUtil.f
import rb.vectrix.mathUtil.round
import rb.vectrix.shapes.LineSegmentI
import sgui.components.events.MouseEvent
import kotlin.math.abs

class ColPolygonBuilder(val map: CwMap) {
    val points = mutableListOf<Vec2i>()
    var thresh = 10.0

    fun draw(gc: GraphicsContext) {
        gc.color = Colors.BLACK
        val xs = FloatArray(points.size + 1)
        val ys = FloatArray(points.size + 1)

        points.forEachIndexed { index, vec2i ->
            xs[index] = vec2i.xi.f
            ys[index] = vec2i.yi.f
        }
        xs[points.size] = points.firstOrNull()?.xi?.f ?: 0f
        ys[points.size] = points.firstOrNull()?.yi?.f ?: 0f

        gc.lineAttributes = LineAttributes(2f)
        gc.drawPolyLine(xs, ys, xs.size)

        state?.draw(gc)
    }

    fun doStart(x: Int, y:Int) {
        points.add(Vec2i(x,y))
        points.add(Vec2i(x,y))
        state = MovingPointState(points.size-1)
    }

    fun handleRelease(x: Int, y: Int) {
        state = null
    }
    fun handlePress(x: Int, y: Int, button: MouseEvent.MouseButton) {
        when(val prop = determineProposition(x,y)) {
            is ProposingMovePointState -> when( button) {
                MouseEvent.MouseButton.RIGHT -> points.removeAt(prop.i)
                else -> state = MovingPointState(prop.i)
            }
            is ProposingMoveLineState -> when (button) {
                MouseEvent.MouseButton.RIGHT -> {
                    points.add(prop.i+1, Vec2i(x,y))
                    state = MovingPointState(prop.i+1)
                }
                else -> state = MovingLineState(prop.i, x, y)
            }
            null -> {
                points.add(Vec2i(x, y))
                state = MovingPointState(points.size-1)
            }
        }
    }
    fun handleMove(x: Int, y: Int, shift: Boolean, ctrl: Boolean) {
        state?.onMove(x, y, shift, ctrl)

        if( state == null) {
            state = determineProposition(x,y)
        }
    }

    private fun getClipPoint(x: Int, y: Int, thresh: Double = this.thresh) : Vec2i? {
        val closest = map.getSnappablePoints()
            .minBy { MathUtil.distance(x.d, y.d, it.x, it.y) }
            ?: return null

        if( MathUtil.distance(x.d, y.d, closest.x, closest.y) < thresh) {
            return Vec2i(closest.x.round, closest.y.round)
        }

        return null
    }

    private fun determineProposition(x: Int, y: Int) : State? {
        val closestPointSet = (0 until points.size)
            .minByWith { MathUtil.distance(x.d, y.d, points[it].x, points[it].y) }
        if( closestPointSet != null && closestPointSet.second <= thresh)
            return ProposingMovePointState(closestPointSet.first)

        val closestLineSet = (0 until points.size-1).asSequence()
            .minByWith { abs(LineSegmentI(points[it].xi, -points[it].yi, points[it+1].xi, -points[it+1].yi ).distanceFromPoint(x.d, -y.d)) }
        if(closestLineSet != null && closestLineSet.second <= thresh)
            return ProposingMoveLineState(closestLineSet.first)

        return null
    }

    private var state: State? = null

    private interface State {
        abstract fun draw(gc: GraphicsContext)
        fun onMove(x: Int, y:Int, shift: Boolean, ctrl: Boolean)
    }

    inner class MovingPointState(val i: Int) : State {
        val startX = points[i].xi
        val startY = points[i].yi

        val pointLeft get() = points.getOrNull(i-1)
        val pointRight get() = points.getOrNull((i+1) % points.size)

        override fun draw(gc: GraphicsContext) {
            gc.fillOval(points[i].xi - 2, points[i].yi - 2, 4, 4)
        }
        override fun onMove(x: Int, y: Int, shift: Boolean, ctrl: Boolean) {
            val sx: Int
            val sy: Int
            when {
                ctrl -> {
                    val snap = getClipPoint(x,y)
                    sx = snap?.xi ?: x
                    sy = snap?.yi ?: y
                }
                shift -> {
                    sy = when {
                        abs(y - (pointLeft?.yi ?: 0)) < thresh -> pointLeft?.yi ?: y
                        abs(y - (pointRight?.yi ?: 0)) < thresh -> pointRight?.yi ?: y
                        else -> y
                    }
                    sx = when {
                        abs(x - (pointLeft?.xi ?: 0)) < thresh -> pointLeft?.xi ?: x
                        abs(x - (pointRight?.xi ?: 0)) < thresh -> pointRight?.xi ?: x
                        else -> x
                    }
                }
                else -> {sx = x; sy = y}
            }

            points[i] = Vec2i(sx,sy)
        }
    }

    inner class MovingLineState(val iLeft: Int, val startMouseX: Int, val startMouseY: Int) : State {
        val startLeftX: Int = points[iLeft].xi
        val startLeftY: Int = points[iLeft].yi
        val deltaX = points[iLeft+1].xi - startLeftX
        val deltaY = points[iLeft+1].yi - startLeftY

        override fun draw(gc: GraphicsContext) {
            gc.color = Colors.YELLOW
            gc.drawLine(points[iLeft].xi,points[iLeft].yi, points[iLeft+1].xi, points[iLeft+1].yi)
        }

        override fun onMove(x: Int, y: Int, shift: Boolean, ctrl: Boolean) {
            val sx: Int
            val sy: Int
            when {
                ctrl -> {
                    val leftX = startLeftX + (x - startMouseX)
                    val leftY = startLeftY + (y - startMouseY)
                    val rightX = startLeftX + deltaX + (x - startMouseX)
                    val rightY = startLeftY + deltaY + (y - startMouseY)

                    val snapLeft = getClipPoint(leftX, leftY)
                    val snapRight = getClipPoint(rightX, rightY)
                    if( snapLeft != null) {
                        sx = snapLeft.xi
                        sy = snapLeft.yi
                    }
                    else if( snapRight != null ) {
                        sx = snapRight.xi
                        sy = snapRight.yi
                    }
                    else {
                        sx = x - startMouseX + startLeftX
                        sy = y - startMouseY + startLeftY
                    }
                }
                shift -> when {
                    Math.abs(x - startMouseX) > Math.abs(y - startMouseY) -> {
                        sx = startLeftX
                        sy = y - startMouseY + startLeftY
                    }
                    else -> {
                        sx = x - startMouseX + startLeftX
                        sy = startLeftY
                    }
                }
                else -> {
                    sx = x - startMouseX + startLeftX
                    sy = y - startMouseY + startLeftY
                }
            }

            points[iLeft] = Vec2i(sx, sy)
            points[iLeft+1] = Vec2i(sx+deltaX, sy+deltaY)
        }

    }

    inner class ProposingMovePointState(val i: Int) : State {
        override fun draw(gc: GraphicsContext) {
            gc.color = Colors.YELLOW
            gc.drawOval(points[i].xi - 3, points[i].yi - 3, 6, 6)
        }
        override fun onMove(x: Int, y: Int, shift: Boolean, ctrl: Boolean) {
            state = null
        }
    }

    inner class ProposingMoveLineState(val i: Int) : State {
        override fun draw(gc: GraphicsContext) {
            gc.color = Colors.YELLOW
            gc.drawLine(points[i].xi, points[i].yi, points[i+1].xi, points[i+1].yi)
        }
        override fun onMove(x: Int, y: Int, shift: Boolean, ctrl: Boolean) {
            state = null
        }
    }
}