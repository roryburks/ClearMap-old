package clearMap.model.map

import rb.owl.bindableMList.BindableMList
import rb.vectrix.intersect.CollisionObject

class CwCollisionSpaceModel
{
    val collisionObjectsBind = BindableMList<CollisionObject>()
    val collisionObjects: List<CollisionObject> get() = collisionObjectsBind.list
}