package com.google.drivedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.drivedemo.driveHelper.GoogleDriveFileHolder
import kotlinx.android.synthetic.main.item_drive_file.view.*

/**
 * @author Abdullah Ayman on 20/09/2020
 */
class DriveFilesAdapter(val context: Context, val mILoadFile: ILoadFile) :
    RecyclerView.Adapter<DriveFilesAdapter.ViewHolder>() {
    var files: ArrayList<GoogleDriveFileHolder> = arrayListOf()
    fun assignFiles(items: List<GoogleDriveFileHolder>) {
        files.clear()
        files.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_drive_file, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return files.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val fileName = view.fileName_tv
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fileName?.text = files[position].name
        holder.fileName.setOnClickListener {
            mILoadFile.openFile(files[position])
        }
    }

    interface ILoadFile {
        fun openFile(file: GoogleDriveFileHolder)
    }
}