package com.service.codingtest.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.service.codingtest.R
import com.service.codingtest.db.AppDB
import com.service.codingtest.model.response.FavoriteEntity
import kotlinx.android.synthetic.main.item_image.view.*


class FavoriteAdapter(private var mData: List<FavoriteEntity>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_thumb = itemView.iv_thumb
        val cb_favorite = itemView.cb_favorite
        val tv_title = itemView.tv_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = mData[position]
        Glide.with(holder.itemView.context).load(entity.thumbnail)
            .transition(withCrossFade())
            .into(holder.iv_thumb)


        holder.cb_favorite.isChecked = true
        holder.cb_favorite.setOnCheckedChangeListener { compoundButton, b ->
            if (!b) {

                AppDB.getInstance(holder.itemView.context).favoriteDao().delete(entity)

                AppDB.getInstance(holder.itemView.context).imageDao().updateisFavorite(entity.isbn, false)
            }
        }

        holder.tv_title.text = entity.title
    }

    override fun getItemCount(): Int = mData.size
}