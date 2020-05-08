package com.proj.notes_board.ui.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.proj.notes_board.R
import com.proj.notes_board.di.Injectable
import com.proj.notes_board.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_notes.*
import javax.inject.Inject

class NotesFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    private var deleteBtn: MenuItem? = null

    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notes_context, menu)
        menu.findItem(R.id.doneBtn).isVisible = false
        deleteBtn = menu.findItem(R.id.deleteBtn)
        viewModel.selectedCount.value?.let {
            deleteBtn?.isVisible = it > 0
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteBtn -> {
                viewModel.deleteSelected()
                showUndoDeletion()
            }
            android.R.id.home -> {
                viewModel.clearSelection()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showUndoDeletion() {
        Snackbar.make(
            createNoteFAB,
            getString(R.string.n_notes_deleted, viewModel.selectedCount.value),
            Snackbar.LENGTH_LONG
        ).apply {
            addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    viewModel.realDelete()
                }
            })
            anchorView = createNoteFAB
            setActionTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            setAction(getString(R.string.undo_deletion)) {
                viewModel.undoDeletion()
            }
        }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotesAdapter(viewModel)
        initToolbar()
        initRecycler()
        initListeners()
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.toolbar_notes)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
    }

    private fun initRecycler() {
        notesRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        notesRecycler.adapter = adapter

        val helper = ItemTouchHelper(
            NotesSwipeCallback(
                viewModel,
                requireContext()
            )
        )
        helper.attachToRecyclerView(notesRecycler)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notesRecycler.scrollToPosition(positionStart)
            }
        })
    }

    private fun initListeners() {
        initViewModelListeners()
        initBtnListeners()
    }

    private fun initViewModelListeners() {
        viewModel.notes.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.selectedCount.observe(viewLifecycleOwner, Observer { count ->
            if (count == 0)
                toolbar.setTitle(R.string.toolbar_notes)
            else
                toolbar.title = getString(R.string.toolbar_n_selected, count)

            (activity as AppCompatActivity?)?.supportActionBar
                ?.setDisplayHomeAsUpEnabled(count > 0)
            deleteBtn?.isVisible = count > 0
        })
    }

    private fun initBtnListeners() {
        createNoteFAB.setOnClickListener {
            viewModel.onCreateNoteClick()
        }
    }
}