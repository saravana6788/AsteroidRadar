package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidClickListener
import com.udacity.asteroidradar.adapter.AsteroidListAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(requireActivity().application)).get(
                MainViewModel::class.java
            )
        binding.viewModel = this.viewModel
        setHasOptionsMenu(true)

        viewModel.asteroidData.observe(viewLifecycleOwner) {
            with(binding.asteroidRecycler) {
                Log.i("AsteroidSize", it.size.toString())
                adapter =
                    AsteroidListAdapter(it, AsteroidClickListener { asteroid ->
                        findNavController().navigate(
                            MainFragmentDirections.actionShowDetail(
                                asteroid
                            )
                        )
                    })
            }

        }





        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewModel.filterAsteroids(
                when (item.itemId) {
                    R.id.today_asteroids -> AsteroidIntervals.TODAY
                    R.id.next_week_asteroids -> AsteroidIntervals.WEEK
                    else -> AsteroidIntervals.ALL
                }
            )
        }
        return true
    }
}

enum class AsteroidIntervals {
    TODAY,
    WEEK,
    ALL
}