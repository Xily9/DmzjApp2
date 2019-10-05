package com.xily.dmzj2.base

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * 一个使用 Data Binding 和 DiffUtil 的 Base Adapter
 *
 * @param <T> List容器的类型
 * @param <V> ViewDataBinding的类型
 */
abstract class BaseListAdapter<T, V : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseListAdapter.BaseViewHolder<V>>(
    diffCallback
) {
    lateinit var context: Context
        private set
    private var onItemClickListener: ((position: Int) -> Unit?)? = null
    private var onItemLongClickListener: ((position: Int) -> Boolean?)? = null

    fun setOnItemClickListener(onItemClickListener: (position: Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (position: Int) -> Boolean) {
        this.onItemLongClickListener = onItemLongClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        context = parent.context
        val binding = DataBindingUtil.inflate<V>(
            LayoutInflater.from(parent.context),
            getLayoutId(),
            parent,
            false
        )
        val holder=BaseViewHolder(binding)
        onItemClickListener?.let {
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(holder.adapterPosition)
            }
        }
        onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(holder.adapterPosition) ?: false
            }
        }
        return holder
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: V, item: T)
    class BaseViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
        RecyclerView.ViewHolder(binding.root)
}
