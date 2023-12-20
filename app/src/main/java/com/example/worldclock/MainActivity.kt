package com.example.worldclock
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.worldclock.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val baseURL = "http://worldtimeapi.org/api/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetTime.setOnClickListener {
            val cityName = binding.editCityName.text.toString()
             if (cityName.isNotEmpty()) {
                 getWorldTime(cityName)
             }
        }
    }

    private fun getWorldTime(city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WorldClockService::class.java)
        val call = service.getWorldTime(city)

        call.enqueue(object: Callback<WorldClockResponse> {
            override fun onResponse(
                call: Call<WorldClockResponse>,
                response: Response<WorldClockResponse>
            ) {
                if (response.isSuccessful) {
                    val worldTimeResponse = response.body()
                    val response = worldTimeResponse?.datetime ?: "Невідомий час"
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val date = dateFormat.parse(response)
                    val formatter = SimpleDateFormat("HH:mm:ss \nyyyy-MM-dd")
                    val dateString = formatter.format(date)
                    binding.textWorldTime.text = "Місцевий час в $city: \n$dateString"
                } else {
                    binding.textWorldTime.text = "Помилка в отриманні часу"
                }
            }

            override fun onFailure(call: Call<WorldClockResponse>, t: Throwable) {
                binding.textWorldTime.text = "Помилка: ${t.message}"
            }
        })
    }
}
