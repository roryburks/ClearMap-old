package clearMap.model.tools.collisionObjectBuilding

import clearMap.model.map.CwMap
import rb.vectrix.intersect.CollisionObject
import rb.vectrix.intersect.CollisionPolygon
import rb.vectrix.intersect.CollisionRayRect
import rb.vectrix.intersect.CollisionRigidRect
import rb.vectrix.linear.Vec2


fun CwMap.getSnappablePoints() : Sequence<Vec2> {
    return this.collisionSpace.collisionObjects.asSequence()
        .flatMap { it.getSnappablePoints().asSequence() }
}

fun CollisionObject.getSnappablePoints() : Iterable<Vec2>
{
    return when( this) {
        is CollisionPolygon -> polygon.vertices
        is CollisionRigidRect -> rect.points
        is CollisionRayRect -> rayRect.points
        else -> emptyList()
    }
}