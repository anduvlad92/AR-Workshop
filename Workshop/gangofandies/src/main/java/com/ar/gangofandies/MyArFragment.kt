package com.ar.gangofandies

import com.google.ar.core.Config
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment

class MyArFragment : ArFragment() {

    override fun getSessionConfiguration(session: Session?): Config? {
        val config = session?.config

        config?.setFocusMode(Config.FocusMode.AUTO)
            ?.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL)
            ?.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)

        return config
    }

    fun onAddGang(hitResult: HitResult?, plane: Plane?) {

        ModelRenderable
            .builder()
            .setSource(context, R.raw.andy_dance)
            .build()
            .thenAccept {
                val anchor = hitResult?.createAnchor()
                val anchorNode = AnchorNode(anchor)

                anchorNode.renderable = it
                anchorNode.worldScale = Vector3(0.2f, 0.2f, 0.2f)
                anchorNode.setParent(arSceneView.scene)
                createGang(anchorNode, it)
            }
    }

    fun createGang(node: Node, renderable: ModelRenderable) {

        val rightAndy = Node()
        rightAndy.setParent(node)
        rightAndy.localPosition = Vector3(0.5f, 0f, 0f)
        rightAndy.renderable = renderable
        rightAndy.worldScale = Vector3(0.1f, 0.1f, 0.1f)

        val leftAndy = Node()
        leftAndy.setParent(node)
        leftAndy.localPosition = Vector3(-0.5f, 0f, 0f)
        leftAndy.renderable = renderable
        leftAndy.worldScale = Vector3(0.1f, 0.1f, 0.1f)

        val frontAndy = Node()
        frontAndy.setParent(node)
        frontAndy.localPosition = Vector3(0.0f, 0f, 0.5f)
        frontAndy.renderable = renderable
        frontAndy.worldScale = Vector3(0.1f, 0.1f, 0.1f)
    }

}