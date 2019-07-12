package clearMap.model.view

import rb.owl.bindable.Bindable


class ViewSchemaModel
{
    val collisionVisBind = Bindable(true)
    val tilesVisBind = Bindable(true)
    val zonesVisBind = Bindable(true)
    val actorsVisBind = Bindable(true)
    val gridVisBind = Bindable(true)
    val gridXBind = Bindable(10)
    val gridYBind = Bindable(10)

    var collisionVis by collisionVisBind
    var tilesVis by tilesVisBind
    var zonesVis by zonesVisBind
    var actorsVis by actorsVisBind
    var gridVis by gridVisBind
    var gridX by gridXBind
    var gridY by gridYBind
}