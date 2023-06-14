package com.sesac.angam.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.angam.R
import com.sesac.angam.data.model.response.RatingResponse

class RatingRecyclerViewAdapter(
    private val tasks: MutableList<RatingResponse> = mutableListOf(),
    private val clickListener: (RatingResponse) -> Unit
) : RecyclerView.Adapter<RatingRecyclerViewAdapter.MyViewHolder>() {

    fun updateTasks(newTasks: List<RatingResponse>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.rating_item, parent, false)
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
        private val history: TextView = view.findViewById(R.id.tv_history)
        private val keyword1: TextView = view.findViewById(R.id.keyword1)
        private val keyword2: TextView = view.findViewById(R.id.keyword2)
        private val keyword3: TextView = view.findViewById(R.id.keyword3)
        private val imageView: ImageView = view.findViewById(R.id.iv_clothes_image)
        private val heart: ImageView = view.findViewById(R.id.iv_heart)

        fun bind(task: RatingResponse, clickListener: (RatingResponse) -> Unit) {

            name.text = task.title
            brand.text = "["+task.brand+"]"

            if(task.history.isNotEmpty()) {
                history.text = task.history
            }

            //keyword
            keyword1.visibility = View.INVISIBLE
            keyword2.visibility = View.INVISIBLE
            keyword3.visibility = View.INVISIBLE

            if (task.keywordList != null) {
                if (task.keywordList.size > 0 && task.keywordList[0].isNotEmpty()) {
                    keyword1.visibility = View.VISIBLE
                    keyword1.text = task.keywordList[0]
                }
                if (task.keywordList.size > 1 && task.keywordList[1].isNotEmpty()) {
                    keyword2.visibility = View.VISIBLE
                    keyword2.text = task.keywordList[1]
                }
                if (task.keywordList.size > 2 && task.keywordList[2].isNotEmpty()) {
                    keyword3.visibility = View.VISIBLE
                    keyword3.text = task.keywordList[2]
                }
            }

            if (task.image.isNotEmpty()) {
                Glide.with(view)
                    .load(task.image)
                    .into(imageView)
            }

            if (task.liked) {
                Glide.with(view)
                    .load(R.drawable.ic_heart_on)
                    .into(heart)
            }

            view.setOnClickListener {
                clickListener(task)
            }
        }
    }
}