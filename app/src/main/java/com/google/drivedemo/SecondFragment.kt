package com.google.drivedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.drivedemo.driveHelper.DriveServiceHelper
import com.google.drivedemo.driveHelper.GoogleDriveFileHolder
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), DriveFilesAdapter.ILoadFile {
    private var mDriveServiceHelper: DriveServiceHelper? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var recyclerView: RecyclerView
    lateinit var mDriveFilesAdapter: DriveFilesAdapter
    lateinit var saveFile: Button
    lateinit var titleEt: EditText
    lateinit var bodyEt: EditText
    lateinit var mprogress: ProgressBar
    lateinit var myFolderId: String
    var pickedFileId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleEt = view.findViewById(R.id.title_et)
        bodyEt = view.findViewById(R.id.body_et)
        recyclerView = view.findViewById(R.id.recycler)
        mprogress = view.findViewById(R.id.progress)
        saveFile = view.findViewById<Button>(R.id.textview_second)
        linearLayoutManager = LinearLayoutManager(requireContext())
        mDriveFilesAdapter = DriveFilesAdapter(requireContext(), this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = mDriveFilesAdapter
        mDriveServiceHelper =
            DriveServiceHelper(
                DriveServiceHelper.getGoogleDriveService(
                    context,
                    GoogleSignIn.getLastSignedInAccount(context),
                    getString(R.string.app_name)
                )
            )
        mprogress.visibility = View.VISIBLE
        mDriveServiceHelper!!.createFolderIfNotExist(getString(R.string.app_name), null)
            .addOnCompleteListener {
                myFolderId = it.result?.id!!
                mprogress.visibility = View.INVISIBLE
                clearData()
            }
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        saveFile.setOnClickListener {
            if (titleEt.text.toString().isEmpty() || bodyEt.text.toString().isEmpty()) {
                Toast.makeText(context, "please add title and body!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (pickedFileId != null) {
                mprogress.visibility = View.VISIBLE
                mDriveServiceHelper!!.saveFile(
                    pickedFileId, titleEt.text.toString(),
                    bodyEt.text.toString()
                ).addOnCompleteListener {
                    mprogress.visibility = View.INVISIBLE
                    if (it.isSuccessful) {
                        clearData()
                        Toast.makeText(context, "saved!", Toast.LENGTH_LONG).show()
                    }
                }
                return@setOnClickListener
            }
            mprogress.visibility = View.VISIBLE
            mDriveServiceHelper!!.createTextFileIfNotExist(
                titleEt.text.toString(),
                bodyEt.text.toString(),
                myFolderId
            )
                .addOnCompleteListener {
                    mprogress.visibility = View.INVISIBLE
                    if (it.isSuccessful) {
                        clearData()
                    } else
                        Toast.makeText(context, "failed create file!", Toast.LENGTH_LONG).show()

                }
        }
        view.findViewById<Button>(R.id.textview_load).setOnClickListener {
            mprogress.visibility = View.VISIBLE
            mDriveServiceHelper!!.queryFiles(myFolderId).addOnCompleteListener {
                mprogress.visibility = View.INVISIBLE
                if (it.isSuccessful) {
                    mDriveFilesAdapter.assignFiles(it.result!!)
                } else
                    Toast.makeText(context, "failed loading files", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun clearData() {
        view?.findViewById<Button>(R.id.textview_load)?.performClick()
        titleEt.setText("")
        bodyEt.setText("")
        saveFile.setText("create file")
        pickedFileId = null
    }

    override fun openFile(file: GoogleDriveFileHolder) {
        pickedFileId = file.id
        mprogress.visibility = View.VISIBLE
        mDriveServiceHelper!!.readFile(file.id).addOnCompleteListener {
            mprogress.visibility = View.INVISIBLE
            if (it.isSuccessful) {
                saveFile.setText("save file")
                titleEt.setText(it.result?.first)
                bodyEt.setText(it.result?.second)
            }
        }
    }
}