package com.proj.notes_board.ui.manageNote

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.proj.notes_board.R
import com.proj.notes_board.di.Injectable
import kotlinx.android.synthetic.main.fragment_manage_note.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.math.sqrt

@ExperimentalCoroutinesApi
class ManageNoteFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ManageNoteViewModel by viewModels {
        viewModelFactory
    }

    private var oldToolbarColor = 0
    private var deleteBtn: MenuItem? = null

    private val layoutListener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            v?.removeOnLayoutChangeListener(this)

            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            val w = dm.widthPixels
            val h = dm.heightPixels
            val r = sqrt((w * w + h * h).toFloat())

            val animator = ViewAnimationUtils.createCircularReveal(
                view,
                w,
                h,
                0F,
                r
            )
            animator.duration = 400
            animator.start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_manage_note, container, false)

        v.addOnLayoutChangeListener(layoutListener)

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notes_context, menu)
        deleteBtn = menu.findItem(R.id.deleteBtn)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.doneBtn -> createNote()
            R.id.deleteBtn -> {
                viewModel.deleteNote()
                showUndoDeletion()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createNote() {
        val title = titleEditText.text?.toString()
        val description = descriptionEditText.text?.toString()

        viewModel.checkAndManageNote(title, description)
    }

    private fun showUndoDeletion() {
        Snackbar.make(
            mainLayout,
            getString(R.string.note_deleted),
            Snackbar.LENGTH_LONG
        ).apply {
            addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
                        viewModel.realDelete()
                }
            })
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

        viewModel.noteId.value = arguments?.getLong("noteId") ?: -1

        initToolbar()
        initViewModelListeners()
        initColorClickListener()
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.toolbar_create_note)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

//        oldToolbarColor = ContextCompat.getColor(
//            requireContext(),
//            android.R.color.transparent
//        )
//        viewModel.noteColor.value = oldToolbarColor
    }

    private fun initViewModelListeners() {
        viewModel.isManageError.observe(viewLifecycleOwner, Observer { error ->
            if (error)
                showInputError()
        })

        viewModel.noteColor.observe(viewLifecycleOwner, Observer { color ->
            Handler().postDelayed({
                animateToolbarColorChange(color)
            }, 200)
        })

        viewModel.note.observe(viewLifecycleOwner, Observer { note ->
            if (note != null) {
                Handler().postDelayed({
                    titleEditText.setText(note.title)
                    descriptionEditText.setText(note.description)

                    val color = viewModel.noteColor.value
                    if (color != null && color != 0)
                        animateToolbarColorChange(color)
                    else
                        animateToolbarColorChange(note.noteColor)

                    toolbar.setTitle(R.string.toolbar_edit_note)
                    deleteBtn?.isVisible = true
                }, 200)
            }
        })
    }

    private fun showInputError() {
        Snackbar.make(mainLayout, getString(R.string.input_error), Snackbar.LENGTH_LONG)
            .setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorError
                )
            )
            .show()
    }

    private fun initColorClickListener() {
        for (child in colorLayout1.children + colorLayout2.children) {
            child.setOnClickListener {
                if (it is CardView) {
                    val newColor = it.cardBackgroundColor.defaultColor
                    viewModel.noteColor.value = newColor
                }
            }
        }
    }

    private fun animateToolbarColorChange(newColor: Int) {
        if (newColor == oldToolbarColor) return

        val animator = ViewAnimationUtils.createCircularReveal(
            reveal,
            toolbar.width / 2,
            toolbar.height / 2,
            0F,
            toolbar.width / 2F
        )

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                reveal.setBackgroundColor(newColor)
                revealBackground.setBackgroundColor(oldToolbarColor)
            }

            override fun onAnimationEnd(animation: Animator?) {
                oldToolbarColor = newColor
            }
        })

        animator.duration = 200
        animator.start()
    }
}