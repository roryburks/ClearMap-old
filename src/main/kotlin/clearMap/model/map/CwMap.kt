package clearMap.model.map

import rb.owl.bindableMList.BindableMListSet

class CwMap
{
    val collisionSpace = CwCollisionSpaceModel()

    val tilesBind = BindableMListSet<CwTileModel>()
    val tiles get() = tilesBind.list

    val actorsBind = BindableMListSet<CwActor>()
    val actors get() = actorsBind.list

}

class CwActor



