package com.fastdev.ui.activity.task

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.baselib.helper.CommonCacheHelper
import com.baselib.helper.DialogHelper
import com.baselib.helper.LogA
import com.baselib.helper.ToastHelper
import com.baselib.rx.event.RxBus
import com.baselib.ui.activity.CommToolBarActivity
import com.baselib.ui.fragment.BaseFragment
import com.baselib.ui.view.other.TextSpanController
import com.fastdev.core.UHFSdk
import com.fastdev.data.event.TaskEventCode
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.response.SourceBean
import com.fastdev.helper.clearLogin
import com.fastdev.helper.getAccountNo
import com.fastdev.helper.isLogin
import com.fastdev.ui.R
import com.fastdev.ui.activity.login.LoginMainActivity
import com.fastdev.ui.activity.task.fragment.TaskListFragment
import com.fastdev.ui.activity.task.viewmodel.TaskListViewModel
import com.fastdev.ui.adapter.BaseFragmentPagerAdapter
import com.fastdev.ui.dialog.ConfigDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_task_list.*
import org.litepal.LitePal
import org.litepal.LitePalDB
import javax.inject.Inject

@AndroidEntryPoint
class TaskListActivity : CommToolBarActivity() {
    @Inject
    lateinit var dbRepository: DbApiRepository
    lateinit var viewModel: TaskListViewModel

    override fun getLayoutResId() = R.layout.activity_task_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = TaskListViewModel.get(this)
        if(TextUtils.isEmpty(CommonCacheHelper.getAccountNo())){
            DialogHelper.showCommDialog(fragmentActivity, "警告", "未获取到登录信息", {
                menuText = "退出"
            }){
                menuText = "重新登录"
                onClick { rootView, any ->
                    CommonCacheHelper.clearLogin()
                    LoginMainActivity.open(fragmentActivity, {
                        recreate()
                    }){
                        finish()
                    }
                }
            }
        }else{
            //初始化数据库
            val db = LitePalDB.fromDefault(CommonCacheHelper.getAccountNo())
            LitePal.use(db)
        }
    }

    override fun initUi() {
        setTitleBar("资产盘点"){
            menuText = TextSpanController().pushColorSpan(Color.parseColor("#666666")).append("参数设置").popSpan().build()
            onClick { rootView, any ->
                ConfigDialog(fragmentActivity).show()
            }
        }
        val fragments: MutableList<Pair<String, Fragment>> = mutableListOf()

        fragments.add(Pair("进行中", TaskListFragment.getIngInstance()))
        fragments.add(Pair("已结束", TaskListFragment.getEndInstance()))

        viewPager.setAdapter(BaseFragmentPagerAdapter(fragments, supportFragmentManager))
        tabLayout.setViewPager(viewPager)

        viewPager.setCurrentItem(0)
        tabLayout.onPageSelected(0)

        val disposable =
                RxBus.listen(TaskEventCode.COMMIT_SUCCESS::class.java)
                        .compose(bindLifecycle())
                        .subscribe {
                            ToastHelper.showToast("删除任务${it.getData()?.toString()}")
                            deleteCacheByTask(it.getData()?.toString())
                            viewModel.refreshGlobal.postValue(true)
                        }
    }

    override fun clickTitleBarBack(): Boolean {
        DialogHelper.showCommDialog(fragmentActivity, "温馨提示", "确定退出当前登录账户吗？", {
            menuText = "取消"
        }){
            menuText = "确定"
            onClick { rootView, any ->
                CommonCacheHelper.clearLogin()
                LoginMainActivity.open(fragmentActivity, {
                    recreate()
                }){
                    finish()
                }
            }
        }
        return false
    }

    fun deleteCacheByTask(taskId: String?){
        val disposable =
                dbRepository.deleteCacheByTask(taskId)
                        .compose(bindProgress())
                        .subscribe {

                        }
    }

    override fun onPause() {
        super.onPause()
        UHFSdk.pause()
    }

    companion object{
        fun open(activity: Activity?){
            activity?.startActivity(Intent(activity, TaskListActivity::class.java))
        }
    }
}
