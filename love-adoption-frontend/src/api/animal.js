import request from './request'

/**
 * 添加动物
 * @param {Object} data - 动物信息
 * @returns {Promise}
 */
export function addAnimalApi(data) {
  return request({
    url: '/api/animal/add',
    method: 'post',
    data
  })
}

/**
 * 删除动物
 * @param {Object} data - 包含动物id的对象 {id: number}
 * @returns {Promise}
 */
export function deleteAnimalApi(data) {
  return request({
    url: '/api/animal/delete',
    method: 'post',
    data
  })
}

/**
 * 根据ID获取动物详情
 * @param {number} id - 动物ID
 * @returns {Promise}
 */
export function getAnimalByIdApi(id) {
  return request({
    url: '/api/animal/get',
    method: 'get',
    params: { id }
  })
}

/**
 * 获取动物列表（不分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function listAnimalApi(params) {
  return request({
    url: '/api/animal/list',
    method: 'get',
    params
  })
}

/**
 * 获取动物列表（分页）
 * @param {Object} params - 分页和查询参数
 * @returns {Promise}
 */
export function listAnimalByPageApi(params) {
  return request({
    url: '/api/animal/list/page',
    method: 'get',
    params
  })
}

/**
 * 更新动物信息
 * @param {Object} data - 动物信息
 * @returns {Promise}
 */
export function updateAnimalApi(data) {
  return request({
    url: '/api/animal/update',
    method: 'post',
    data
  })
} 