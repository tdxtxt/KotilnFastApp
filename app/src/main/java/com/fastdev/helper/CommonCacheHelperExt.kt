package com.fastdev.helper

import com.baselib.helper.CommonCacheHelper
import com.fastdev.data.response.LoginEntity
import com.fastdev.data.response.LoginUser

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/7
 */

fun CommonCacheHelper.getGuideVersion(): String? = CommonCacheHelper.getString("guideVersion", "")
fun CommonCacheHelper.putGuideVersion(versionCode: String) = CommonCacheHelper.putString("guideVersion", versionCode)

fun CommonCacheHelper.isLogin(): Boolean = getLogin() != null
fun CommonCacheHelper.getAccountNo() = getLogin()?.passport
fun CommonCacheHelper.getToken() = getLogin()?.token?: ""

fun CommonCacheHelper.saveLoginUser(name: String?, pwd: String?) = CommonCacheHelper.put<LoginUser>("loginUser", LoginUser(name, pwd))
fun CommonCacheHelper.getLoginUser() = CommonCacheHelper.getParcelable("loginUser", LoginUser::class.java)

fun CommonCacheHelper.saveLogin(data: LoginEntity) = CommonCacheHelper.put<LoginEntity>("loginResult", data)
fun CommonCacheHelper.getLogin(): LoginEntity? = CommonCacheHelper.getParcelable("loginResult", LoginEntity::class.java)
fun CommonCacheHelper.clearLogin() = CommonCacheHelper.remove("loginResult")