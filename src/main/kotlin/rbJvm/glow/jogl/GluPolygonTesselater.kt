package rbJvm.glow.jogl

import com.jogamp.opengl.glu.GLU
import com.jogamp.opengl.glu.GLUtessellatorCallback
import rb.glow.gle.GLPrimitive
import rb.glow.gle.Vec2uv
import rb.glow.glu.IPolygonTesselator
import rb.vectrix.compaction.FloatCompactor
import rb.vectrix.mathUtil.MathUtil
import rb.vectrix.mathUtil.d
import java.lang.Exception

class GluPolytessException(message:String) : Exception(message)

object GluPolygonTesselater :IPolygonTesselator {
    override fun tesselatePolygon( x: Sequence<Double>, y: Sequence<Double>, count: Int) : GLPrimitive {
        println("start")
        val tess = GLU.gluNewTess()
        val callback = GLUTCB()
        val xi = x.iterator()
        val yi = y.iterator()

        GLU.gluTessCallback(tess, GLU.GLU_TESS_VERTEX, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_BEGIN, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_END, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_ERROR, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_COMBINE, callback)

        GLU.gluTessProperty(tess,
            GLU.GLU_TESS_WINDING_RULE,
            GLU.GLU_TESS_WINDING_ODD.toDouble())
        GLU.gluTessBeginPolygon(tess, null)
        GLU.gluTessBeginContour(tess)
        for (i in 0 until count) {
            val buffer = doubleArrayOf(xi.next(), yi.next(), 0.0, 7.0, 8.0)
            GLU.gluTessVertex(tess, buffer, 0, buffer)
        }
        GLU.gluTessEndContour(tess)
        GLU.gluTessEndPolygon(tess)
        GLU.gluDeleteTess(tess)

        println("end")
        return callback.buildPrimitive()
    }

    private class GLUTCB : GLUtessellatorCallback {
        private val data = FloatCompactor()
        private val types = mutableListOf<Int>()
        private val lengths = mutableListOf<Int>()
        private var currentLength = 0

        override fun begin(type: Int) {
            types.add(type)
        }

        override fun combine(coords: DoubleArray, data: Array<Any>, weight: FloatArray, out: Array<Any>) {
            val d = coords
            println("combine: ${d[0]}, ${d[1]}, ${d[2]}, ${d.getOrNull(3)}, ${d.getOrNull(4)}")
            out[0] = coords
        }

        override fun edgeFlag(arg0: Boolean) {}

        override fun end() {
            lengths.add(currentLength)
            currentLength = 0
        }

        override fun error(errnum: Int) {
            val estring: String = GLU().gluErrorString(errnum)

            throw GluPolytessException("Tessellation Error: $estring")
        }

        override fun vertex(arg0: Any) {
            val d = arg0 as DoubleArray
            data.add(d[0].toFloat())
            data.add(d[1].toFloat())

            println("${d[0]}, ${d[1]}, ${d[2]}, ${d.getOrNull(3)}, ${d.getOrNull(4)}")
            ++currentLength
        }

        fun buildPrimitive(): GLPrimitive {
            val len = Math.min(types.size, lengths.size)
            val ptypes = IntArray(len)
            val plengths = IntArray(len)
            for (i in 0 until len) {
                ptypes[i] = types[i]
                plengths[i] = lengths[i]
            }

            return GLPrimitive(data.toArray(), intArrayOf(2), ptypes, plengths)
        }

        override fun edgeFlagData(arg0: Boolean, arg1: Any) {}
        override fun beginData(type: Int, polygonData: Any) {}
        override fun combineData(coords: DoubleArray, data: Array<Any>, weight: FloatArray, out: Array<Any>, arg4: Any) {}
        override fun endData(arg0: Any) {}
        override fun errorData(arg0: Int, arg1: Any) {}
        override fun vertexData(arg0: Any, arg1: Any) {}

    }



