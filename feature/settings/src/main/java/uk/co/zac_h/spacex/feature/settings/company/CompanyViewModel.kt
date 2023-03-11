package uk.co.zac_h.spacex.feature.settings.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.async
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel() {

    private val _company = MutableLiveData<ApiResult<Company>>()
    val company: LiveData<ApiResult<Company>> = _company

    fun getCompany() {
        viewModelScope.launch {
            val response = async(_company) {
                repository.fetch(key = "agency", cachePolicy = CachePolicy.ALWAYS)
            }

            _company.value = response.await().map { Company(it) }
        }
    }

}