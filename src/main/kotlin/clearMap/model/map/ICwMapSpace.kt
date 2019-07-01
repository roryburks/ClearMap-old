package clearMap.model.map

import rb.owl.bindableMList.BindableMListSet

interface ICwMapSpace
{
    val mapsBind : BindableMListSet<CwMap>
    val maps : List<CwMap>
}

class CwMapSpace : ICwMapSpace
{
    override val mapsBind = BindableMListSet<CwMap>()
    override val maps: List<CwMap> get() = mapsBind.list
}