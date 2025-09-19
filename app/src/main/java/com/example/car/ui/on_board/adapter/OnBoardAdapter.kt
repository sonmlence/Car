package com.example.car.ui.on_board.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.car.data.models.OnBoardModel
import com.example.car.databinding.ItemOnBoardBinding

class OnBoardAdapter(
    private val onBoardList: List<OnBoardModel>,
    val onStart: () -> Unit,
    val onSkip: () -> Unit
) :
    RecyclerView.Adapter<OnBoardAdapter.OnBoardViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnBoardViewHolder {
        return OnBoardViewHolder(
            ItemOnBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: OnBoardViewHolder,
        position: Int
    ) {
        holder.bind(onBoardList[position])
    }

    override fun getItemCount() = onBoardList.size

    inner class OnBoardViewHolder(private val binding: ItemOnBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(onBoardModel: OnBoardModel) {
            binding.tvTitle.text = onBoardModel.title
            binding.tvDesc.text = onBoardModel.desc
            Glide.with(binding.gifConvenience.context)
                .asGif()
                .load(onBoardModel.gif)
                .into(binding.gifConvenience)

            if ((adapterPosition) != onBoardList.size - 1) {
                binding.btnStart.visibility = View.GONE
                binding.tvSkip.setOnClickListener {
                    onSkip()
                }
            } else {
                binding.tvSkip.visibility = View.GONE
                binding.btnStart.setOnClickListener {
                    onStart()
                }
            }
        }
    }

}