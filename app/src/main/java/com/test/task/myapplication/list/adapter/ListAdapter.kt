package com.test.task.myapplication.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.task.myapplication.R
import com.test.task.myapplication._models.ItemModel
import com.test.task.myapplication.list.IImageListViewModel
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(private val viewModel: IImageListViewModel) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var dataset = listOf<ItemModel>()

    class MyViewHolder(view: ViewGroup, onClick: (itemModel: ItemModel) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val title: TextView = view.list_title
        val image: ImageView = view.list_image
        lateinit var itemModel: ItemModel
        var itemIndex: Int = -1
        init {
            view.setOnClickListener {
                if (itemIndex >= 0) onClick(itemModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false) as ViewGroup
        return MyViewHolder(view) {
            viewModel.selectItem(it)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataset[position]
        holder.title.text = item.title
        holder.itemIndex = position
        holder.itemModel = item
        viewModel.loadImageTo(holder.image, item.imageUrl)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    init {
        viewModel.subscribeOnChange { data ->
            dataset = data
            notifyDataSetChanged()
        }
    }
}