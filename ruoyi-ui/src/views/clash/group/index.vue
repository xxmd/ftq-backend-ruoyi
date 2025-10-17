<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="分组名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入分组名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分组类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择分组类型" clearable>
          <el-option
            v-for="dict in dict.type.clash_group_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="分组状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择分组状态" clearable>
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="itemList"
      row-key="id"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <!--      <el-table-column type="selection" align="center"/>-->
      <el-table-column prop="name" label="分组名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="type" label="分组类型"></el-table-column>
      <el-table-column prop="url" label="延迟测试链接" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="intervalSeconds" label="延迟测试间隔（S）"></el-table-column>
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
            v-hasPermi="['clash:group:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['clash:group:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-form-item label="分组类型" prop="type">
            <el-radio-group v-model="form.type">
              <el-radio
                v-for="dict in dict.type.clash_group_type"
                :key="dict.value"
                :label="dict.value"
                :value="dict.label"
              />
            </el-radio-group>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="分组名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入分组名称"/>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="下属节点" prop="nodeIdList">
            <el-transfer v-model="form.nodeIdList" :data="nodeList"
                         :titles="['可用节点', '选中节点']"></el-transfer>
          </el-form-item>
        </el-row>


        <el-collapse>
          <el-collapse-item title="更多设置" name="1">
            <el-row>
              <el-form-item label="延迟测试链接" prop="url">
                <el-input v-model="form.url" placeholder="请输入延迟测试链接"/>
              </el-form-item>
            </el-row>

            <el-row>
              <el-form-item label="延迟测试间隔" prop="intervalSeconds">
                <el-input-number v-model="form.intervalSeconds" :min="60" :max="3600"
                                 label="请输入延迟测试间隔"></el-input-number>
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
import {fuzzyQuery, exactQuery, deleteItems, insertItem, updateItem, getItem} from "@/api/clash/group"
import {listNode} from "@/api/clash/node"

export default {
  name: "Node",
  dicts: ['sys_normal_disable', 'clash_group_type'],
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
      itemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        type: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      nodeList: [],
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        type: [
          {required: true, message: "分组类型不能为空", trigger: "blur"}
        ],
        name: [
          {required: true, message: "分组名称不能为空", trigger: "blur"},
          {validator: this.validateName, trigger: "blur"}
        ],
        url: [
          {
            type: "url",
            message: "请输入合法的URL地址，例如：https://example.com",
            trigger: ["blur", "change"]
          }
        ],
        status: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ]
      },
      ProxyGroupType: {
        URL_TEST: "URL_TEST",
        MANUAL_SELECT: "MANUAL_SELECT",
        LOAD_BALANCE: "LOAD_BALANCE",
      }
    }
  },
  created() {
    this.getList()
    this.getNodeList()
  },
  methods: {
    async validateName(rule, value, callback) {
      if (value) {
        const response = await exactQuery({name: this.form.name});
        if (response.data && response.data.length > 0) {
          const daoItem = response.data[0];
          if (daoItem.id != this.form.id) {
            callback(new Error(`名称为【${this.form.name}】的分组已存在`))
          }
        }
      }
      callback();
    },
    getNodeList() {
      listNode({}).then(response => {
        this.nodeList = response.rows.map((item) => {
          return {
            key: item.id,
            label: item.name,
            disabled: item.status == 1,
          }
        })
      })
    },
    getList() {
      this.loading = true
      fuzzyQuery(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.itemList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        type: this.ProxyGroupType.URL_TEST,
        url: "http://www.gstatic.com/generate_204",
        intervalSeconds: 60,
        nodeIdList: [],
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
      this.ids = selection.map(item => item.id)
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加代理分组"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getItem(row.id).then(response => {
        response.data.nodeIdList = response.data.proxies.map(item => item.id)
        this.form = response.data
        // this.form.nodeIdList = [];
        this.open = true
      })
      this.title = "修改代理分组"
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.proxies = this.form.nodeIdList.map(item => {
            return {
              id: item,
            }
          })
          if (this.form.id != undefined) {
            updateItem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            insertItem(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('确认删除名称为"' + row.name + '"的数据项？').then(function () {
        return deleteItems(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
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
