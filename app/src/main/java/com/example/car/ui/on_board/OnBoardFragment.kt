package com.example.car.ui.on_board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.car.R
import com.example.car.data.models.OnBoardModel
import com.example.car.databinding.FragmentOnBoardBinding
import com.example.car.ui.on_board.adapter.OnBoardAdapter
import com.example.car.pref.Prefs

class OnBoardFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardBinding
    private lateinit var adapter: OnBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OnBoardAdapter(loadOnBoardData(), ::onStartBoard, ::onSkipBoard)
        binding.vpOnBoard.adapter = adapter
        binding.wormDotsIndicator.attachTo(binding.vpOnBoard)
    }

    private fun onSkipBoard() {
        binding.vpOnBoard.currentItem = loadOnBoardData().size
    }

    private fun onStartBoard() {
        val prefs = Prefs(requireContext())
        prefs.setOnBoardShown()

        findNavController().navigate(R.id.action_onBoardFragment_to_mainFragment)
    }

    private fun loadOnBoardData(): List<OnBoardModel> {
        return listOf(
            OnBoardModel(
                title = "Удобство",
                desc = "Создавайте заметки в два клика! Записывайте мысли, идеи и важные задачи мгновенно.",
                gif = R.drawable.convenience
            ),
            OnBoardModel(
                title = "Организация",
                desc = "Организуйте заметки по папкам и тегам. Легко находите нужную информацию в любое время.",
                gif = R.drawable.organization
            ),
            OnBoardModel(
                title = "Синхронизация",
                desc = "Синхронизация на всех устройствах. Доступ к записям в любое время и в любом месте.",
                gif = R.drawable.synchronization
            )
        )
    }
}
