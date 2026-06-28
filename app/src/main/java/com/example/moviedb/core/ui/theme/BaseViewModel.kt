package com.example.moviedb.core.ui.theme

import androidx.lifecycle.ViewModel
import com.example.moviedb.core.ui.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<S: ViewState>(
    initialState: S
): ViewModel() {
    protected val stateField = MutableStateFlow(initialState)
    open val state: StateFlow<S> = stateField.asStateFlow()
}