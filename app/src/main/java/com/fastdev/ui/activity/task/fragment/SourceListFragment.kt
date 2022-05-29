package com.fastdev.ui.activity.task.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.baselib.helper.DialogHelper
import com.baselib.ui.mvp.view.fragment.BaseMvpFragment
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.presenter.SourceListPresenter
import com.fastdev.ui.activity.task.viewmodel.Option
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel
import com.fastdev.ui.adapter.BaseQuickLoadMoreAdapter
import com.fastdev.ui.dialog.RemarkDialog
import com.fastdev.ui.view.HookCheckBox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_source_list.*
import javax.inject.Inject

@AndroidEntryPoint
class SourceListFragment : BaseMvpFragment(), SourceListPresenter.BaseMvpImpl {
    var viewModel: TaskDetailsViewModel? = null
    @Inject
    lateinit var presenter: SourceListPresenter
    lateinit var adapter: BaseQuickLoadMoreAdapter<SourceBean, BaseViewHolder>
    var pageNum = 1
    var type = SourceBean.STATUS_ALL

    override fun getParams(bundle: Bundle?) {
        type = bundle?.getString("type")?: SourceBean.STATUS_ALL
    }

    override fun createPresenter() = presenter
    override fun createMvpView() = this

    override fun getLayoutId() = R.layout.fragment_source_list

