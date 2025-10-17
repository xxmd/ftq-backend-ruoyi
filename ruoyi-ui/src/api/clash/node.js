import request from '@/utils/request'

// 查询节点列表
export function listNode(query) {
  return request({
    url: '/clash/proxyNode/list',
    method: 'get',
    params: query
  })
}

// 查询节点详细
export function getNode(id) {
  return request({
    url: '/clash/proxyNode/' + id,
    method: 'get'
  })
}

// 新增节点
export function addNode(data) {
  return request({
    url: '/clash/proxyNode',
    method: 'post',
    data: data
  })
}

// 精确查询
export function exactQuery(data) {
  return request({
    url: '/clash/proxyNode/exactQuery',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateNode(data) {
  return request({
    url: '/clash/proxyNode',
    method: 'put',
    data: data
  })
}

// // 角色数据权限
// export function dataScope(data) {
//   return request({
//     url: '/system/role/dataScope',
//     method: 'put',
//     data: data
//   })
// }
//
// // 角色状态修改
// export function changeRoleStatus(roleId, status) {
//   const data = {
//     roleId,
//     status
//   }
//   return request({
//     url: '/system/role/changeStatus',
//     method: 'put',
//     data: data
//   })
// }

// 删除节点
export function delNode(ids) {
  return request({
    url: '/clash/proxyNode/' + ids,
    method: 'delete'
  })
}

// // 查询角色已授权用户列表
// export function allocatedUserList(query) {
//   return request({
//     url: '/system/role/authUser/allocatedList',
//     method: 'get',
//     params: query
//   })
// }
//
// // 查询角色未授权用户列表
// export function unallocatedUserList(query) {
//   return request({
//     url: '/system/role/authUser/unallocatedList',
//     method: 'get',
//     params: query
//   })
// }
//
// // 取消用户授权角色
// export function authUserCancel(data) {
//   return request({
//     url: '/system/role/authUser/cancel',
//     method: 'put',
//     data: data
//   })
// }
//
// // 批量取消用户授权角色
// export function authUserCancelAll(data) {
//   return request({
//     url: '/system/role/authUser/cancelAll',
//     method: 'put',
//     params: data
//   })
// }
//
// // 授权用户选择
// export function authUserSelectAll(data) {
//   return request({
//     url: '/system/role/authUser/selectAll',
//     method: 'put',
//     params: data
//   })
// }
//
// // 根据角色ID查询部门树结构
// export function deptTreeSelect(roleId) {
//   return request({
//     url: '/system/role/deptTree/' + roleId,
//     method: 'get'
//   })
// }
