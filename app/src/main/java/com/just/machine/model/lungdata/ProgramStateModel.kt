package com.just.machine.model.lungdata

class ProgramStateModel {
    private var isLock: Boolean = false
    var oldOne: ProgramState? = null
    var newOne: ProgramState? = null

    fun lockState() {
        isLock = true
    }

    fun releaseLockState() {
        isLock = false
    }

    fun update(newState: ProgramState) {
        if (isLock) return
        oldOne = newOne
        newOne = newState
    }

    enum class ProgramState {
        warming,
        disconnected,
        connected,
        ready,
        calibrating,
        setting,
        cpxing,
        analyzing,
        reporting
    }
}
