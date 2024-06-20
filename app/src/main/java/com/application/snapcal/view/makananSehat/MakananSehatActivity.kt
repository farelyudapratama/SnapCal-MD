package com.application.snapcal.view.makananSehat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.snapcal.R
import com.application.snapcal.databinding.ActivityMakananSehatBinding
import com.bumptech.glide.Glide

class MakananSehatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMakananSehatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakananSehatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val foodList = listOf(
            MakananSehat("Nasi Merah", 110, 2, 0, R.drawable.nasi_merah),
            MakananSehat("Sup Ayam", 180 , 16 , 2, R.drawable.sup_ayam),
            MakananSehat("Salad Buah", 100, 1, 14, R.drawable.salad_buah),
            MakananSehat("Tempe Rebus", 100, 10, 1, R.drawable.tempe_rebus),
            MakananSehat("Tahu Kukus", 70, 8, 1, R.drawable.tahu_kukus),
            MakananSehat("Sayur Bayam", 40, 3, 0, R.drawable.sayur_bayam),
            MakananSehat("Ikan Bakar", 150, 20, 0, R.drawable.ikan_bakar),
            MakananSehat("Brokoli Rebus", 55, 4, 2,R.drawable.brokoli_rebus),
            MakananSehat("Ubi Rebus", 112, 2, 5, R.drawable.ubi_rebus),
            MakananSehat("Bubur Kacang Hijau", 250 , 10, 10, R.drawable.bubur_kacang_ijo)
        )

        recyclerView.adapter = FoodAdapter(foodList)
    }
}

class FoodAdapter(private val foodList: List<MakananSehat>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodName: TextView = view.findViewById(R.id.foodName)
        val calories: TextView = view.findViewById(R.id.calories)
        val protein: TextView = view.findViewById(R.id.protein)
        val sugar: TextView = view.findViewById(R.id.sugar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan_sehat, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        Glide.with(holder.itemView.context)
            .load(food.imageResId)
            .into(holder.itemView.findViewById(R.id.foodImage))
        holder.foodName.text = food.name
        holder.calories.text = "Kalori: ${food.calories}"
        holder.protein.text = "Protein: ${food.protein}g"
        holder.sugar.text = "Gula: ${food.sugar}g"
    }

    override fun getItemCount() = foodList.size
}
