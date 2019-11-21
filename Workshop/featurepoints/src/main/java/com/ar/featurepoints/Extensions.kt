package com.ar.featurepoints

import com.google.ar.sceneform.math.Vector3
import java.nio.FloatBuffer

fun FloatBuffer.toFeaturePointsList(): ArrayList<FeaturePoint> {
    val result = arrayListOf<FeaturePoint>()

    val numOfFeaturePoints = limit() / 4

    if (numOfFeaturePoints > 0) {
        for (i in 0 until limit() / 4) {

            val coordinates = Vector3()
            coordinates.x = get(i * 4)
            coordinates.y = get(i * 4 + 1)
            coordinates.z = get(i * 4 + 2)
            val confidence = get(i * 4 + 3)

            result.add(FeaturePoint(coordinates, confidence))
        }
    }
    return result
}
