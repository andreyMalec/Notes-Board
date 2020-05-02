package com.proj.notes_board.ui.createNote

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.proj.notes_board.R
import com.proj.notes_board.di.Injectable
import com.proj.notes_board.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_create_note.*
import javax.inject.Inject

class CreateNoteFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    private var oldToolbarColor = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notes_context, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.createBtn)
            createNote()

        return super.onOptionsItemSelected(item)
    }

    private fun createNote() {
        val title = titleEditText.text?.toString()
        val description = descriptionEditText.text?.toString()
        val badgeColor = oldToolbarColor

        viewModel.checkAndCreateNote(title, description, badgeColor)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initViewModelListeners()
        initColorClickListener()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        oldToolbarColor = ContextCompat.getColor(
            requireContext(),
            R.color.colorPrimary
        )
    }

    private fun initViewModelListeners() {
        viewModel.createResultError.observe(viewLifecycleOwner, Observer { error ->
            if (error)
                showInputError()
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
                    animateToolbarColorChange(newColor)
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