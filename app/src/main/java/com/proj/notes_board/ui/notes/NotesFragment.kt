package com.proj.notes_board.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.proj.notes_board.R
import com.proj.notes_board.di.Injectable
import com.proj.notes_board.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_notes.*
import javax.inject.Inject

class NotesFragment : Fragment(), Injectable, NotesAdapter.NoteAction {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    private val adapter = NotesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        initListeners()
    }

    private fun initRecycler() {
        notesRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        notesRecycler.adapter = adapter
    }

    private fun initListeners() {
        initViewModelListeners()
        initBtnListeners()
    }

    private fun initViewModelListeners() {
        viewModel.notes.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initBtnListeners() {
        createNoteFAB.setOnClickListener {
            viewModel.onCreateNoteClick()
        }

    }
}