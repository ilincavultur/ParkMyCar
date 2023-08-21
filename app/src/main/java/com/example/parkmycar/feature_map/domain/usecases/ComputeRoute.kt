package com.example.parkmycar.feature_map.domain.usecases

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.parkmycar.core.util.Resource
import com.example.parkmycar.feature_map.domain.models.Spot
import com.example.parkmycar.feature_map.domain.repository.SpotRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class ComputeRoute(
    private val repository: SpotRepository
) {
    suspend operator fun invoke(source: String, target: String) : Flow<Resource<MutableList<List<LatLng>>>> {
        return repository.computeRoute(source, target)
    }
}