package clearMap.ui.systems

import clearMap.model.commands.ICentralCommandExecutor
import clearMap.ui.resources.ILogger
import rb.glow.gle.IGLEngine
import sgui.UIPoint
import sguiSwing.SwIcon
import javax.swing.*


/***
 * Sample scheme:
 * MenuItem("&Root")
 * MenuItem(".Sub1")
 * MenuItem("-")
 * MenuItem(".Sub2")
 * MenuItem("..SubSub1")
 * MenuItem("Root2")
 *
 * Each dot before the name indicates the level it should be in.  For example one dot
 *   means it goes inside the last zero-dot item, two dots means it should go in the last
 *   one-dot item, etc.  Note: if you skip a certain level of dot's (eg: going from
 *   two dots to four dots), then the extra dots will be ignored, possibly resulting
 *   in unexpected menu form.
 * The & character before a letter represents the Mnemonic key that should be associated
 *   with it.
 * If the title is simply - (perhaps after some .'s representing its depth), then it is
 *   will simply construct a separator and will ignore the last two elements in the
 *   array (in fact they don't need to exist).
 */
data class MenuItem(
    val lexicon : String,
    val command: ICommand? = null,
    val icon: SwIcon? = null,
    val customAction :(()->Unit)? = null,
    val enabled: Boolean = true)

interface IContextMenus {
    fun LaunchContextMenu(point: UIPoint, scheme: List<MenuItem>, obj: Any? = null)
}

class SwContextMenus(
    private val _commandExecuter: ICentralCommandExecutor,
    private val _gle: IGLEngine,
    private val _logger: ILogger) : IContextMenus {
    val cmenu = JPopupMenu()

    override fun LaunchContextMenu(point: UIPoint, scheme: List<MenuItem>, extra: Any?) {
        cmenu.removeAll()

        constructMenu(cmenu, scheme.toList(), extra)
        cmenu.show( (point as? sguiSwing.SUIPoint)?.component, point.x, point.y)

        SwingUtilities.invokeLater { cmenu.requestFocus() } // Meh
    }

    fun constructMenu(root: JComponent, menuScheme: List<MenuItem>, extra: Any? = null) {
        val isMenuBar = root is JMenuBar
        val isPopup = root is JPopupMenu
        val activeRootTree = MutableList<JMenuItem?>(10, {null})

        // Atempt to construct menu from parsed data in menu_scheme
        var activeDepth = 0
        menuScheme.forEachIndexed { index, item ->
            var mnemonic = 0.toChar()

            // Determine the depth of the node and crop off the extra .'s
            var depth = _imCountLevel(item.lexicon)
            var lexiocon = item.lexicon.substring(depth)

            if( depth > activeDepth) {
                _logger.logWarning("Bad Menu Scheme.")
                depth = activeDepth
            }
            activeDepth = depth+1

            if( lexiocon == "-") {
                if( depth == 0) {
                    if( isPopup) (root as JPopupMenu).addSeparator()
                }
                else
                    (activeRootTree[depth-1] as JMenu).addSeparator()

                activeDepth--
            }
            else {
                // Detect the Mnemonic
                val mnemIndex = lexiocon.indexOf('&')
                if( mnemIndex != -1 && mnemIndex != lexiocon.length-1) {
                    mnemonic = lexiocon[mnemIndex+1]
                    lexiocon = lexiocon.substring(0, mnemIndex) + lexiocon.substring(mnemIndex+1)
                }

                // Determine if it needs to be a Menu (which contains other options nested in it)
                //	or a plain MenuItem (which doesn't)
                val node = when {
                    (depth != 0 || !isMenuBar) && (index+1 == menuScheme.size || _imCountLevel(menuScheme[index+1].lexicon) <= depth)
                    -> JMenuItem(lexiocon).also { it.isEnabled = item.enabled }
                    else -> JMenu(lexiocon).also { it.isEnabled = item.enabled }
                }
                if( mnemonic != 0.toChar())
                    node.setMnemonic(mnemonic)

                if( item.command != null)
                    node.addActionListener { _gle.runInGLContext { _commandExecuter.executeCommand(item.command.commandString, extra)} }
                if( item.customAction != null)
                    node.addActionListener { _gle.runInGLContext { item.customAction.invoke()} }

                if( item.icon != null)
                    TODO()

                // Add the MenuItem into the appropriate workspace
                when {
                    depth == 0 -> root.add( node)
                    else -> activeRootTree[depth-1]!!.add(node)
                }

                activeRootTree[depth] = node
            }
        }
    }


    private val MAX_LEVEL = 10
    fun _imCountLevel(s: String): Int {
        var r = 0
        while (r < s.length && s[r] == '.')
            r++
        return Math.min(r, MAX_LEVEL)
    }
}
