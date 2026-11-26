package uk.co.zac_h.spacex.feature.settings.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.withRefresh
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel() {

    private val refresh = MutableStateFlow<ApiResult<Unit>>(ApiResult.Pending)

    val company: Flow<ApiResult<Company>> =
        repository.agency.map { agency -> agency?.let(::Company) }.withRefresh(refresh)

    init {
        getCompany()
    }

    /** Fetches the agency if the stored one is out of date. */
    fun getCompany() {
        refresh.value = ApiResult.Pending
        viewModelScope.launch { refresh.value = repository.refreshAgency() }
    }
}