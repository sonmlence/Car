package com.example.car

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.car.databinding.ItemCarBinding

class CarAdapter(val carList: List<CarModel>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

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

    class CarViewHolder(private val binding: ItemCarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(carModel: CarModel){
            binding.tvCamry.text=carModel.carName
            binding.tvPrice.text=carModel.carPrice

            binding.imgCar.loadImg(carModel.img)
        }
    }
}

