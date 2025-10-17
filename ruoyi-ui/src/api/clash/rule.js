import request from '@/utils/request'

export function fuzzyQuery(query) {
  return request({
    url: '/clash/proxyRule/fuzzyQuery',
    method: 'get',
    params: query
  })
}

export function getItem(id) {
  return request({
    url: '/clash/proxyRule/' + id,
    method: 'get'
  })
}

export function insertItem(data) {
  return request({
    url: '/clash/proxyRule',
    method: 'post',
    data: data
  })
}

export function exactQuery(data) {
  return request({
    url: '/clash/proxyRule/exactQuery',
    method: 'post',
    data: data
  })
}

export function updateItem(data) {
  return request({
    url: '/clash/proxyRule',
    method: 'put',
    data: data
  })
}

export function deleteItems(ids) {
  return request({
    url: '/clash/proxyRule/' + ids,
    method: 'delete'
  })
}
