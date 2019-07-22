package clearMap.model.etc

import rb.glow.GraphicsContext
import rb.vectrix.intersect.CollisionObject
import rb.vectrix.intersect.CollisionPolygon
import rb.vectrix.mathUtil.f

fun CollisionObject.draw( gc : GraphicsContext) = when(this) {
    is CollisionPolygon -> {
        val xs = FloatArray(polygon.vertices.size + 1)
        val ys = FloatArray(polygon.vertices.size + 1)

        polygon.vertices.forEachIndexed { index, vec2i ->
            xs[index] = vec2i.x.f
            ys[index] = vec2i.y.f
        }
        xs[polygon.vertices.size] = polygon.vertices.firstOrNull()?.x?.f ?: 0f
        ys[polygon.vertices.size] = polygon.vertices.firstOrNull()?.y?.f ?: 0f

        val alpha = gc.alpha
        gc.alpha = alpha*0.5f
        gc.fillPolygon(xs.asList(), ys.asList(), xs.size)
        gc.alpha = alpha

        gc.drawPolyLine(xs, ys, xs.size)
    }
    else -> {}
}