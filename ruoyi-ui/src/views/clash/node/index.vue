<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="节点名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入节点名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择节点类型" clearable>
          <el-option
            v-for="dict in dict.type.clash_node_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="节点状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择节点状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['clash:node:add']"
        >新增
        </el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="info"-->
      <!--          plain-->
      <!--          icon="el-icon-sort"-->
      <!--          size="mini"-->
      <!--          @click="toggleExpandAll"-->
      <!--        >展开/折叠-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="nodeList"
      row-key="menuId"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column type="selection" align="center"/>
      <el-table-column prop="name" label="节点名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="type" label="节点类型"></el-table-column>
      <el-table-column prop="server" label="代理服务器地址" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="port" label="代理服务器端口"></el-table-column>
      <!--      <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true"></el-table-column>-->
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['clash:node:edit']"
          >修改
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-copy"
            @click="handleCopy(scope.row)"
            v-hasPermi="['clash:node:add']"
          >复制
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['clash:node:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="节点类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio
                  v-for="dict in dict.type.clash_node_type"
                  :key="dict.value"
                  :label="dict.value"
                  :value="dict.label"
                />
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-form-item label="节点名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入节点名称"/>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="代理服务器地址" prop="server">
            <el-input v-model="form.server" placeholder="请输入代理服务器地址"/>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="代理服务器端口" prop="port">
            <el-input-number v-model="form.port" :min="1" :max="65535" label="请输入代理服务器端口"></el-input-number>
          </el-form-item>
        </el-row>

        <el-row v-if="form.type == nodeType.HTTP">
          <el-form-item label="是否开启tls" prop="tls">
            <el-switch
              v-model="form.tls">
            </el-switch>
          </el-form-item>
        </el-row>

        <el-row v-if="form.type == nodeType.SHADOW_SOCKS">
          <el-form-item required label="加密算法" prop="cipher">
            <el-select v-model="form.cipher" placeholder="请选择加密算法">
              <el-option
                v-for="dict in dict.type.clash_node_cipher"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-row>

        <el-row v-if="form.type == nodeType.SHADOW_SOCKS || form.type == nodeType.TROJAN">
          <el-form-item label="代理认证密码" prop="password">
            <el-input
              v-model="form.password"
              placeholder="请输入代理认证密码"
            />
          </el-form-item>
        </el-row>


        <el-collapse>
          <el-collapse-item title="更多设置" name="1">
            <el-row>
              <el-form-item label="允许udp流量" prop="udp">
                <el-switch
                  v-model="form.udp">
                </el-switch>
              </el-form-item>
            </el-row>

            <el-row>
              <el-form-item label="跳过证书验证" prop="skipCertVerify">
                <el-switch
                  v-model="form.skipCertVerify">
                </el-switch>
              </el-form-item>
            </el-row>

            <el-row>
              <el-form-item label="服务器名称指示" prop="sni">
                <el-input
                  v-model="form.sni"
                  placeholder="请输入服务器名称指示"
                />
              </el-form-item>
            </el-row>

            <el-row>
              <el-form-item label="备注" prop="remark">
                <el-input
                  type="textarea"
                  autosize
                  placeholder="请输入备注"
                  v-model="form.remark"/>
              </el-form-item>
            </el-row>

            <el-row>
              <el-form-item label="状态" prop="status">
                <el-select v-model="form.status" placeholder="请选择状态">
                  <el-option
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-row>
          </el-collapse-item>
        </el-collapse>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listNode, getNode, delNode, addNode, updateNode, exactQuery} from "@/api/clash/node"

