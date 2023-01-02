package com.ead.project.dreamer.ui.directory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ead.project.dreamer.data.database.model.AnimeBase
import com.ead.project.dreamer.domain.DirectoryManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DirectoryViewModel @Inject constructor(
    private val directoryManager: DirectoryManager
): ViewModel() {

    fun getDirectory(title : String) : LiveData<List<AnimeBase>> =
        directoryManager.getDirectoryList.livedata(title,false)

    fun getFullDirectory(title: String): LiveData<List<AnimeBase>> =
        directoryManager.getDirectoryList.livedata(title,true)
}