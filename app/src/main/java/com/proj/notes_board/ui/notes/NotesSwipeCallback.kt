package com.proj.notes_board.ui.notes

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.proj.notes_board.R
import kotlin.math.abs

class NotesSwipeCallback(private val vm: NoteAction, private val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {

    private val editIcon = ContextCompat.getDrawable(context, R.drawable.edit_icon)
    private val background = ColorDrawable(
        ContextCompat.getColor(
            context,
            R.color.colorAccent
        )
    )

    private var vibrate = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        vm.onNoteSwipe(viewHolder.adapterPosition)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is NotesAdapter.NoteItemViewHolder &&
            viewHolder.binding?.selectedMarker?.visibility == View.VISIBLE
        )
            0
        else
            super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val a = 0F
        val b = c.width / 3F
        if (abs(dX) in a..b)
            vibrate = true

        if (abs(dX) >= c.width / 2F && vibrate)
            context.getSystemService(Context.VIBRATOR_SERVICE)?.let {
                it as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    it.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                else
                    it.vibrate(30)

                vibrate = false
            }

        if (editIcon == null) return

        val itemView = viewHolder.itemView
        val top = itemView.top + (itemView.height - editIcon.intrinsicHeight) / 2
        val left =
            itemView.width - editIcon.intrinsicWidth - (itemView.height - editIcon.intrinsicHeight) / 2
        val right = left + editIcon.intrinsicHeight
        val bottom = top + editIcon.intrinsicHeight

        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        editIcon.setBounds(left, top, right, bottom)

        background.draw(c)
        editIcon.draw(c)
    }
}