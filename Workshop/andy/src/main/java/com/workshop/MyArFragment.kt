package com.workshop

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.math.Vector3Evaluator
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.rendering.MaterialFactory


class MyArFragment : ArFragment() {

    override fun getSessionConfiguration(session: Session?): Config? {
        val config = session?.config

        config?.setFocusMode(Config.FocusMode.AUTO)
            ?.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL)
            ?.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)

        return config
    }


    fun onAddNewAndy(hitResult: HitResult, plane: Plane) {

        val futureAndy = ModelRenderable
            .builder()
            .setSource(context, R.raw.andy_dance)
            .build()

        val futureHat = ModelRenderable
            .builder()
            .setSource(context, R.raw.baseball_cap)
            .build()

        val futureBallon = MaterialFactory.makeOpaqueWithColor(
            context,
            com.google.ar.sceneform.rendering.Color(Color.RED)
        )
            .thenApply {
                ShapeFactory.makeSphere(0.5f, Vector3(0.0f, 0.0f, 0.0f), it)
            }


        futureAndy.thenCombine(futureHat, { andy, hat -> Pair(andy, hat) })
            .thenCombine(futureBallon, { pair, ballon -> Triple(pair.first, pair.second, ballon) })
            .thenAccept {
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arSceneView.scene)


                val baseNode = Node()
                baseNode.setParent(anchorNode)

                val andyNode = SkeletonNode()
                andyNode.renderable = it.first
                andyNode.setParent(baseNode)

                val boneNode = Node()
                boneNode.setParent(andyNode)
                andyNode.setBoneAttachment("hat_point", boneNode)

                val hatNode = Node()
                hatNode.renderable = it.second
                hatNode.setParent(boneNode)
                hatNode.worldScale = Vector3.one()
                hatNode.worldRotation = Quaternion.identity()
                val pos = hatNode.worldPosition


                pos.y -= .1f
                hatNode.worldPosition = pos

                andyNode.worldScale = Vector3(0.2f, 0.2f, 0.2f)

                val boneBallonNode = Node()
                boneBallonNode.setParent(andyNode)
                andyNode.setBoneAttachment("head_point", boneBallonNode)

                val ballonNode = Node()
                ballonNode.name = "ballon"
                ballonNode.renderable = it.third
                ballonNode.worldScale = Vector3(0.1f, 0.1f, 0.1f)
                ballonNode.localPosition = Vector3(0f, 0.3f, 0f)
                ballonNode.setParent(baseNode)

                val animator = ModelAnimator(it.first.getAnimationData(0), it.first)

                animator.repeatCount = 30
                animator.start()
                moveAndyAndDestroy(baseNode)
            }

    }


    fun moveAndyAndDestroy(node: Node) {
        val cameraPos = arSceneView.scene.camera.worldPosition
        val finalPos = Vector3(cameraPos.x, cameraPos.y - 0.2f, cameraPos.z - 0.5f)


        val objectAnimator = ObjectAnimator()
        objectAnimator.setTarget(node)
        objectAnimator.setAutoCancel(true)
        objectAnimator.setObjectValues(node.worldPosition, finalPos)
        objectAnimator.setPropertyName("worldPosition")
        objectAnimator.setDuration(5000)
        objectAnimator.setEvaluator(Vector3Evaluator())
        objectAnimator.setInterpolator(LinearInterpolator())
        objectAnimator.doOnEnd {
            val ballon = node.findByName("ballon")
            ballon?.setOnTapListener { hitTestResult, motionEvent ->
                showPoster(node)
            }
        }
        objectAnimator.start()
    }

    fun showPoster(node: Node) {
        val posterNode = node.children.get(1)
        val andyNode = node.children.get(0)


        val animatorSet = AnimatorSet()
        animatorSet.playTogether(dropAndy(andyNode), animatePosterScale(posterNode))
        animatorSet.start()

    }

    fun dropAndy(node: Node): ObjectAnimator {

        val objectAnimator = ObjectAnimator()
        objectAnimator.setTarget(node)
        objectAnimator.setAutoCancel(true)
        objectAnimator.setObjectValues(
            node.worldPosition,
            Vector3(node.worldPosition.x, -1.5f, node.worldPosition.z)
        )
        objectAnimator.setPropertyName("worldPosition")
        objectAnimator.setDuration(5000)
        objectAnimator.setEvaluator(Vector3Evaluator())
        objectAnimator.setInterpolator(LinearInterpolator())
        objectAnimator.doOnEnd {
            node.setParent(null)
        }
        return objectAnimator
    }

    fun animatePosterScale(node: Node): ObjectAnimator {
        val objectAnimator = ObjectAnimator()
        objectAnimator.setTarget(node)
        objectAnimator.setAutoCancel(true)
        objectAnimator.setObjectValues(
            node.worldScale,
            Vector3(0.2f, 0.2f, 0.2f)
        )
        objectAnimator.setPropertyName("worldScale")
        objectAnimator.setDuration(1000)
        objectAnimator.setEvaluator(Vector3Evaluator())
        objectAnimator.setInterpolator(LinearInterpolator())
        objectAnimator.doOnEnd {
            node.setParent(null)
        }
        return objectAnimator
    }


}

