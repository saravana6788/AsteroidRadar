package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidViewBinding

class AsteroidListAdapter(private val asteroids:List<Asteroid>, private val clickListener: AsteroidClickListener):
    RecyclerView.Adapter<AsteroidListAdapter.ViewHolder>() {


    class ViewHolder private constructor(private val binding: AsteroidViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroidData: Asteroid, asteroidClickListener: AsteroidClickListener) {
            binding.apply{
                asteroid = asteroidData
                asteroidName.text = asteroidData.codename
                asteroidCloseApproachDate.text = asteroidData.closeApproachDate
                clickListener = asteroidClickListener
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = AsteroidViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asteroids[position],clickListener)
    }

    override fun getItemCount(): Int {
        return asteroids.size
    }

}

class AsteroidClickListener(val clickListener: (asteroid:Asteroid)->Unit){
    fun onClick(asteroid:Asteroid) = clickListener(asteroid)

    }
