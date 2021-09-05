package uk.co.zac_h.spacex.about.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.dto.spacex.Company
import uk.co.zac_h.spacex.dto.spacex.CompanyResponse
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.map
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel() {

    private val _company = MutableLiveData<ApiResult<Company>>(ApiResult.pending())
    val company: LiveData<ApiResult<Company>> = _company

    fun getCompany() {
        viewModelScope.launch {
            val response = async {
                repository.fetch(key = "Company", cachePolicy = CachePolicy.ALWAYS)
            }

            _company.value = response.await().map { Company(it) }
        }
    }

}