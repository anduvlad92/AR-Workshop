package com.ar.my_andy

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class MyArFragment : ArFragment() {
    override fun getSessionConfiguration(session: Session?): Config? {
        val config = session?.config

        config?.setFocusMode(Config.FocusMode.AUTO)
            ?.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL)
            ?.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)

        return config
    }
}