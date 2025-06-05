import request from './request'

/**
 * 添加用户
 * @param {Object} data - 用户信息
 * @returns {Promise}
 */
export function addUserApi(data) {
  return request({
    url: '/api/user/add',
    method: 'post',
    data
  })
}

/**
 * 删除用户
 * @param {Object} data - 包含用户id的对象 {id: number}
 * @returns {Promise}
 */
export function deleteUserApi(data) {
  return request({
    url: '/api/user/delete',
    method: 'post',
    data
  })
}

/**
 * 根据ID获取用户详情
 * @param {number} id - 用户ID
 * @returns {Promise}
 */
export function getUserByIdApi(id) {
  return request({
    url: '/api/user/get',
    method: 'get',
    params: { id }
  })
}

/**
 * 获取当前登录用户信息
 * @returns {Promise}
 */
export function getLoginUserApi() {
  return request({
    url: '/api/user/get/login',
    method: 'get'
  })
}

/**
 * 获取用户列表（不分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function listUserApi(params) {
  return request({
    url: '/api/user/list',
    method: 'get',
    params
  })
}

/**
 * 获取用户列表（分页）
 * @param {Object} params - 分页和查询参数
 * @returns {Promise}
 */
export function listUserByPageApi(params) {
  return request({
    url: '/api/user/list/page',
    method: 'get',
    params
  })
}

/**
 * 用户登录
 * @param {Object} data - 登录信息 {userAccount: string, userPassword: string, checkPassword: string}
 * @returns {Promise}
 */
export function userLoginApi(data) {
  return request({
    url: '/api/user/login',
    method: 'post',
    data
  })
}

/**
 * 用户登出
 * @returns {Promise}
 */
export function userLogoutApi() {
  return request({
    url: '/api/user/logout',
    method: 'post'
  })
}

/**
 * 用户注册
 * @param {Object} data - 注册信息
 * @returns {Promise}
 */
export function userRegisterApi(data) {
  return request({
    url: '/api/user/register',
    method: 'post',
    data
  })
}

/**
 * 更新用户信息
 * @param {Object} data - 用户信息
 * @returns {Promise}
 */
export function updateUserApi(data) {
  return request({
    url: '/api/user/update',
    method: 'post',
    data
  })
} 