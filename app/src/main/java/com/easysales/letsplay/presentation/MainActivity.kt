package com.easysales.letsplay.presentation

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.easysales.letsplay.R
import com.easysales.letsplay.domain.CommonService
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.article.Args
import com.easysales.letsplay.presentation.article.ArticleFragment
import com.easysales.letsplay.presentation.articlelist.ArticleListFragment
import com.easysales.letsplay.presentation.core.BaseMvpActivity
import com.easysales.letsplay.presentation.tutorial.HelpFragment
import com.easysales.letsplay.presentation.tutorial.TutorialActivity
import com.easysales.letsplay.utils.LogUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*
import javax.inject.Inject

@RuntimePermissions
class MainActivity : BaseMvpActivity<MainPresenter, MainContract.View>(), MainContract.View {

    private val disposables = CompositeDisposable()
    private val navigationSubject = PublishSubject.create<NavigationItem>()

    @Inject lateinit var commonService: CommonService
    @Inject lateinit var mainPresenter: MainPresenter


    override fun getLayoutId() = R.layout.activity_main
    override fun getPresenter(): MainPresenter = mainPresenter
    override var navigationClicks: Observable<NavigationItem> = navigationSubject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getDI().addTutorialComponent()

        val navView: BottomNavigationView = findViewById(R.id.navView)
        navView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_games -> {
                    navigationSubject.onNext(NavigationItem.GAMES)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorites -> {
                    navigationSubject.onNext(NavigationItem.FAVORITES)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_help -> {
                    navigationSubject.onNext(NavigationItem.HELP)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_logs -> {
                    navigationSubject.onNext(NavigationItem.LOGS)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isFinishing) {
            disposables.clear()
            App.getDI().clearTutorialComponent()
        }
    }

    override fun initActivity(savedInstantState: Bundle?) {
        App.getDI().appComponent.inject(this)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        getPresenter().start(getBaseView())
        onInitWithPermissionCheck()
    }

    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onInit() {
    }

    override fun showTutorialActivity() {
        startActivity(Intent(this, TutorialActivity::class.java))
        finish()
    }

    override fun showFavoriteView() {
        TODO("Not yet implemented")
    }

    override fun showHelpView() = showFragment(HelpFragment(), false)
    override fun showArticlesView() = showFragment(ArticleListFragment(), false)
    override fun showArticleView(articleId: String) = showFragment(ArticleFragment.newInstance(Args(articleId)), true)
    override fun sendLogs() {
        LogUtils.sendLogs(this, LogUtils.EmailSettings("", "", "", "Отправить логи"))
    }

    //region permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionsDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI or disabling certain functionality
        showToast(R.string.permissions_denied)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForContacts(request: PermissionRequest) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        val dialog = showDialog(OptionsDialog.OptionsDialogParams(
            getString(R.string.permissions),
            getString(R.string.permissions_explain),
            OptionsDialog.OptionsDialogButtonParams(getString(R.string.button_ok), true),
            OptionsDialog.OptionsDialogButtonParams(getString(R.string.button_denied), true)
        ))

        disposables.addAll(
            dialog.btn1Clicks.subscribe(
                {
                    request.proceed()
                    dialog.dismiss()
                },
                { LogUtils.eFile(LogUtils.LOG_UI, it.message!!, it) }
            ),
            dialog.btn2Clicks.subscribe(
                {
                    request.cancel()
                    dialog.dismiss()
                },
                { LogUtils.eFile(LogUtils.LOG_UI, it.message!!, it) }
            )
        )
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionsNeverAskAgain() {
        showToast(R.string.permissions_denied)
    }
    //endregion
}
