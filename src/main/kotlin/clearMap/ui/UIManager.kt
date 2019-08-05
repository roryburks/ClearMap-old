package clearMap.ui

import clearMap.hybrid.Hybrid
import clearMap.model.IMasterModel
import javax.swing.WindowConstants

interface IUIManager {
    fun init(model: IMasterModel)

}

class UIManager  : IUIManager {
    override fun init(model: IMasterModel) {
        val root = RootWindow(model, Hybrid.ui)
        root.pack()
        root.isLocationByPlatform = true
        root.isVisible = true
        root.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

}