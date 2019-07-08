package clearMap.ui.views.mapArea

import clearMap.hybrid.Hybrid
import clearMap.model.penner.IPenner
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLJPanel
import jpen.*
import jpen.event.PenListener
import jpen.owner.multiAwt.AwtPenToolkit
import rb.vectrix.mathUtil.round
import rbJvm.glow.jogl.JOGLProvider
import sgui.components.events.MouseEvent
import sguiSwing.components.ISwComponent
import sguiSwing.components.SwComponent
import java.awt.event.MouseAdapter
import javax.swing.SwingUtilities

class JoglMapAreaPanel
private constructor(
    private val _penner: IPenner,
    private val _canvas: GLJPanel)
    :ISwComponent by SwComponent(_canvas)
{
    constructor(penner: IPenner) : this(
        penner,
        GLJPanel(GLCapabilities(GLProfile.getDefault())))

    init {
        _canvas.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: java.awt.event.MouseEvent?) {
                requestFocus()
                super.mousePressed(e)
            }
        })

        _canvas.skipGLOrientationVerticalFlip = true
        //Hybrid.timing.createTimer(50, true){redraw()}

        fun MButtonFromPButton( pbutton: PButton) = when(val x = pbutton.type) {
            PButton.Type.LEFT -> MouseEvent.MouseButton.LEFT
            PButton.Type.RIGHT -> MouseEvent.MouseButton.RIGHT
            PButton.Type.CENTER -> MouseEvent.MouseButton.CENTER
            else -> null
        }

        AwtPenToolkit.addPenListener(_canvas, object: PenListener {
            override fun penKindEvent(evt: PKindEvent) {
                SwingUtilities.invokeLater {
                    // TODO: Switch between active sets
                    when (evt.kind.type) {
                        PKind.Type.CURSOR -> {
                        }
                        PKind.Type.STYLUS -> {
                        }
                        PKind.Type.ERASER -> {
                        }
                        else -> {
                        }
                    }
                }
            }

            override fun penButtonEvent(evt: PButtonEvent) {
                val button = MButtonFromPButton(evt.button) ?: return

                SwingUtilities.invokeLater {
                    JOGLProvider.context.makeCurrent()
                    when (evt.button.value) {
                        true -> _penner.penDown(button)
                        false -> _penner.penUp(button)
                    }
                    JOGLProvider.context.release()
                }
            }

            override fun penTock(p0: Long) {}

            override fun penLevelEvent(evt: PLevelEvent) {
                evt.levels.forEach { level ->
                    when( level.type ) {
                        PLevel.Type.X -> _penner.rawUpdateX(level.value.round)
                        PLevel.Type.Y -> _penner.rawUpdateY(level.value.round)
                        PLevel.Type.PRESSURE -> _penner.rawUpdatePressure(level.value)
                        else -> {}
                    }
                }
            }

            override fun penScrollEvent(p0: PScrollEvent) {}
        })
    }
}