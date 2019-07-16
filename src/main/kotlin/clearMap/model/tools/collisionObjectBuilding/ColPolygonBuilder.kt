package clearMap.model.tools.collisionObjectBuilding

import rb.glow.GraphicsContext
import rb.glow.color.Colors
import rb.vectrix.linear.Vec2i
import rb.vectrix.mathUtil.f

class ColPolygonBuilder {
    val points = mutableListOf<Vec2i>()
    var thresh = 5.0

    fun draw(gc: GraphicsContext) {
        gc.color = Colors.BLACK
        gc.drawPolyLine(
            points.map { it.x.f }.toFloatArray(),
            points.map { it.y.f }.toFloatArray(),
            points.size)
    }


    fun getClipPoint(x: Int, y: Int, thresh: Double = this.thresh) : Vec2i? {
        // TODO
        return null
    }

    private val state: State? = null

    private interface State {
        abstract fun draw(gc: GraphicsContext)
        fun onMove(x: Int, y:Int, shift: Boolean, ctrl: Boolean)
    }

    inner class MovingPointState(val i: Int) : State {
        val startX = points[i].xi
        val startY = points[i].yi
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
                shift -> when {
                    Math.abs(x - startX) > Math.abs(y - startY) -> {sx = x; sy = startY}
                    else -> {sx = startX; sy = y}
                }
                else -> {sx = x; sy = y}
            }

            points[i] = Vec2i(sx,sy)
        }
    }

    inner class MovingLine(val iLeft: Int, val startMouseX: Int, val startMouseY: Int) : State {
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
                        sx = startMouseX
                        sy = y - startMouseY + startLeftY
                    }
                    else -> {
                        sx = x - startMouseX + startLeftX
                        sy = startMouseY
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
}