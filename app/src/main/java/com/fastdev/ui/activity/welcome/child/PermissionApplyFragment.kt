package com.fastdev.ui.activity.welcome.child

import android.Manifest
import com.baselib.helper.RequestPermissionHelper
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.fastdev.ui.activity.welcome.WellcomeActivity
import kotlinx.android.synthetic.main.fragment_wellcome_permission_apply.*

class PermissionApplyFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_wellcome_permission_apply

    override fun initUi() {
        btn_next.setOnClickListener {
            isPermissionDisplay = true
            applyPermission {
                getActivityNew<WellcomeActivity>()?.handleResult()
            }
        }
    }

    private fun applyPermission(callback: () -> Unit){
        RequestPermissionHelper.requestPermission(activity, permissions){
            grantedPermission { callback.invoke() }

            deniedPermission { callback.invoke() }
        }
    }

    companion object{
        var isPermissionDisplay = true

        val permissions = listOf(/*Manifest.permission.READ_PHONE_STATE,*/
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                /* Manifest.permission.CAMERA,*/
                /* Manifest.permission.READ_CONTACTS,*/
               /* Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION*/
        )
    }
   }