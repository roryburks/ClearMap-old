package clearMap.model.map

import rb.extendo.delegates.OnChangeDelegate
import rb.glow.IImage
import rb.owl.IObservable
import rb.owl.Observable
import rb.owl.bindable.Bindable
import rb.owl.bindable.onChangeObserver
import rb.owl.bindableMList.BindableMListSet
import rb.vectrix.shapes.IPolygon
import rb.vectrix.shapes.RectI

interface ICwTileModel
{
    val img: IImage

    val tileSegmentsBind : BindableMListSet<CwTileSegment>
    val tileSegments : MutableList<CwTileSegment>

    val nameBind : Bindable<String>
    var name: String

    val tileSegmentChangeObs :IObservable<()->Unit>
}

class CwTileModel(
    img: IImage,
    name : String) : ICwTileModel
{
    override val img: IImage = img

    override val tileSegmentsBind = BindableMListSet<CwTileSegment>()
    override val tileSegments get() = tileSegmentsBind.list

    override val nameBind = Bindable(name)
    override var name: String by nameBind

    override val tileSegmentChangeObs = Observable<()->Unit>()

    fun triggerSegmentChanged(tile: CwTileSegment) = tileSegmentChangeObs.trigger { it() }
}

sealed class CwTileSegment

class CwTileRectSegment(
    bounds: RectI,
    private val _context : CwTileModel) : CwTileSegment()
{
    var bounds by OnChangeDelegate(bounds) {_context.triggerSegmentChanged(this)}
}

class CwTilePolySegment(
    poly: IPolygon,
    private val _context : CwTileModel) : CwTileSegment()
{
    var poly by OnChangeDelegate(poly) {_context.triggerSegmentChanged(this)}
}