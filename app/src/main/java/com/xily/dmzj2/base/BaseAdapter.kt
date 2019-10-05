package com.xily.dmzj2.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * RecycleView通用基类
 * 极度简化了RecycleView的使用
 *
 * @param <T> 子类的ViewHolder
 * @param <U> List容器的类型
 * @author Xily
 */

abstract class BaseAdapter<T : BaseAdapter.BaseViewHolder, U>(var list: List<U>?) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {
    lateinit var context: Context
        private set
    private var onItemClickListener: ((position: Int) -> Unit?)? = null
    private var onItemLongClickListener: ((position: Int) -> Boolean?)? = null
    var headerView: View? = null
        set(value) {
            field = value
            notifyItemInserted(0)
        }
    var footerView: View? = null
        set(value) {
            field = value
            notifyItemInserted(itemCount - 1)
        }

    val TYPE_HEADER = 0
    val TYPE_FOOTER = 1
    val TYPE_NORMAL = 2

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun setOnItemClickListener(onItemClickListener: (position: Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (position: Int) -> Boolean) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun getItemCount(): Int {
        var size = list?.size ?: 0
        if (headerView == null && footerView == null)
            return size
        if (headerView != null)
            size++
        if (footerView != null)
            size++
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (headerView == null && footerView == null) {
            return TYPE_NORMAL
        }
        if (headerView != null && position == 0) {
            return TYPE_HEADER
        }
        if (footerView != null && position == itemCount - 1) {
            return TYPE_FOOTER
        }
        return TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        headerView?.let {
            if (viewType == TYPE_HEADER)
                return BaseViewHolder(it)
        }
        footerView?.let {
            if (viewType == TYPE_FOOTER)
                return BaseViewHolder(it)
        }
        val holder = createViewHolder(LayoutInflater.from(context).inflate(getLayoutId(), parent, false))
        onItemClickListener?.let {
            holder.itemView.setOnClickListener {
                val position = if (headerView != null)
                    holder.adapterPosition - 1
                else
                    holder.adapterPosition
                onItemClickListener?.invoke(position)
            }
        }
        onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                val position = if (headerView != null)
                    holder.adapterPosition - 1
                else
                    holder.adapterPosition
                onItemLongClickListener?.invoke(position) ?: false
            }
        }
        return holder
    }

    /**
     * 利用反射获取子类的ViewHolder实例
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewHolder(view: View): BaseViewHolder {
        val type = javaClass.genericSuperclass//获取父类Type
        if (type is ParameterizedType) {//判断是不是泛型参数列表
            val types = type.actualTypeArguments//获取泛型参数列表
            val clazz = types[0] as Class<*>//获取第一个Class
            try {
                val cons: Constructor<*>
                return if (clazz.isMemberClass && !Modifier.isStatic(clazz.modifiers)) {//是内部类且不是静态类
                    cons = clazz.getDeclaredConstructor(javaClass, View::class.java)//获取内部类的构造函数,要注意必须要传入外部类的Class
                    cons.isAccessible = true//设置为可访问
                    cons.newInstance(this, view) as T//实例化
                } else {
                    cons = clazz.getDeclaredConstructor(View::class.java)
                    cons.isAccessible = true
                    cons.newInstance(view) as T
                }
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
        return BaseViewHolder(view)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (headerView != null)
                onBindViewHolder(holder as T, position - 1, list!![position - 1])
            else
                onBindViewHolder(holder as T, position, list!![position])
        }
    }

    protected abstract fun onBindViewHolder(holder: T, position: Int, value: U)

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