    override fun initUi() {
        viewModel = fragmentActivity?.run { TaskDetailsViewModel.get(this) }
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = object : BaseQuickLoadMoreAdapter<SourceBean, BaseViewHolder>(R.layout.item_source, R.layout.layout_empty_source){
            override fun convert(holder: BaseViewHolder, item: SourceBean) {
                holder.setGone(R.id.checkbox, type != SourceBean.STATUS_PK && type != SourceBean.STATUS_PY)
                val checkBox =  holder.getView<HookCheckBox>(R.id.checkbox)
                checkBox.isCheck = item.isCheck
                holder.setText(R.id.tv_name, item.getNonullName())
                        .setText(R.id.tv_code, item.pp_code)
                        .setText(R.id.tv_status, item.getStatusName())
                        .setText(R.id.tv_model, item.getNonullType())
                        .setText(R.id.tv_specs, item.getNonullModel())
                        .setText(R.id.tv_position, item.getNonullAddr())
                        .setText(R.id.tv_remark, item.memo)
                holder.setTextColorRes(R.id.tv_status, when(item.pp_act){
                    SourceBean.STATUS_WAIT -> R.color.purple_9829ff
                    SourceBean.STATUS_FINISH -> R.color.green_09bb07
                    SourceBean.STATUS_PY -> R.color.red_ff852b
                    SourceBean.STATUS_PK -> R.color.red_f5222d
                    else -> R.color.black_999999
                })
            }
        }.apply {
            loadMoreModule.setOnLoadMoreListener {
                load()
            }
            addChildClickViewIds(R.id.btn_remark)
            setOnItemChildClickListener { adapt, view, position ->
                val item = getItem(position)
                val oldStatus = item.pp_act
                fragmentActivity?.apply {
                    RemarkDialog(this).show(item){ newItem ->
                        presenter.updateSource(viewModel?.taskId, newItem){ existNewItem ->
                            if(existNewItem != null) viewModel?.refreshAll?.postValue(Pair(Option.UPDATE, existNewItem))

                            if(oldStatus == existNewItem?.pp_act){//没有修改状态
                                when(existNewItem.pp_act){
                                    SourceBean.STATUS_FINISH ->  viewModel?.refreshFinish?.postValue(Pair(Option.UPDATE, existNewItem))
                                    SourceBean.STATUS_PK ->  viewModel?.refreshPK?.postValue(Pair(Option.UPDATE, existNewItem))
                                    SourceBean.STATUS_PY ->  viewModel?.refreshPY?.postValue(Pair(Option.UPDATE, existNewItem))
                                    SourceBean.STATUS_WAIT ->  viewModel?.refreshWait?.postValue(Pair(Option.UPDATE, existNewItem))
                                }
                            }else{//修改了状态
                                //移除当前的内容
                                when(oldStatus){
                                    SourceBean.STATUS_FINISH -> viewModel?.refreshFinish?.postValue(Pair(Option.DELETE, item))
                                    SourceBean.STATUS_PK -> viewModel?.refreshPK?.postValue(Pair(Option.DELETE, item))
                                    SourceBean.STATUS_PY -> viewModel?.refreshPY?.postValue(Pair(Option.DELETE, item))
                                    SourceBean.STATUS_WAIT -> viewModel?.refreshWait?.postValue(Pair(Option.DELETE, item))
                                }
                                //新增当前内容
                                when(existNewItem?.pp_act){
                                    SourceBean.STATUS_FINISH -> viewModel?.refreshFinish?.postValue(Pair(Option.INSERT, existNewItem))
                                    SourceBean.STATUS_PK -> viewModel?.refreshPK?.postValue(Pair(Option.INSERT, existNewItem))
                                    SourceBean.STATUS_PY -> viewModel?.refreshPY?.postValue(Pair(Option.INSERT, existNewItem))
                                    SourceBean.STATUS_WAIT -> viewModel?.refreshWait?.postValue(Pair(Option.INSERT, existNewItem))
                                }

                                viewModel?.refreshQuantity?.postValue(true)
                            }
                        }
                    }
                }
            }
            setOnItemClickListener { adapt, view, position ->
                val checkbox = adapter.getViewByPosition(position, R.id.checkbox)
                if(checkbox?.visibility == View.VISIBLE){
                    if(checkbox is HookCheckBox){
                        val item = adapter.getItem(position)
                        item.isCheck = !item.isCheck
                        checkbox.isCheck = item.isCheck
                        checkChange()
                    }
                }
            }
            adapter = this
        }

        when(type){
            SourceBean.STATUS_ALL ->{
                view_menu.visibility = View.GONE
                viewModel?.refreshAll?.observe(this, Observer {
                    optAdapter(it)
                })
            }
            SourceBean.STATUS_WAIT ->{
                view_menu.visibility = View.GONE
                viewModel?.refreshWait?.observe(this, Observer {
                    optAdapter(it)
                })
            }
            SourceBean.STATUS_FINISH ->{
                view_menu.visibility = View.GONE
                viewModel?.refreshFinish?.observe(this, Observer {
                    optAdapter(it)
                })
            }
            SourceBean.STATUS_PK ->{
                view_menu.visibility = View.GONE
                viewModel?.refreshPK?.observe(this, Observer {
                    optAdapter(it)
                })
            }
            SourceBean.STATUS_PY ->{
                view_menu.visibility = View.GONE
                viewModel?.refreshPY?.observe(this, Observer {
                    optAdapter(it)
                })
            }
        }

        btn_delete.setOnClickListener {
            DialogHelper.showCommDialog(fragmentActivity,  "温馨提示", "确定是否删除选中的资产", { menuText = "取消"},
                    {
                        menuText = "删除"
                        onClick { rootView, any ->
                            presenter.deleteSource(viewModel?.taskId, adapter.data.filter { it.isCheck }){
                                //刷新全部数据
                                viewModel?.refreshGlobal?.value = true
                            }
                        }
                    })
        }
        checkBox.setOnCheckChangeListener {
            if(checkBox.isPressed){
                adapter.data.forEach { source ->
                    source.isCheck = it
                }
                btn_delete.setTextColor(if(it) ContextCompat.getColor(btn_delete.context, R.color.blue_2981ff) else ContextCompat.getColor(btn_delete.context, R.color.black_666666))
                btn_delete.isEnabled = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun checkChange(){
        if(type != SourceBean.STATUS_PK && type != SourceBean.STATUS_PY) return

        var isAllCheck = true
        var hasCheck = false
        adapter.data.forEach {
            if(it.isCheck) hasCheck = true
            if(!it.isCheck) isAllCheck= false
        }
        if(adapter.data.size == 0){
            isAllCheck = false
            hasCheck = false
        }
        checkBox.isCheck = isAllCheck
        checkBoxText.setTextColor(if(isAllCheck) ContextCompat.getColor(btn_delete.context, R.color.blue_2981ff) else ContextCompat.getColor(btn_delete.context, R.color.black_666666))
        btn_delete.setTextColor(if(hasCheck) ContextCompat.getColor(btn_delete.context, R.color.blue_2981ff) else ContextCompat.getColor(btn_delete.context, R.color.black_666666))
        btn_delete.isEnabled = hasCheck
    }

    private fun dataChange(){
        if(type != SourceBean.STATUS_PK && type != SourceBean.STATUS_PY) return

        view_menu.visibility = if(adapter.data.size > 0) View.VISIBLE else View.GONE
    }

    private fun optAdapter(opt: Pair<Option, SourceBean?>){
        if(opt.first == Option.INSERT){
            if(opt.second != null) adapter.addData(opt.second)
            dataChange()
        }else if(opt.first == Option.UPDATE){
            adapter.updateItem(opt.second)
        }else if(opt.first == Option.DELETE){
            if(opt.second != null) adapter.remove(opt.second)
            dataChange()
        }else if(opt.first == Option.RELOAD){
            reload(null)
        }
        checkChange()
    }

    override fun reload(view: View?) {
        pageNum = 1
        load()
    }

    private fun load(){
        when(type){
            SourceBean.STATUS_ALL ->{
                presenter.querySourceAll(viewModel?.taskId, pageNum, viewModel?.sqlWhere)
            }
            SourceBean.STATUS_WAIT ->{
                presenter.querySourceByWait(viewModel?.taskId, pageNum, viewModel?.sqlWhere)
            }
            SourceBean.STATUS_PY ->{
                presenter.querySourceByPY(viewModel?.taskId, pageNum, viewModel?.sqlWhere)
            }
            SourceBean.STATUS_PK ->{
                presenter.querySourceByPK(viewModel?.taskId, pageNum, viewModel?.sqlWhere)
            }
            SourceBean.STATUS_FINISH ->{
                presenter.querySourceByFinish(viewModel?.taskId, pageNum, viewModel?.sqlWhere)
            }
        }
    }

    override fun updateView(pageNum: Int, data: MutableList<SourceBean>?) {
        adapter.updateData(pageNum, data)
        if(pageNum == 1) dataChange()
        this.pageNum ++
    }

    companion object{
        fun getAllInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putString("type", SourceBean.STATUS_ALL) }) }
        fun getUnInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putString("type", SourceBean.STATUS_WAIT) }) }
        fun getCompleteInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putString("type", SourceBean.STATUS_FINISH) }) }
        fun getOutsideInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putString("type", SourceBean.STATUS_PY) }) }
        fun getWaneInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putString("type", SourceBean.STATUS_PK) }) }
    }
}