package com.service.codingtest.view.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.codingtest.R
import com.service.codingtest.db.AppDB
import com.service.codingtest.model.response.FavoriteEntity
import com.service.codingtest.model.response.ItemsEntity
import com.service.codingtest.network.MLog
import kotlinx.android.synthetic.main.item_image.view.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ImageAdapter: PagingDataAdapter<ItemsEntity, ImageAdapter.ViewHolder>(ChatDiffCallback) {

    private val TAG = ImageAdapter::class.java.name

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_image, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)!!

        Glide.with(holder.itemView.context).load(data.thumbnail)
            .into(holder.iv_thumb)

        holder.tv_title.text = data.title

        holder.tv_contents.text = data.contents

        if(data.sale_price > 0) {
            val dFormatter = DecimalFormat("###,###,###")
            holder.tv_sale_price.text = dFormatter.format(data.sale_price)
        }

        if(data.datetime.isNotEmpty()) {
            val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val result1: Date = df1.parse(data.datetime) as Date
            val rewardString = format.format(result1)
            holder.tv_datetime.text = rewardString
        }

        holder.cb_favorite.setOnCheckedChangeListener(null)
        holder.cb_favorite.isChecked = data.isFavorite
        holder.cb_favorite.setOnCheckedChangeListener { compoundButton, b ->

//            data.isFavorite = b
//            AppDB.getInstance(holder.itemView.context).imageDao().update(data)

            AppDB.getInstance(holder.itemView.context).imageDao().updateisFavorite(data.isbn, b)

            val favoriteEntity = FavoriteEntity(
                0,
                data.isbn,
                data.title,
                data.thumbnail,
                data.searchWord,
            )
            if (b) {
                AppDB.getInstance(holder.itemView.context).favoriteDao().insert(favoriteEntity)
            } else {
                AppDB.getInstance(holder.itemView.context).favoriteDao().delete(favoriteEntity)
            }

            MLog.d(TAG, data.isbn +  " / " + b + " setOnCheckedChangeListener")
        }

        MLog.d(TAG, data.isbn +  " / " + data.isFavorite + " / "+data.isbn)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_thumb = itemView.iv_thumb
        val cb_favorite = itemView.cb_favorite
        val tv_title = itemView.tv_title
        val tv_contents = itemView.tv_contents
        val tv_sale_price = itemView.tv_sale_price
        val tv_datetime = itemView.tv_datetime

    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val ChatDiffCallback = object : DiffUtil.ItemCallback<ItemsEntity>() {
            override fun areItemsTheSame(oldItem: ItemsEntity, newItem: ItemsEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsEntity, newItem: ItemsEntity): Boolean {
                return oldItem.isbn == newItem.isbn
            }

            override fun getChangePayload(oldItem: ItemsEntity, newItem: ItemsEntity): Any? {
                return if (sameExceptScore(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptScore(oldItem: ItemsEntity, newItem: ItemsEntity): Boolean {
            return oldItem.copy(isbn = newItem.isbn) == newItem
        }
    }
}