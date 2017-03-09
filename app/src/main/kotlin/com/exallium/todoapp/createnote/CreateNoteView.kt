package com.exallium.todoapp.createnote

import android.os.Bundle
import com.exallium.todoapp.mvp.BaseView
import rx.Observable

/**
 * View interface for Controller displaying Create Note Screen.
 */
interface CreateNoteView : BaseView {
    fun getNoteTitle(): String
    fun getNoteBody(): String
    fun showNoteDetail(args: Bundle)
    fun showAllNotes(args: Bundle)
    fun showUnableToSaveNoteError()
    fun cancelCreateNoteClicks(): Observable<Unit>
    fun saveNoteClicks(): Observable<Unit>
}
