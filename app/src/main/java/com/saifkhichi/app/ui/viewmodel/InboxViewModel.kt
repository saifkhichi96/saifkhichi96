package com.saifkhichi.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saifkhichi.app.data.repo.InboxRepository
import com.saifkhichi.app.model.Inbox
import com.saifkhichi.app.model.Result
import com.saifkhichi.app.model.Thread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Stores and prepares the Inbox data for displaying.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
@HiltViewModel
class InboxViewModel @Inject constructor(var repository: InboxRepository) : ViewModel() {

    private val _inbox = MutableLiveData<Result<Inbox>>()
    val inbox: LiveData<Result<Inbox>> = _inbox

    private val _threads = MutableLiveData<Result<List<Thread>>>()
    val threads: LiveData<Result<List<Thread>>> = _threads

    init {
        repository.messages?.let {
            _inbox.value = Result.Success(it)
        }
    }

    /**
     * Asynchronously fetches the messages in the Inbox and returns the
     * Result to the associated live data.
     */
    fun getAllMessages() {
        viewModelScope.launch {
            val result = repository.listAll()
            _threads.value = when (result) {
                is Result.Success -> Result.Success(result.data.toThreads())
                is Result.Error -> result
            }
        }
    }

}