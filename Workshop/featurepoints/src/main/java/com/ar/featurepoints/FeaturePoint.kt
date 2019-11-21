package com.ar.featurepoints

import com.google.ar.sceneform.math.Vector3

class FeaturePoint(val coordinates: Vector3, val confidence: Float){


    override fun toString(): String {
        return "FeaturePoint(coordinates=$coordinates, confidence=$confidence)"
    }
}