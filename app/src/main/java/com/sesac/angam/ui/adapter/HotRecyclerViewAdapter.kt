package com.sesac.angam.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.angam.R
import com.sesac.angam.data.model.response.HotResponse

class HotRecyclerViewAdapter(
    private val tasks: MutableList<HotResponse> = mutableListOf(),
    private val clickListener: (HotResponse) -> Unit
) : RecyclerView.Adapter<HotRecyclerViewAdapter.MyViewHolder>() {

    fun updateTasks(newTasks: List<HotResponse>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.hot_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (tasks.isNotEmpty()) {
            val task = tasks[position]
            holder.bind(task, clickListener)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // UI 요소에 대한 참조를 정의합니다.
        private val name: TextView = view.findViewById(R.id.tv_name)
        private val brand: TextView = view.findViewById(R.id.tv_brand)
        private val imageView: ImageView = view.findViewById(R.id.iv_clothes_image)
        private val heart: ImageView = view.findViewById(R.id.iv_heart)
        private val layout: ConstraintLayout = view.findViewById(R.id.cl_hot_item)

        fun bind(task: HotResponse, clickListener: (HotResponse) -> Unit) {

            name.text = task.title
            brand.text = "["+task.brand+"]"

            if (task.image.isNotEmpty()) {
                Glide.with(view)
                    .load(task.image)
                    .into(imageView)
            }

            if (task.liked) {
                Glide.with(view)
                    .load(R.drawable.ic_heart_on)
                    .into(heart)
                layout.setBackgroundResource(R.drawable.bg_heart_on)
            }

            view.setOnClickListener {
                clickListener(task)
            }
        }
    }
}