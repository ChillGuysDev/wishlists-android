

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
internal fun ${FILE_NAME}(viewModel: ${NAME}ViewModel) {
    val state by viewModel.state.collectAsState()
    ${NAME}Content(state)
}

@Composable
internal fun ${NAME}Content(state: ${NAME}State) {
    
}
