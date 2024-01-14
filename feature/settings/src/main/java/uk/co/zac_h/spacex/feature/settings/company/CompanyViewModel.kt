package uk.co.zac_h.spacex.feature.settings.company

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.apiFlow
import uk.co.zac_h.spacex.network.toType
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val repository: CompanyRepository
) : ViewModel() {

    val company: Flow<ApiResult<Company>> = apiFlow {
        repository.fetch(key = "agency", cachePolicy = CachePolicy.ALWAYS)
    }.toType(::Company)
}