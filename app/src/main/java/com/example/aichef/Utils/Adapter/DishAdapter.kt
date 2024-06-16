package com.example.aichef.Utils.Adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aichef.R
import com.example.aichef.Utils.Model.DishData


class DishAdapter (val dishList: MutableList<DishData>  = ArrayList<DishData>()): RecyclerView.Adapter<DishAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.each_dish_item,
            parent,false
        )
        return MyViewHolder(itemView)

    }
    interface OnItemDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    var onDeleteClickListener: OnItemDeleteClickListener? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.trash.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(position)
        }
        val currentitem = dishList[position]
        holder.dateD.text = currentitem.date
        holder.typeD.text = currentitem.selectedDish
        holder.timeD.text = currentitem.selectedTime
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDishList(dishList : List<DishData>){
        this.dishList.clear()
        this.dishList.addAll(dishList)
        notifyDataSetChanged()
    }

    class  MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val trash : ImageButton = itemView.findViewById(R.id.deleteBtnCard)
        val dateD : TextView = itemView.findViewById(R.id.tvDate)
        val typeD : TextView = itemView.findViewById(R.id.tvDishType)
        val timeD : TextView = itemView.findViewById(R.id.tvTime)
    }
}
