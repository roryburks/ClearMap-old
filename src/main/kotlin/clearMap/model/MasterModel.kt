package clearMap.model

import clearMap.model.commands.CentralCommandExecutor
import clearMap.model.commands.ICentralCommandExecutor
import clearMap.model.map.CwMapSpace
import clearMap.model.map.ICwMapSpace

interface  IMasterModel {
    val mapSpace : ICwMapSpace
    val commandExecutor: ICentralCommandExecutor
}

class MasterModel : IMasterModel{
    override val mapSpace: ICwMapSpace = CwMapSpace()
    override val commandExecutor: ICentralCommandExecutor = CentralCommandExecutor(this)
}