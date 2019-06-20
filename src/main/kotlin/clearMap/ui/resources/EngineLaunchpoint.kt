package clearMap.ui.resources

import clearMap.gl.GL330ShaderLoader
import rb.glow.gle.GLEngine
import rb.glow.gle.IGLEngine
import rbJvm.glow.awt.AwtImageConverter
import rbJvm.glow.jogl.GluPolygonTesselater
import rbJvm.glow.jogl.JOGLContext
import rbJvm.glow.jogl.JOGLProvider

object EngineLaunchpoint {
    val gle : IGLEngine by lazy {
        GLEngine(
            GluPolygonTesselater,
            GL330ShaderLoader(JOGLProvider.gl, JClassScriptService(ConsoleLogger)),
            converter,
            JOGLContext()
        )
    }

    val converter = AwtImageConverter {gle}
}