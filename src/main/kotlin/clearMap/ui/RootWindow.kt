package clearMap.ui

import clearMap.hybrid.Hybrid
import clearMap.model.IMasterModel
import clearMap.model.commands.GlobalCommands
import clearMap.ui.systems.Hotkey
import clearMap.ui.systems.MenuItem
import clearMap.ui.systems.SwContextMenus
import clearMap.ui.systems.omniContainer.OmniContainer
import clearMap.ui.systems.omniContainer.OmniSegment
import clearMap.ui.views.mapArea.MapSection
import clearMap.ui.views.ViewSchemaView
import sgui.systems.KeypressSystem
import sguiSwing.components.SwMenuBar
import sguiSwing.components.jcomponent
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities

class RootWindow(val master: IMasterModel) : JFrame() {

    init {
        val scheme = listOf(
            MenuItem("&File"),
            MenuItem(".New Map", GlobalCommands.NewMap)
        )

        val bar = SwMenuBar()
        SwContextMenus(
            master.commandExecutor,
            Hybrid.gle,
            Hybrid.logger).constructMenu(bar, scheme)
        jMenuBar = bar
    }

    private val _omni = OmniContainer {
        right += OmniSegment(ViewSchemaView(master, Hybrid.ui), 150, 150)
        center = OmniSegment(MapSection(master, Hybrid.ui), 200, 200)
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

    init /* Bindings */ {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher { evt ->
            if(focusOwner == null || !Hybrid.keypressSystem.hotkeysEnabled)
                return@addKeyEventDispatcher false

            Hybrid.gle.runInGLContext {
                when (evt.id) {
                    KeyEvent.KEY_PRESSED -> {
                        if (evt.keyCode == KeyEvent.VK_SPACE)
                            KeypressSystem.holdingSpace = true
                        val key = evt.keyCode
                        val modifier = evt.modifiersEx

                        val command = master.hotkeyManager.getCommand(Hotkey(key, modifier))
                        command?.apply { master.commandExecutor.executeCommand(this.commandString, this.objectCreator?.invoke(master)) }
                    }
                    KeyEvent.KEY_RELEASED -> {
                        if (evt.keyCode == KeyEvent.VK_SPACE)
                            KeypressSystem.holdingSpace = false
                    }
                }
            }

            false
        }
    }
}