package com.sesac.angam.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.data.model.response.BidListResponse

class SellerBiddingRecyclerViewAdapter(
    private val tasks: MutableList<BidListResponse> = mutableListOf(),
    private val clickListener: (BidListResponse) -> Unit
) : RecyclerView.Adapter<SellerBiddingRecyclerViewAdapter.MyViewHolder>() {

    fun updateTasks(newTasks: List<BidListResponse>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.bidding_item, parent, false)
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
        private val name: TextView = view.findViewById(R.id.tv_bidder_name)
        private val bid: TextView = view.findViewById(R.id.tv_bidder_price)
        private val imageView: ImageView = view.findViewById(R.id.iv_bidder_image)
        private val sell: ImageView = view.findViewById(R.id.iv_sell)
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout)
        private var highestBid = "0"

        fun bind(task: BidListResponse, clickListener: (BidListResponse) -> Unit) {

            sell.visibility = View.GONE

            name.text = task.name
            highestBid = task.bid.toString()
            bid.text = task.bid.toString()

            if (task.image.isNotEmpty()) {
                Glide.with(view)
                    .load(task.image)
                    .into(imageView)
            }

            //첫번째 입찰자만
            if (adapterPosition == 0) {
//                Highest bid

                sell.visibility = View.VISIBLE
                layout.setBackgroundResource(R.drawable.bg_blue_point)
                name.setTextColor(view.resources.getColor(R.color.blue_main))
                bid.setTextColor(view.resources.getColor(R.color.blue_main))
            }

            view.setOnClickListener {
                clickListener(task)
            }
        }
    }
}