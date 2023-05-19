package com.example.musicplayer.view.audioplayer

enum class StartMode {
    standby, play
}

enum class TransEvent {
    playTapped, stopTapped, doneTapped, failed
}

sealed class PlayerState {
    class start(val mode: StartMode): PlayerState()
    class playing(val parent: StartMode): PlayerState()
    class paused(val grand: StartMode): PlayerState()

    fun transition(event: TransEvent): PlayerState {
        if (event == TransEvent.doneTapped) {
            return start(StartMode.standby)
        }
        return when (this) {
            is start -> when (mode) {
                StartMode.play -> if (event == TransEvent.playTapped) playing(StartMode.play) else this
                StartMode.standby -> when (event) {
                    TransEvent.playTapped -> playing(StartMode.standby)
                    else -> this
                }
            }
            is playing -> when (event) {
                TransEvent.playTapped -> paused(this.parent)
                TransEvent.stopTapped, TransEvent.failed -> start(this.parent)
                else -> this
            }
            is paused -> when (event) {
                TransEvent.playTapped -> playing(this.grand)
                TransEvent.stopTapped -> start(StartMode.standby)
                else -> this
            }
        }
    }
}