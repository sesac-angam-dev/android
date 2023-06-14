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
import com.sesac.angam.data.model.response.EstimateResponse

class Selling3RecyclerViewAdapter(
    private val tasks: MutableList<EstimateResponse> = mutableListOf(),
    private val clickListener: (EstimateResponse) -> Unit
) : RecyclerView.Adapter<Selling3RecyclerViewAdapter.MyViewHolder>() {

    fun updateTasks(newTasks: List<EstimateResponse>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.expected_selling_item, parent, false)
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
        private val meanBid: TextView = view.findViewById(R.id.tv_price)
        private val bidderName1: TextView = view.findViewById(R.id.tv_bidder_name1)
        private val bidderName2: TextView = view.findViewById(R.id.tv_bidder_name2)
        private val bidderName3: TextView = view.findViewById(R.id.tv_bidder_name3)
        private val bidderImage1: ImageView = view.findViewById(R.id.iv_bidder_image1)
        private val bidderImage2: ImageView = view.findViewById(R.id.iv_bidder_image2)
        private val bidderImage3: ImageView = view.findViewById(R.id.iv_bidder_image3)
        private val bidderPrice1: TextView = view.findViewById(R.id.tv_bidder_price1)
        private val bidderPrice2: TextView = view.findViewById(R.id.tv_bidder_price2)
        private val bidderPrice3: TextView = view.findViewById(R.id.tv_bidder_price3)

        var isClicked = false
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout)


        fun bind(task: EstimateResponse, clickListener: (EstimateResponse) -> Unit) {

            name.text = task.title
            brand.text = "["+task.brand+"]"
            meanBid.text = task.meanBid.toString()

            // 입찰자 정보가 있는 경우에만 작업 수행
            if (task.userBidInfoList.size >= 1) {
                if (task.userBidInfoList[0].name.isNotEmpty()) {
                    bidderName1.text = task.userBidInfoList[0].name
                }
                if (task.userBidInfoList[0].bid.toString().isNotEmpty()) {
                    bidderPrice1.text = task.userBidInfoList[0].bid.toString()
                }
                if (task.userBidInfoList[0].image.isNotEmpty()) {
                    Glide.with(view)
                        .load(task.userBidInfoList[0].image)
                        .into(bidderImage1)
                }
            }

            if (task.userBidInfoList.size >= 2) {
                if (task.userBidInfoList[1].name.isNotEmpty()) {
                    bidderName2.text = task.userBidInfoList[1].name
                }
                if (task.userBidInfoList[1].bid.toString().isNotEmpty()) {
                    bidderPrice2.text = task.userBidInfoList[1].bid.toString()
                }
                if (task.userBidInfoList[1].image.isNotEmpty()) {
                    Glide.with(view)
                        .load(task.userBidInfoList[1].image)
                        .into(bidderImage2)
                }
            }

            if (task.userBidInfoList.size >= 3) {
                if (task.userBidInfoList[2].name.isNotEmpty()) {
                    bidderName3.text = task.userBidInfoList[2].name
                }
                if (task.userBidInfoList[2].bid.toString().isNotEmpty()) {
                    bidderPrice3.text = task.userBidInfoList[2].bid.toString()
                }
                if (task.userBidInfoList[2].image.isNotEmpty()) {
                    Glide.with(view)
                        .load(task.userBidInfoList[2].image)
                        .into(bidderImage3)
                }
            }


            view.setOnClickListener {
                if(isClicked){
                    layout.background = view.resources.getDrawable(R.drawable.bg_selling_gray)
                    isClicked = false
                }else {
                    layout.background = view.resources.getDrawable(R.drawable.bg_selling_blue)
                    isClicked = true
                }
                clickListener(task)
            }
        }
    }
}