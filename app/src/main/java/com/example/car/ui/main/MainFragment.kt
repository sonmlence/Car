package com.example.car.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.car.data.models.CarModel
import com.example.car.databinding.FragmentMainBinding
import com.example.car.ui.main.adapter.CarAdapter

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val list = arrayListOf(
        CarModel(
            img = "https://media.dealeralchemist.com/jellies/Toyota/Camry/C453129_040_Side.png?auto=compress%2Cformat",
            carName = "Toyota Camry",
            carPrice = "65$/day"
        ),
        CarModel(
            img = "https://www.carsized.com/resources/audi/r8/c-v10/2015/sm_211132102_audi-r8-2015-side-view_4x.png",
            carName = "Audi R8",
            carPrice = "120$/day"
        ),
        CarModel(
            img = "https://pngimg.com/d/mercedes_PNG1842.png",
            carName = "Mercedes",
            carPrice = "70$/day"
        )
    )

    private val carAdapter = CarAdapter(list) { carModel ->
        val action = MainFragmentDirections.actionMainFragmentToDetailCarFragment(carModel)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCar.adapter = carAdapter
        }
}
