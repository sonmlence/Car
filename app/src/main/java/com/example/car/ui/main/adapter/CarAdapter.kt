package com.example.car.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.car.data.models.CarModel
import com.example.car.databinding.ItemCarBinding
import com.example.car.utils.loadImg

class CarAdapter(val carList: List<CarModel>, val onClick:(carModel: CarModel)-> Unit) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarViewHolder {
        return CarViewHolder(
            ItemCarBinding.inflate(
                (LayoutInflater.from(parent.context)),
                        parent, false
            )
            )

    }

    override fun onBindViewHolder(
        holder: CarViewHolder,
        position: Int
    ) {
        holder.onBind(carList[position])
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    inner class CarViewHolder(private val binding: ItemCarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(carModel: CarModel){
            binding.tvCamry.text=carModel.carName
            binding.tvPrice.text=carModel.carPrice

            binding.imgCar.loadImg(carModel.img)

            itemView.setOnClickListener {
onClick(carModel)
            }
        }
    }
}