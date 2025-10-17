import request from '@/utils/request'

export function fuzzyQuery(query) {
  return request({
    url: '/clash/proxyGroup/fuzzyQuery',
    method: 'get',
    params: query
  })
}

export function getItem(id) {
  return request({
    url: '/clash/proxyGroup/' + id,
    method: 'get'
  })
}

export function insertItem(data) {
  return request({
    url: '/clash/proxyGroup',
    method: 'post',
    data: data
  })
}

export function exactQuery(data) {
  return request({
    url: '/clash/proxyGroup/exactQuery',
    method: 'post',
    data: data
  })
}

export function updateItem(data) {
  return request({
    url: '/clash/proxyGroup',
    method: 'put',
    data: data
  })
}

export function deleteItems(ids) {
  return request({
    url: '/clash/proxyGroup/' + ids,
    method: 'delete'
  })
}
