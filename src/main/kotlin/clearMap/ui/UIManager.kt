package clearMap.ui

import clearMap.model.IMasterModel
import javax.swing.WindowConstants

interface IUIManager {
    fun launchRoot( model: IMasterModel)

}

class UIManager  : IUIManager {
    override fun launchRoot(model: IMasterModel) {
        val root = RootWindow(model)
        root.pack()
        root.isLocationByPlatform = true
        root.isVisible = true
        root.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

}