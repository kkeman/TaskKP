package com.service.codingtest.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.service.codingtest.R
import com.service.codingtest.db.AppDB
import com.service.codingtest.model.response.ItemsEntity
import com.service.codingtest.view.adapters.FavoriteAdapter
import com.service.codingtest.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.frag_detail.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class DetailFragment : Fragment() {

    private val mTAG = DetailFragment::class.java.name

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_detail, null);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val data = bundle.getParcelable<ItemsEntity>("bundleKey")!!

            Glide.with(requireContext()).load(data.thumbnail)
                .into(iv_url)

            tv_title.text = data.title

            if(data.sale_price > 0) {
                val dFormatter = DecimalFormat("###,###,###")
                tv_sale_price.text = dFormatter.format(data.sale_price)
            }

            tv_publisher.text = data.publisher

            tv_contents.text = data.contents

            if(data.datetime.isNotEmpty()) {
                val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                val result1: Date = df1.parse(data.datetime) as Date
                val rewardString = format.format(result1)
                tv_datetime.text = rewardString
            }

            cb_favorite.isChecked = data.isFavorite

            cb_favorite.setOnCheckedChangeListener { compoundButton, b ->
                data.isFavorite = b

                lifecycleScope.launchWhenCreated {
                    AppDB.getInstance(requireContext()).imageDao().update(data)

                    viewModel.select(data, bundle.getInt("position"))
                }
            }
        }
    }
}