export default {
  name: "Node",
  dicts: ['sys_normal_disable', 'clash_node_type', 'clash_node_cipher'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 角色表格数据
      nodeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // 日期范围
      dateRange: [],
      // 数据范围选项
      dataScopeOptions: [
        {
          value: "1",
          label: "全部数据权限"
        },
        {
          value: "2",
          label: "自定数据权限"
        },
        {
          value: "3",
          label: "本部门数据权限"
        },
        {
          value: "4",
          label: "本部门及以下数据权限"
        },
        {
          value: "5",
          label: "仅本人数据权限"
        }
      ],
      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        type: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        type: [
          {required: true, message: "节点类型不能为空", trigger: "blur"}
        ],
        name: [
          {required: true, message: "节点名称不能为空", trigger: "blur"},
          {validator: this.exactQuery, trigger: "blur"}
        ],
        server: [
          {required: true, message: "代理服务器地址不能为空", trigger: "blur"}
        ],
        port: [
          {required: true, message: "代理服务器端口不能为空", trigger: "blur"}
        ],
        cipher: [
          {required: true, message: "加密算法不能为空", trigger: "blur"}
        ],
        password: [
          {required: true, message: "代理认证密码不能为空", trigger: "blur"}
        ],
        status: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ]
      },
      nodeType: {
        HTTP: "HTTP",
        TROJAN: "TROJAN",
        SHADOW_SOCKS: "SHADOW_SOCKS",
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async exactQuery(rule, value, callback) {
      if (value) {
        const response = await exactQuery({name: this.form.name});
        console.log(response)
        if (response.data && response.data.length > 0) {
          const daoProxyNode = response.data[0];
          if (daoProxyNode.id != this.form.id) {
            console.log(daoProxyNode.id != this.form.id);
            callback(new Error(`名称为【${this.form.name}】的节点已存在`))
          }
        }
      }
      callback();
    },
    /** 查询角色列表 */
    getList() {
      this.loading = true
      listNode(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.nodeList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    /** 查询菜单树结构 */
    getMenuTreeselect() {
      menuTreeselect().then(response => {
        this.menuOptions = response.data
      })
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys()
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    // 所有部门节点数据
    getDeptAllCheckedKeys() {
      // 目前被选中的部门节点
      let checkedKeys = this.$refs.dept.getCheckedKeys()
      // 半选中的部门节点
      let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    /** 根据角色ID查询菜单树结构 */
    getRoleMenuTreeselect(roleId) {
      return roleMenuTreeselect(roleId).then(response => {
        this.menuOptions = response.menus
        return response
      })
    },
    /** 根据角色ID查询部门树结构 */
    getDeptTree(roleId) {
      return deptTreeSelect(roleId).then(response => {
        this.deptOptions = response.depts
        return response
      })
    },
    // 角色状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.roleName + '"角色吗？').then(function () {
        return changeRoleStatus(row.roleId, row.status)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0"
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 取消按钮（数据权限）
    cancelDataScope() {
      this.openDataScope = false
      this.reset()
    },
    // 表单重置
    reset() {
      if (this.$refs.menu != undefined) {
        this.$refs.menu.setCheckedKeys([])
      }
      this.menuExpand = false,
        this.menuNodeAll = false,
        this.deptExpand = true,
        this.deptNodeAll = false,
        this.form = {
          type: this.nodeType.SHADOW_SOCKS,
          tls: true,
          udp: true,
          skipCertVerify: true,
          status: "0",
          remark: undefined
        }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleDataScope":
          this.handleDataScope(row)
          break
        case "handleAuthUser":
          this.handleAuthUser(row)
          break
        default:
          break
      }
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value
        }
      } else if (type == 'dept') {
        let treeList = this.deptOptions
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value
        }
      }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions : [])
      } else if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions : [])
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.form.menuCheckStrictly = value ? true : false
      } else if (type == 'dept') {
        this.form.deptCheckStrictly = value ? true : false
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      // this.getMenuTreeselect()
      this.open = true
      this.title = "添加节点"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getNode(row.id).then(response => {
        this.form = response.data
        this.open = true
      })
      this.title = "修改节点"
    },
    /** 选择角色权限范围触发 */
    dataScopeSelectChange(value) {
      if (value !== '2') {
        this.$refs.dept.setCheckedKeys([])
      }
    },
    /** 分配数据权限操作 */
    handleDataScope(row) {
      this.reset()
      const deptTreeSelect = this.getDeptTree(row.roleId)
      getRole(row.roleId).then(response => {
        this.form = response.data
        this.openDataScope = true
        this.$nextTick(() => {
          deptTreeSelect.then(res => {
            this.$refs.dept.setCheckedKeys(res.checkedKeys)
          })
        })
      })
      this.title = "分配数据权限"
    },
    /** 分配用户操作 */
    handleAuthUser: function (row) {
      const roleId = row.roleId
      this.$router.push("/system/role-auth/user/" + roleId)
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateNode(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addNode(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 提交按钮（数据权限） */
    submitDataScope: function () {
      if (this.form.roleId != undefined) {
        this.form.deptIds = this.getDeptAllCheckedKeys()
        dataScope(this.form).then(response => {
          this.$modal.msgSuccess("修改成功")
          this.openDataScope = false
          this.getList()
        })
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('确认删除名称为"' + row.name + '"的数据项？').then(function () {
        return delNode(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 修改按钮操作 */
    handleCopy(row) {
      this.reset()
      getNode(row.id).then(response => {
        response.data.id = undefined
        response.data.name += "【复制】"
        this.form = response.data
        this.open = true
      })
      this.title = "复制节点"
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/role/export', {
        ...this.queryParams
      }, `role_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
