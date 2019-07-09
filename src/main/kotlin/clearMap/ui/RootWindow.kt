package clearMap.ui

import clearMap.hybrid.Hybrid
import clearMap.model.IMasterModel
import clearMap.ui.systems.MenuItem
import clearMap.ui.systems.SwContextMenus
import clearMap.ui.systems.omniContainer.OmniContainer
import clearMap.ui.systems.omniContainer.OmniSegment
import clearMap.ui.views.mapArea.MapSection
import sgui.Orientation
import sguiSwing.components.SwMenuBar
import sguiSwing.components.jcomponent
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.SwingUtilities

class RootWindow(val master: IMasterModel) : JFrame() {

    init {
        val scheme = listOf(
            MenuItem("&File"),
            MenuItem("&New Map")
        )

        val bar = SwMenuBar()
        SwContextMenus(master.commandExecutor, Hybrid.gle, Hybrid.logger).constructMenu(bar, scheme)
        jMenuBar = bar
    }

    private val _omni = OmniContainer {
        left
        center = OmniSegment(MapSection(master), 200, 200)
    }


    init /* Layout */ {
        this.layout = GridLayout()

        this.title = "Spirite"

        val multiLevel = Hybrid.ui.CrossPanel {
            // TODO: Fix with mouse input
//            rows.addFlatGroup {
//                add(topLevelView, flex = 1f)
//                flex = 1f
//            }
            rows.addFlatGroup {
                add(_omni, flex = 1f)
                flex = 1f
            }
        }
        this.add( multiLevel.jcomponent)

        SwingUtilities.invokeLater {this.size = Dimension(800,600) }
        //SwingUtilities.invokeLater {groupView.component.jcomponent.requestFocus() }
    }
}