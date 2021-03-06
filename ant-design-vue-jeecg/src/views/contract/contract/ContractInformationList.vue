<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="合同编号">
              <a-input placeholder="请输入合同编号" v-model="queryParam.contractNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="凭证号">
              <a-input placeholder="请输入凭证号" v-model="queryParam.voucherNo"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="物资名称">
                <a-input placeholder="请输入物资名称" v-model="queryParam.materialName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="收货单位">
                <a-input placeholder="请输入收货单位" v-model="queryParam.receivingUnit"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('合同信息表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <contract-information-modal ref="modalForm" @ok="modalFormOk"></contract-information-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContractInformationModal from './modules/ContractInformationModal'

  export default {
    name: 'ContractInformationList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ContractInformationModal
    },
    data () {
      return {
        description: '合同信息表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'合同编号',
            align:"center",
            dataIndex: 'contractNo'
          },
          {
            title:'凭证号',
            align:"center",
            dataIndex: 'voucherNo'
          },
          {
            title:'合同类型',
            align:"center",
            dataIndex: 'contractType'
          },
          {
            title:'物资编码',
            align:"center",
            dataIndex: 'materialCode'
          },
          {
            title:'物资名称',
            align:"center",
            dataIndex: 'materialName'
          },
          {
            title:'供货单位',
            align:"center",
            dataIndex: 'supplier'
          },
          {
            title:'收货单位',
            align:"center",
            dataIndex: 'receivingUnit'
          },
          {
            title:'检斤',
            align:"center",
            dataIndex: 'weighing'
          },
          {
            title:'合同单价',
            align:"center",
            dataIndex: 'contractPrice'
          },
          {
            title:'结算结果',
            align:"center",
            dataIndex: 'settlementResults'
          },
          {
            title:'结算单价',
            align:"center",
            dataIndex: 'settlemenPrice'
          },
          {
            title:'结算数量',
            align:"center",
            dataIndex: 'settlementQuantity'
          },
          {
            title:'贷款',
            align:"center",
            dataIndex: 'loan'
          },
          {
            title:'税金',
            align:"center",
            dataIndex: 'taxes'
          },
          {
            title:'含税',
            align:"center",
            dataIndex: 'taxIncluded'
          },
          {
            title:'税率',
            align:"center",
            dataIndex: 'taxRate'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title:'取样日期',
            align:"center",
            dataIndex: 'weighingDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'结算人',
            align:"center",
            dataIndex: 'clearingHouse'
          },
          {
            title:'结算日期',
            align:"center",
            dataIndex: 'settlementDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'结算标识',
            align:"center",
            dataIndex: 'settlementIdentification'
          },
          {
            title:'是否删除',
            align:"center",
            dataIndex: 'isDelete'
          },
          {
            title:'运费',
            align:"center",
            dataIndex: 'shipping'
          },
          {
            title:'点收',
            align:"center",
            dataIndex: 'accept'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/contract/contractInformation/list",
          delete: "/contract/contractInformation/delete",
          deleteBatch: "/contract/contractInformation/deleteBatch",
          exportXlsUrl: "/contract/contractInformation/exportXls",
          importExcelUrl: "contract/contractInformation/importExcel",
          
        },
        dictOptions:{},
      }
    },
    created() {
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>