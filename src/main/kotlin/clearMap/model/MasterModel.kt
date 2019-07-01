package clearMap.model

import clearMap.model.map.CwMapSpace
import clearMap.model.map.ICwMapSpace

interface  IMasterModel {
    val mapSpace : ICwMapSpace
}

class MasterModel : IMasterModel{
    override val mapSpace: ICwMapSpace = CwMapSpace()
}