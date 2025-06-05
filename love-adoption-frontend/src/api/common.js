import request from './request'

/**
 * 文件上传
 * @param {FormData} data - 包含文件的FormData对象
 * @returns {Promise}
 */
export function uploadFileApi(data) {
  return request({
    url: '/api/admin/common/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
} 