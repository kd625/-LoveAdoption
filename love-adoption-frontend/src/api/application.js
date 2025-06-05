import request from './request'

/**
 * 添加申请
 * @param {Object} data - 申请信息
 * @returns {Promise}
 */
export function addApplicationApi(data) {
  return request({
    url: '/api/application/add',
    method: 'post',
    data
  })
}

/**
 * 删除申请
 * @param {Object} data - 包含申请id的对象 {id: number}
 * @returns {Promise}
 */
export function deleteApplicationApi(data) {
  return request({
    url: '/api/application/delete',
    method: 'post',
    data
  })
}

/**
 * 根据ID获取申请详情
 * @param {number} id - 申请ID
 * @returns {Promise}
 */
export function getApplicationByIdApi(id) {
  return request({
    url: '/api/application/get',
    method: 'get',
    params: { id }
  })
}

/**
 * 获取申请列表（不分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function listApplicationApi(params) {
  return request({
    url: '/api/application/list',
    method: 'get',
    params
  })
}

/**
 * 获取申请列表（分页）
 * @param {Object} params - 分页和查询参数
 * @returns {Promise}
 */
export function listApplicationByPageApi(params) {
  return request({
    url: '/api/application/list/page',
    method: 'get',
    params
  })
}

/**
 * 更新申请信息
 * @param {Object} data - 申请信息
 * @returns {Promise}
 */
export function updateApplicationApi(data) {
  return request({
    url: '/api/application/update',
    method: 'post',
    data
  })
} 