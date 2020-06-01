package com.easysales.letsplay.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_options.*
import com.easysales.letsplay.R
import com.easysales.letsplay.presentation.core.ClickEvent
import java.io.Serializable

class OptionsDialog : DialogFragment() {

    private val btn1Subject : PublishSubject<ClickEvent> = PublishSubject.create()
    private val btn2Subject : PublishSubject<ClickEvent> = PublishSubject.create()
    private val btn3Subject : PublishSubject<ClickEvent> = PublishSubject.create()

    public val btn1Clicks : Observable<ClickEvent> = btn1Subject
    public val btn2Clicks : Observable<ClickEvent> = btn2Subject
    public val btn3Clicks : Observable<ClickEvent> = btn3Subject

    companion object {
        private const val ARGS = "ARGS"

        fun newInstance(params: OptionsDialogParams) : OptionsDialog {
            val args = Bundle()
            args.putSerializable(ARGS, params)
            val fragment = OptionsDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dialog_options, container, false)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val params = arguments!!.getSerializable(ARGS) as OptionsDialogParams
        init(params)
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    private fun init(params: OptionsDialogParams) {
        fun initButton(btn: Button, params: OptionsDialogButtonParams) {
            btn.text = params.title
            btn.visibility = if(params.visible) View.VISIBLE else View.GONE
        }

        btn1.setOnClickListener { btn1Subject.onNext(object: ClickEvent {}) }
        btn2.setOnClickListener { btn2Subject.onNext(object: ClickEvent {}) }
        btn3.setOnClickListener { btn3Subject.onNext(object: ClickEvent {}) }
        vClose.setOnClickListener { dismiss() }

        tvTitle.text = params.title
        tvQuestion.text = params.question

        initButton(btn1, params.btn1Params)
        initButton(btn2, params.btn2Params)
        initButton(btn3, params.btn3Params)
    }

    public data class OptionsDialogParams(
        val title: String,
        val question: String,
        val btn1Params: OptionsDialogButtonParams,
        val btn2Params: OptionsDialogButtonParams = OptionsDialogButtonParams("", false),
        val btn3Params: OptionsDialogButtonParams = OptionsDialogButtonParams("", false)
        ) : Serializable

    data class OptionsDialogButtonParams(
        val title: String,
        val visible: Boolean = true
    ) : Serializable
}