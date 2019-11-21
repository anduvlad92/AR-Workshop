package com.ar.featurepoints

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.Config
import com.google.ar.core.PointCloud
import com.google.ar.core.Session
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import java.lang.Exception
import com.google.ar.sceneform.rendering.MaterialFactory


class MyArFragment : ArFragment() {

    var featurePointMaterial: Material? = null
    var featurePointCreating: Boolean = false
    var isInProcess: Boolean = false
    val featurePointNodesHolder: ArrayList<Node> = ArrayList()
    var lastFeaturePointsProcessing: Long = 0

    override fun getSessionConfiguration(session: Session?): Config? {
        val config = session?.config

        session?.run {
            setupFeaturePointMaterial()
        }

        config?.setFocusMode(Config.FocusMode.AUTO)
            ?.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL)
            ?.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)

        return config
    }

    fun setupFeaturePointMaterial() {
        if (featurePointMaterial == null && !featurePointCreating) {
            featurePointCreating = true
            val color = Color(0f, 0f, 1f, 1f)
            MaterialFactory.makeOpaqueWithColor(context, color)
                .thenAccept {
                    featurePointMaterial = it
                }
        }
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)
        if (isInProcess) {
            return
        }
        if (featurePointMaterial != null)
            frameTime?.run {
                var pointCloud: PointCloud? = null


                try {
                    isInProcess = true

                    arSceneView.arFrame?.run {
                        if (timestamp - lastFeaturePointsProcessing < 300000000)
                            return
                        lastFeaturePointsProcessing = timestamp

                        pointCloud = acquirePointCloud()
                    }

                    //Remove previous nodes
                    featurePointNodesHolder.forEach {
                        arSceneView.scene.removeChild(it)
                    }
                    featurePointNodesHolder.clear()

                    //Add new feature points
                    pointCloud?.points?.toFeaturePointsList()
                        ?.filter {
                            it.confidence > 0.6
                        }
                        ?.take(40)?.forEach {
                            val node = Node()
                            node.renderable = ShapeFactory.makeSphere(
                                0.005f,
                                Vector3(it.coordinates.x, it.coordinates.y, it.coordinates.z),
                                featurePointMaterial
                            )

                            node.renderable!!.isShadowCaster = false
                            node.setParent(arSceneView.scene)
                            featurePointNodesHolder.add(node)
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isInProcess = false
                    pointCloud?.release()
                }
            }
    }
}

