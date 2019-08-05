package clearMap.ui

import clearMap.model.master.IMasterModel
import clearMap.model.master.MasterModel
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

        master = MasterModel()
        SwingUtilities.invokeAndWait {
            val bi = BufferedImage(10,10, BufferedImage.TYPE_4BYTE_ABGR)
            RasterHelper.getDataStorageFromBi(bi)

            EngineLaunchpoint.gle
            uiManager.init(master)

        }

        SwingUtilities.invokeLater { }
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
}