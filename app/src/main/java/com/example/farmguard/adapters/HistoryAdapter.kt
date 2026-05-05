package com.example.farmguard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.farmguard.databinding.ItemReportBinding
import com.example.farmguard.models.Report
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val reports: List<Report>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]
        holder.binding.tvItemType.text = report.farmType
        holder.binding.tvItemDiagnosis.text = report.diagnosisResult
        
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.binding.tvItemDate.text = sdf.format(report.timestamp.toDate())

        if (report.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(report.imageUrl)
                .into(holder.binding.ivReport)
        }
    }

    override fun getItemCount() = reports.size
}