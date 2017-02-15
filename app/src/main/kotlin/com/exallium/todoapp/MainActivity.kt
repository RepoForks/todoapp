package com.exallium.todoapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.TextSwitcher
import butterknife.BindView
import butterknife.ButterKnife
import com.bluelinelabs.conductor.*
import com.exallium.todoapp.allnotes.AllNotesViewImpl
import com.exallium.todoapp.app.TodoApp
import com.exallium.todoapp.screenbundle.ScreenBundleHelper
import javax.inject.Inject

/**
 * Single Activity for Application
 */
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var screenBundleHelper: ScreenBundleHelper

    @BindView(R.id.conductor_container)
    lateinit var container: ViewGroup

    @BindView(R.id.main_activity_toolbar_title)
    lateinit var toolbarTitle: TextSwitcher

    lateinit var router: Router

    val changeListener = object : ControllerChangeHandler.ControllerChangeListener {
        override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
        }

        override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
            val title = screenBundleHelper.getTitle(to?.args)
            toolbarTitle.setText(title)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        ButterKnife.bind(this)
        TodoApp.component.inject(this)
        router = Conductor.attachRouter(this, container, savedInstanceState)
        router.addChangeListener(changeListener)
        toolbarTitle.setText(getString(R.string.app_name))
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(AllNotesViewImpl(Bundle())))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}