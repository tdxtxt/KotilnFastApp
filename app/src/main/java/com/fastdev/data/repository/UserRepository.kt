package com.fastdev.data.repository

import com.fastdev.net.ApiClient
import com.fastdev.data.repository.api.UserApi

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/7
 */
object UserRepository {
    private val userApi: UserApi = ApiClient.getUserApi()

}