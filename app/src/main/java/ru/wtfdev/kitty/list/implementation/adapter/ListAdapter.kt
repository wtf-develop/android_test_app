package ru.wtfdev.kitty.list.implementation.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty.databinding.ListItemBinding
import ru.wtfdev.kitty.list.IImageListViewModel
import java.net.URL

class ListAdapter(private val viewModel: IImageListViewModel) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var dataset = listOf<ItemModel>()

    companion object {
        val transparent = 0.4f
    }

    class MyViewHolder(
        view: ListItemBinding,
        onClick: (itemModel: ItemModel) -> Unit,
        onLike: (id: Int) -> Boolean,
        onDislike: (id: Int) -> Boolean,
        onAbuse: (id: Int) -> Boolean
    ) :
        RecyclerView.ViewHolder(view.root) {
        val title: TextView = view.listTitle
        val image: ImageView = view.listImage
        val like = view.like
        val dislike = view.dislike
        val abuse = view.abuse
        val domain = view.domain
        lateinit var itemModel: ItemModel
        var itemIndex: Int = -1

        init {
            view.root.setOnClickListener {
                if (itemIndex >= 0) onClick(itemModel)
            }
            like.setOnClickListener {
                if (onLike(itemModel.id)) {
                    like.alpha = 1.0f
                } else {
                    like.alpha = transparent
                }
            }
            dislike.setOnClickListener {
                if (onDislike(itemModel.id)) {
                    dislike.alpha = 1.0f
                } else {
                    dislike.alpha = transparent
                }
            }
            abuse.setOnClickListener {
                if (onAbuse(itemModel.id)) {
                    abuse.alpha = 1.0f
                } else {
                    abuse.alpha = transparent
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding, {
            viewModel.selectItem(it)
        }, {
            viewModel.like(it)
        }, {
            viewModel.dislike(it)
        }, {
            viewModel.abuse(it)
        })
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataset[position]
        holder.title.text = item.title
        holder.itemIndex = position
        holder.itemModel = item
        viewModel.loadImageTo(holder.image, item.link)
        if (viewModel.checkLike(item.id)) {
            holder.like.alpha = 1.0f
        } else {
            holder.like.alpha = transparent
        }
        if (viewModel.checkDislike(item.id)) {
            holder.dislike.alpha = 1.0f
        } else {
            holder.dislike.alpha = transparent
        }
        if (viewModel.checkAbuse(item.id)) {
            holder.abuse.alpha = 1.0f
        } else {
            holder.abuse.alpha = transparent
        }

        if (item.dislakes > item.likes && item.dislakes > item.abuse) {
            setBGcolor(holder.title, R.color.base_main)
        } else if (item.abuse > item.likes && item.abuse > item.dislakes) {
            setBGcolor(holder.title, R.color.base_secondary2)
        } else {
            setBGcolor(holder.title, R.color.base_secondary1)
        }
        var str = ""
        try {
            val murl = URL(item.link)
            str = murl.host
        } catch (_: Exception) {
        }
        holder.domain.text = str

    }

    private fun setBGcolor(view: View, resid: Int) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.setBackgroundColor(view.context.getColor(resid))
        } else {
            view.setBackgroundColor(view.context.resources.getColor(resid))
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun setData(data: List<ItemModel>) {
        dataset = data
        notifyDataSetChanged()
    }
}