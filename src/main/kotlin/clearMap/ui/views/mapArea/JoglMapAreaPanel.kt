package clearMap.ui.views.mapArea

import clearMap.hybrid.Hybrid
import clearMap.model.master.IMasterModel
import clearMap.model.penner.MapPenner
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLJPanel
import jpen.*
import jpen.event.PenListener
import jpen.owner.multiAwt.AwtPenToolkit
import rb.glow.gle.GLGraphicsContext
import rb.vectrix.mathUtil.round
import rbJvm.glow.jogl.JOGLProvider
import sgui.components.events.MouseEvent
import sguiSwing.components.ISwComponent
import sguiSwing.components.SwComponent
import java.awt.event.MouseAdapter
import javax.swing.SwingUtilities

class JoglMapAreaPanel
private constructor(
    private val _penner: MapPenner,
    private val _context: MapSection,
    private val _master: IMasterModel,
    private val _canvas: GLJPanel)
    :ISwComponent by SwComponent(_canvas)
{
    constructor(penner: MapPenner, context : MapSection, master: IMasterModel) : this(
        penner,
        context,
        master,
        GLJPanel(GLCapabilities(GLProfile.getDefault())))

    init {
        _canvas.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: java.awt.event.MouseEvent?) {
                requestFocus()
                super.mousePressed(e)
            }
        })

        _canvas.skipGLOrientationVerticalFlip = true
        Hybrid.timing.createTimer(50, true){redraw()}

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


        val mouseAdapter = object : MouseAdapter() {
            fun update(e: java.awt.event.MouseEvent) {
                _penner.holdingAlt = e.isAltDown
                _penner.holdingCtrl = e.isControlDown
                _penner.holdingShift = e.isShiftDown

            }

            override fun mouseMoved(e: java.awt.event.MouseEvent) = update(e)
            override fun mouseDragged(e: java.awt.event.MouseEvent)  = update(e)
            override fun mousePressed(e: java.awt.event.MouseEvent) {
                update(e)
                _canvas.requestFocus()
            }
            override fun mouseReleased(e: java.awt.event.MouseEvent) = update(e)
        }

        _canvas.addMouseMotionListener(mouseAdapter)
        _canvas.addMouseListener(mouseAdapter)

        _canvas.addGLEventListener(object : GLEventListener {
            override fun reshape(drawable: GLAutoDrawable?, x: Int, y: Int, width: Int, height: Int) {}
            override fun dispose(drawable: GLAutoDrawable?) {}

            override fun display(drawable: GLAutoDrawable) {

                val w = drawable.surfaceWidth
                val h = drawable.surfaceHeight

                val gle = Hybrid.gle
                val glgc = GLGraphicsContext(w, h, false, gle, true)

                JOGLProvider.gl2 = drawable.gl.gL2
                gle.setTarget(null)
                glgc.clear()

                val gl = gle.gl
                gl.viewport(0, 0, w, h)

                MapAreaDrawer.drawMap(glgc, _context, _master, width, height)
                JOGLProvider.gl2 = null
            }

            override fun init(drawable: GLAutoDrawable) {
                // Disassociate default workspace and assosciate the workspace from the GLEngine
                //	(so they can share resources)
                val primaryContext = JOGLProvider.context

                val unusedDefaultContext = drawable.context
                unusedDefaultContext.makeCurrent()
                drawable.setContext( null, true)

                val subContext = drawable.createContext( primaryContext)
                subContext.makeCurrent()
                drawable.setContext(subContext, true)
            }
        })
    }
}