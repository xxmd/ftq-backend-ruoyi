<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="规则类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择规则类型" clearable>
          <el-option
            v-for="dict in dict.type.clash_rule_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="策略类型" prop="policy">
        <el-select v-model="queryParams.policy" placeholder="请选择策略类型" clearable>
          <el-option
            v-for="dict in dict.type.clash_policy_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="规则状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择规则状态" clearable>
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
    >
      <!--      <el-table-column type="selection" align="center"/>-->
      <el-table-column prop="type" label="规则类型">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.clash_rule_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="规则内容" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="policy" label="策略类型">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.clash_policy_type" :value="scope.row.policy"/>
        </template>
      </el-table-column>
      <el-table-column prop="proxyNode.name" label="节点名称">
      </el-table-column>
      <el-table-column prop="proxyGroup.name" label="分组名称">
      </el-table-column>
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
      <el-table-column prop="sort" label="排序">
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['clash:rule:edit']"
          >修改
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-copy"
            @click="handleCopy(scope.row)"
            v-hasPermi="['clash:rule:add']"
          >复制
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['clash:rule:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-form-item label="规则类型" prop="type">
            <el-radio-group v-model="form.type">
              <el-radio
                v-for="dict in dict.type.clash_rule_type"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="规则内容" prop="content">
            <el-input v-model="form.content" placeholder="请输入规则内容"/>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="策略类型" prop="policy">
            <el-select v-model="form.policy" placeholder="请选择策略类型">
              <el-option
                v-for="dict in dict.type.clash_policy_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-row>

        <el-row v-if="form.policy === ProxyRulePolicyType.PROXY.value">
          <el-form-item label="代理节点" prop="proxyNode.id">
            <el-select v-model="form.proxyNode.id" filterable placeholder="请选择代理节点">
              <el-option
                v-for="item in nodeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                :disabled="item.status == 1"
              />
            </el-select>
          </el-form-item>
        </el-row>

        <el-row v-if="form.policy === ProxyRulePolicyType.PROXY_GROUP.value">
          <el-form-item label="代理分组" prop="proxyGroup.id">
            <el-select v-model="form.proxyGroup.id" filterable placeholder="请选择代理分组">
              <el-option
                v-for="item in groupList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                :disabled="item.status == 1"
              />
            </el-select>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="form.sort" :min="0"></el-input-number>
          </el-form-item>
        </el-row>

        <el-collapse>
          <el-collapse-item title="更多设置" name="1">
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
import {fuzzyQuery, exactQuery, deleteItems, insertItem, updateItem, getItem} from "@/api/clash/rule"
import * as nodeApi from "@/api/clash/node";
import * as groupApi from "@/api/clash/group";
import node from "@/views/clash/node/index.vue";
import {getNode} from "@/api/clash/node";

export default {
  name: "Node",
  dicts: ['sys_normal_disable', 'clash_rule_type', 'clash_policy_type'],
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
      groupList: [],
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        type: [
          {required: true, message: "规则类型不能为空", trigger: "blur"}
        ],
        content: [
          {validator: this.validateContent, trigger: "blur"}
        ],
        policy: [
          {required: true, message: "策略类型不能为空", trigger: "blur"}
        ],
        proxyNode: {
          id: [
            {validator: this.validateNode, trigger: "blur"}
          ],
        },
        proxyGroup: {
          id: [
            {validator: this.validateGroup, trigger: "blur"}
          ],
        },
        status: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ]
      },
      ProxyRuleType: {
        MATCH: {
          label: "全匹配",
          value: "MATCH",
        },
        GEOIP: {
          label: "IP国家",
          value: "GEOIP",
        }
      },
      ProxyRulePolicyType: {
        DIRECT: {
          label: "直连",
          value: "DIRECT",
        },
        REJECT: {
          label: "拒绝",
          value: "DIRECT",
        },
        PROXY: {
          label: "代理节点",
          value: "PROXY",
        },
        PROXY_GROUP: {
          label: "代理分组",
          value: "PROXY_GROUP",
        },
      }
    }
  },
  created() {
    this.getList()
    this.getNodeList()
    this.getGroupList()
  },
  methods: {
    validateContent(rule, value, callback) {
      if (this.form.type === this.ProxyRuleType.MATCH.value || value) {
        callback();
      } else {
        callback(new Error(`当规则类型不为${this.ProxyRuleType.MATCH.label}时，规则内容必填`))
      }
    },
    validateNode(rule, value, callback) {
      console.log("validateNode", rule, value);
      if (this.form.policy === this.ProxyRulePolicyType.PROXY.value && !value) {
        callback(new Error(`当策略类型为${this.ProxyRulePolicyType.PROXY.label}时，代理节点必选`))
      } else {
        callback();
      }
    },
    validateGroup(rule, value, callback) {
      console.log("validateGroup", rule, value);
      if (this.form.policy === this.ProxyRulePolicyType.PROXY_GROUP.value && !value) {
        callback(new Error(`当策略类型为${this.ProxyRulePolicyType.PROXY_GROUP.label}时，代理分组必选`))
      } else {
        callback()
      }
    },
    getNodeList() {
      nodeApi.listNode({}).then(response => {
        this.nodeList = response.rows;
      })
    },
    getGroupList() {
      groupApi.fuzzyQuery({}).then(response => {
        this.groupList = response.rows;
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
        type: this.ProxyRuleType.GEOIP.value,
        policy: this.ProxyRulePolicyType.PROXY_GROUP.value,
        proxyNode: {},
        proxyGroup: {},
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加分流规则"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getItem(row.id).then(response => {
        response.data.proxyNode = response.data.proxyNode || {}
        response.data.proxyGroup = response.data.proxyGroup || {}
        this.form = response.data
        this.open = true
      })
      this.title = "修改分流规则"
    },
    /** 提交按钮 */
    submitForm: function () {
      console.log("submitForm")
      this.$refs["form"].validate(valid => {
        if (valid) {
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
        } else {
          console.log("error submit!!")
        }
      })
    },
    handleCopy(row) {
      this.reset()
      getItem(row.id).then(response => {
        response.data.id = undefined
        this.form = response.data
        this.open = true
      })
      this.title = "复制分流规则"
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      console.log(this.dict.type.clash_rule_type)
      console.log(row.type)
      const specialType = this.dict.type.clash_rule_type.find(it => it.value == row.type)
      console.log(specialType)
      const name = `${specialType.label}-${row.content}`
      this.$modal.confirm(`确认删除【${name}】数据项？`).then(function () {
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
