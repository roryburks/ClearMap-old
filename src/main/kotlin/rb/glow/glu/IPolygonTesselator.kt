package rb.glow.glu

import rb.glow.gle.GLPrimitive
import rb.glow.gle.Vec2uv

interface IPolygonTesselator {
    fun tesselatePolygon(x: Sequence<Double>, y: Sequence<Double>, count: Int) : GLPrimitive
    fun tesselatePolygon(points: Sequence<Vec2uv>, count: Int) : GLPrimitive
}