package uk.co.zac_h.spacex.about.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.utils.ApiResult
import uk.co.zac_h.spacex.utils.map
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel() {

    private val _company = MutableLiveData<ApiResult<Company>>()
    val company: LiveData<ApiResult<Company>> = _company

    init {
        viewModelScope.launch {
            _company.value = ApiResult.pending()
            val response = async(SupervisorJob()) {
                repository.getCompany()
            }

            withContext(Dispatchers.Main) {
                _company.value = response.map { Company(it) }
            }
        }
    }

}