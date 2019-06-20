package clearMap.ui

import clearMap.model.IMasterModel
import clearMap.model.MasterModel
import clearMap.ui.resources.EngineLaunchpoint
import clearMap.ui.resources.setupSwGuiStuff
import rbJvm.glow.awt.RasterHelper
import rbJvm.vectrix.SetupVectrixForJvm
import java.awt.image.BufferedImage
import javax.swing.SwingUtilities
import javax.swing.UIManager

private lateinit var master: IMasterModel
val uiManager = clearMap.ui.UIManager()

fun main( args: Array<String>) {
    try {
        SetupVectrixForJvm()
        setupSwGuiStuff()
        UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName())

        SwingUtilities.invokeAndWait {
            val bi = BufferedImage(10,10, BufferedImage.TYPE_4BYTE_ABGR)
            RasterHelper.getDataStorageFromBi(bi)

            EngineLaunchpoint.gle
            master = MasterModel()
            uiManager.launchRoot(master)

        }

        SwingUtilities.invokeLater { }
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
}