    override fun tesselatePolygon( points: Sequence<Vec2uv>, count: Int) : GLPrimitive {
        val tess = GLU.gluNewTess()
        val callback = GLUTCB()
        val pi = points.iterator()

        GLU.gluTessCallback(tess, GLU.GLU_TESS_VERTEX, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_BEGIN, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_END, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_ERROR, callback)
        GLU.gluTessCallback(tess, GLU.GLU_TESS_COMBINE, callback)

        GLU.gluTessProperty(tess,
            GLU.GLU_TESS_WINDING_RULE,
            GLU.GLU_TESS_WINDING_ODD.toDouble())
        GLU.gluTessBeginPolygon(tess, null)
        GLU.gluTessBeginContour(tess)
        for (i in 0 until count) {
            val point = pi.next()
            val buffer = doubleArrayOf(point.x.d, point.y.d, 0.0, point.u.d, point.v.d)
            GLU.gluTessVertex(tess, buffer, 0, buffer)
        }
        GLU.gluTessEndContour(tess)
        GLU.gluTessEndPolygon(tess)
        GLU.gluDeleteTess(tess)

        return callback.buildPrimitive()
    }

    private class GLUTCBuv: GLUtessellatorCallback {
        private val data = FloatCompactor()
        private val types = mutableListOf<Int>()
        private val lengths = mutableListOf<Int>()
        private var currentLength = 0

        override fun begin(type: Int) {
            types.add(type)
        }

        override fun combine(coords: DoubleArray, data: Array<Any>, weight: FloatArray, out: Array<Any>) {
            val d1 = data.getOrNull(0) as? DoubleArray
            val d2 = data.getOrNull(0) as? DoubleArray
            val d3 = data.getOrNull(0) as? DoubleArray
            val d4 = data.getOrNull(0) as? DoubleArray

            val u = (d1?.getOrNull(3) ?: 0.0) * weight[0].d +
                    (d2?.getOrNull(3) ?: 0.0) * weight[0].d +
                    (d3?.getOrNull(3) ?: 0.0) * weight[0].d +
                    (d4?.getOrNull(3) ?: 0.0) * weight[0].d
            val v = (d1?.getOrNull(4) ?: 0.0) * weight[0].d +
                    (d2?.getOrNull(4) ?: 0.0) * weight[0].d +
                    (d3?.getOrNull(4) ?: 0.0) * weight[0].d +
                    (d4?.getOrNull(4) ?: 0.0) * weight[0].d
            out[0] = doubleArrayOf(coords[0], coords[1], coords[2], u, v)
        }

        override fun edgeFlag(arg0: Boolean) {}

        override fun end() {
            lengths.add(currentLength)
            currentLength = 0
        }

        override fun error(errnum: Int) {
            val estring: String = GLU().gluErrorString(errnum)

            throw GluPolytessException("Tessellation Error: $estring")
        }

        override fun vertex(arg0: Any) {
            val d = arg0 as DoubleArray
            data.add(d[0].toFloat())
            data.add(d[1].toFloat())

            println("${d[0]}, ${d[1]}, ${d[2]}, ${d.getOrNull(3)}, ${d.getOrNull(4)}")
            ++currentLength
        }

        fun buildPrimitive(): GLPrimitive {
            val len = Math.min(types.size, lengths.size)
            val ptypes = IntArray(len)
            val plengths = IntArray(len)
            for (i in 0 until len) {
                ptypes[i] = types[i]
                plengths[i] = lengths[i]
            }

            return GLPrimitive(data.toArray(), intArrayOf(2), ptypes, plengths)
        }

        override fun edgeFlagData(arg0: Boolean, arg1: Any) {}
        override fun beginData(type: Int, polygonData: Any) {}
        override fun combineData(coords: DoubleArray, data: Array<Any>, weight: FloatArray, out: Array<Any>, arg4: Any) {}
        override fun endData(arg0: Any) {}
        override fun errorData(arg0: Int, arg1: Any) {}
        override fun vertexData(arg0: Any, arg1: Any) {}

    }
}