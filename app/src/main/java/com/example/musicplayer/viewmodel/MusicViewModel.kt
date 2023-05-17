package com.example.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.musicplayer.model.data.MusicItem
import com.example.musicplayer.model.database.MusicDatabase
import com.example.musicplayer.model.repository.MusicRepository
import kotlinx.coroutines.launch

class MusicViewModel(private val repository: MusicRepository) : ViewModel() {
    val allMusic: LiveData<List<MusicItem>> = repository.allMusic.asLiveData()
    val activeMusicItem: LiveData<MusicItem> = repository.activeMusicItem.asLiveData()

    fun insert(music: MusicItem) = viewModelScope.launch {
        repository.insert(music)
    }

    fun insertAll(music: List<MusicItem>) = viewModelScope.launch {
        repository.insertAll(music)
    }

    fun setActive(music: MusicItem, value: Boolean) = viewModelScope.launch {
        repository.setActive(music, value)
    }

    fun update(music: MusicItem) = viewModelScope.launch {
        repository.update(music)
    }

    fun updateAll(music: List<MusicItem>) = viewModelScope.launch {
        repository.updateAll(music)
    }

    fun delete(music: MusicItem) = viewModelScope.launch {
        repository.delete(music)
    }
}