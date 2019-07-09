package clearMap.model.map

import rb.owl.bindable.Bindable
import rb.owl.bindableMList.BindableMListSet

class CwMap(w: Int, h: Int)
{
    val collisionSpace = CwCollisionSpaceModel()

    val tilesBind = BindableMListSet<CwTileModel>()
    val tiles get() = tilesBind.list

    val actorsBind = BindableMListSet<CwActor>()
    val actors get() = actorsBind.list

    val widthBind = Bindable(w)
    val heightBind = Bindable(h)
    var width by widthBind
    var height by heightBind
}

class CwActor



