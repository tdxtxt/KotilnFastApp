package com.fastdev.ui.activity.welcome.child

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baselib.helper.CacheHelper
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.helper.getGuideVersion
import com.fastdev.helper.putGuideVersion
import com.fastdev.ui.R
import com.fastdev.ui.activity.welcome.WellcomeActivity
import com.youth.banner.adapter.BannerAdapter
import kotlinx.android.synthetic.main.fragment_guide.*

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/3
 */
class GuideFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_guide

    override fun initUi() {
        CacheHelper.putGuideVersion(GuideFragment.versionCode)

        banner.addBannerLifecycleObserver(fragmentActivity)
        banner.adapter = GuideAdapter(listOf(
                Pair("第一页", R.layout.view_wellcome_guide_1),
                Pair("第二页", R.layout.view_wellcome_guide_2),
                Pair("第三页", R.layout.view_wellcome_guide_3)))
        banner.currentItem = 0
    }

    inner class GuideAdapter(data: List<Pair<String, Int>>) : BannerAdapter<Pair<String, Int>, GuideAdapter.ViewHolder>(data) {
        override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent?.context).inflate(viewType, parent, false))
        }
        override fun onBindView(holder: ViewHolder?, item: Pair<String, Int>?, position: Int,size: Int) {
            holder?.btnNext?.setOnClickListener {
                getActivityNew<WellcomeActivity>()?.handleResult()
            }
        }

        override fun getItemViewType(position: Int): Int {
            return getData(position).second
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val btnNext: View? = view.findViewById(R.id.btn_next)
        }
    }

    companion object {
        const val versionCode = "1.0.0"

        fun isNewVersion() = GuideFragment.versionCode != CacheHelper.getGuideVersion()
    }
}