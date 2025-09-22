package com.example.car.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.car.data.models.CarModel
import com.example.car.databinding.FragmentDetailCarBinding

class DetailCarFragment : Fragment() {

    private lateinit var binding: FragmentDetailCarBinding
    private val args: DetailCarFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailCarBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val car: CarModel = args.carModel

        binding.tvCar.text = car.carName
        binding.tvShape.text = car.carPrice

        Glide.with(binding.ivCar)
            .load(car.img)
            .into(binding.ivCar)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}