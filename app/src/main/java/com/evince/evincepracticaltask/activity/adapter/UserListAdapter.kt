package com.evince.evincepracticaltask.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evince.evincepracticaltask.activity.model.UserModel
import com.evince.evincepracticaltask.databinding.AdapterUserListBinding

class UserListAdapter(var context : Context,var list : List<UserModel.DataX>,
var onItemClick: OnItemClick) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    class ViewHolder(var adapterUserListBinding: AdapterUserListBinding) :
        RecyclerView.ViewHolder(adapterUserListBinding.root){

            fun bind(context: Context,position: Int, userModel: UserModel.DataX,onItemClick: OnItemClick){
                adapterUserListBinding.apply {
                    tvUserName.text = userModel.first_name +""+userModel.last_name
                    tvEmail.text = userModel.email

                    Glide.with(context).load(userModel.avatar).into(ivUserProfile)

                    itemView.setOnClickListener {
                        onItemClick.onItemClick(userModel)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterUserListBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context,position,list.get(position),onItemClick)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClick{
        fun onItemClick(userModel: UserModel.DataX)
    }
}