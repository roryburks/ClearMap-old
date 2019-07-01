package clearMap.model.map

import rb.glow.IImage
import rb.owl.bindable.Bindable
import rb.owl.bindableMList.BindableMList
import rb.vectrix.shapes.RectI



class CwTileModel(
    img: IImage,
    name : String)
{
    val img: IImage = img

    val tileMapBind = BindableMList<RectI>()
    val tileMap get() = tileMapBind.list

    val nameBind = Bindable(name)
    var name: String by nameBind
}