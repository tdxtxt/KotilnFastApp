package com.fastdev.data.repository

import com.fastdev.net.ApiClient
import com.fastdev.data.repository.api.UserApi
import com.fastdev.data.repository.base.BaseRepository
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/7
 */
class UserRepository @Inject constructor(): BaseRepository() {
    private val userApi: UserApi = ApiClient.getUserApi()

}