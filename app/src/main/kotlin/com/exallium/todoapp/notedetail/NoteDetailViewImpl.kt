package com.exallium.todoapp.notedetail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.bluelinelabs.conductor.RouterTransaction
import com.exallium.todoapp.R
import com.exallium.todoapp.allnotes.AllNotesViewImpl
import com.exallium.todoapp.app.TodoApp
import com.exallium.todoapp.editnote.EditNoteViewImpl
import com.exallium.todoapp.entities.Note
import com.exallium.todoapp.mvp.BaseViewImpl
import com.jakewharton.rxbinding.view.clicks
import rx.Observable
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Conductor Controller displaying a single note
 */
class NoteDetailViewImpl(val bundle: Bundle) : BaseViewImpl<NoteDetailView, NoteDetailPresenter, NoteDetailViewImpl,
        NoteDetailComponent>(bundle), NoteDetailView {

    private val STATE_NOTE_TITLE = "noteTitle"
    private val STATE_NOTE_BODY = "noteBody"
    private val STATE_NOTE_CREATED = "noteCreated"
    private val STATE_NOTE_UPDATED = "noteUpdated"

    @BindView(R.id.note_title)
    lateinit var noteTitleTextView: TextView

    @BindView(R.id.note_body)
    lateinit var noteBodyTextView: TextView

    @BindView(R.id.note_date_created)
    lateinit var noteDateCreated: TextView

    @BindView(R.id.note_date_updated)
    lateinit var noteDateUpdated: TextView

    @BindView(R.id.note_delete_button)
    lateinit var deleteNote: View

    @BindView(R.id.note_edit_button)
    lateinit var editNote: View

    override fun getComponent(): NoteDetailComponent = DaggerNoteDetailComponent.builder()
            .todoAppComponent(TodoApp.component)
            .noteDetailModule(NoteDetailModule(this))
            .build()

    override fun getLayoutId(): Int = R.layout.note_detail_view

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putString(STATE_NOTE_TITLE, noteTitleTextView.text.toString())
        outState.putString(STATE_NOTE_BODY, noteBodyTextView.text.toString())
        outState.putString(STATE_NOTE_CREATED, noteDateCreated.text.toString())
        outState.putString(STATE_NOTE_UPDATED, noteDateUpdated.text.toString())
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        if (savedViewState != null) {
            noteTitleTextView.text = savedViewState.getString(STATE_NOTE_TITLE)
            noteBodyTextView.text = savedViewState.getString(STATE_NOTE_BODY)
            savedViewState.getString(STATE_NOTE_CREATED)?.let { noteDateCreated.text = it }
            savedViewState.getString(STATE_NOTE_UPDATED)?.let { noteDateUpdated.text = it }
        }
    }

    override fun setNoteData(note: Note) {
        noteTitleTextView.text = note.title
        noteBodyTextView.text = note.body
        noteDateCreated.setDate(note.created)
        noteDateUpdated.setDate(note.updated)
    }

    override fun showUnableToLoadNoteDetailError() {
        displaySnackbar(R.string.unable_to_load_note_error)
    }

    override fun showNoteDeletedMessage() {
        displaySnackbar(R.string.note_deleted_message)
    }

    override fun deleteNoteClicks(): Observable<Unit> = deleteNote.clicks()

    override fun editNoteClicks(): Observable<Unit> = editNote.clicks()

    override fun showAllNotes(args: Bundle) {
        router.pushController(RouterTransaction.with(AllNotesViewImpl(args)))
    }

    override fun showEditNote(args: Bundle) {
        router.pushController(RouterTransaction.with(EditNoteViewImpl(args)))
    }

    private fun TextView.setDate(valueInMillis: Long) {
        if (valueInMillis != 0L) {
            this.text = SimpleDateFormat().format(Date(valueInMillis))
        }
    }
}
