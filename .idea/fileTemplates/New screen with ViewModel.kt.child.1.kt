

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class ${FILE_NAME} @Inject constructor() {

    val state = MutableStateFlow<${NAME}State>(${NAME}State.Loading)
    
    
}
