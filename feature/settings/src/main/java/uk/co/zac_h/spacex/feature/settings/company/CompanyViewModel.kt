package uk.co.zac_h.spacex.feature.settings.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.get
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel() {

    private val _company: MutableStateFlow<ApiResult<Company>> = MutableStateFlow(ApiResult.Pending)
    val company: Flow<ApiResult<Company>> = _company

    init {
        getCompany()
    }

    fun getCompany() {
        _company.get(viewModelScope) {
            repository.fetch(key = "agency", cachePolicy = CachePolicy.ALWAYS).map(::Company)
        }
    }
}