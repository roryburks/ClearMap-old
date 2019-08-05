package clearMap.ui

import clearMap.hybrid.Hybrid
import clearMap.model.master.IMasterModel
import clearMap.model.commands.GlobalCommands
import clearMap.ui.systems.Hotkey
import clearMap.ui.systems.MenuItem
import clearMap.ui.systems.SwContextMenus
import clearMap.ui.systems.omniContainer.OmniContainer
import clearMap.ui.systems.omniContainer.OmniSegment
import clearMap.ui.systems.omniContainer.SubContainer
import clearMap.ui.views.mapArea.MapSection
import clearMap.ui.views.ViewSchemaView
import clearMap.ui.views.tile.TileListView
import clearMap.ui.views.tile.TileSegmentListView
import clearMap.ui.views.tile.TileToolPropertyView
import clearMap.ui.views.tile.TileToolView
import clearMap.ui.views.tile.tileImageView.TileImageView
import clearMap.ui.views.tools.ToolPanel
import sgui.components.IComponentProvider
import sgui.systems.KeypressSystem
import sguiSwing.SwKeycodeMapper
import sguiSwing.components.SwMenuBar
import sguiSwing.components.jcomponent
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities

class RootWindow(
    private val _master: IMasterModel,
    private val _ui: IComponentProvider) : JFrame() {

    init {
        val scheme = listOf(
            MenuItem("&File"),
            MenuItem(".&New Map", GlobalCommands.NewMap),
            MenuItem(".&Import Tile", GlobalCommands.ImportTile)

            )

        val bar = SwMenuBar()
        SwContextMenus(
            _master.commandExecutor,
            Hybrid.gle,
            Hybrid.logger).constructMenu(bar, scheme)
        jMenuBar = bar
    }

    // Map View
    private val _mapSection = MapSection(_master, _ui)
    private val _mapOmni = OmniContainer {
        center = SubContainer(200, 400) {
            right += OmniSegment(ViewSchemaView(_master, _ui), 150, 150)
            center = OmniSegment(_mapSection, 200, 200)
        }
        bottom += OmniSegment(ToolPanel(_master, _ui), 100, 100)
    }

    // Tile View
    private val _tileImageSection = TileImageView(_master, _ui)
    private val _tileOmni = OmniContainer {
        right += SubContainer(150, 150) {
            center = OmniSegment(TileListView(_master, _ui), 200, 400)
            bottom += OmniSegment(TileSegmentListView(_master, _ui), 100, 150)
        }
        center = SubContainer(200, 400) {
            center = OmniSegment(_tileImageSection, 200, 400)
            bottom += SubContainer(100, 100) {
                left += OmniSegment(TileToolView(_master, _ui), 100, 150)
                center =  OmniSegment(TileToolPropertyView(_master, _ui), 100, 150)
            }
        }
    }

    private val _tab = _ui.TabbedPane()

    init /* Layout */ {
        _tab.addTab("Map View", _mapOmni)
        _tab.addTab("Tile View", _tileOmni)

        this.layout = GridLayout()

        this.title = "Spirite"

        val multiLevel = _ui.CrossPanel {
            rows.addFlatGroup {
                add(_tab, flex = 1f)
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

                        val command = _master.hotkeyManager.getCommand(Hotkey(key, modifier))
                        command?.apply { _master.commandExecutor.executeCommand(this.commandString, this.objectCreator?.invoke(_master)) }

                        SwKeycodeMapper.map(evt.keyCode)?.run { _mapSection.penner.pressKey(this) }
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