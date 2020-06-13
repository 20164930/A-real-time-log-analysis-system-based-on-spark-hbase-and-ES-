<template>
  <div style="margin-top: 15px;">
    <el-input placeholder="请输入内容" v-model="input" class="input">
      <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
    </el-input>
    <el-table :data="tableData" stripe style="width: 100%;margin-top: 20px">
    <el-table-column prop="timestamp" label="时间戳" width="125" align="center">
    </el-table-column>
    <el-table-column prop="session_id" label="sesson_id" width="90" align="center">
    </el-table-column>
    <el-table-column prop="date"  label="日期" width="90" align="center">
    </el-table-column>
    <el-table-column prop="uid" label="uid" width="100" align="center">
    </el-table-column>
    <el-table-column prop="ip" label="ip" width="130" align="center">
    </el-table-column>
    <el-table-column prop="local_list" label="住址" width="120" align="center">
    </el-table-column>
    <el-table-column prop="referrer" label="referrer" width="90" align="center">
    </el-table-column>
      <el-table-column prop="cookie" label="cookie" align="center">
      </el-table-column>
    <el-table-column prop="params" label="params" align="center">
    </el-table-column>
    </el-table>
  </div>
</template>

<script>
    export default {
      name: "Search",
      data () {
        return {
          input: '',
          tableData:[]
        }
      },
      methods:{
        search:function(){
          this.axios
            .post('/api/hisSearch',{keyword:this.input.trim()})
            .then(re => {
              const d=re.data;
              for(let i=0;i<d.length;i++){
                let tmp={
                  'timestamp':d[i]['timestamp'],
                  'session_id':d[i]['session_id'],
                  'date':d[i]['date'],
                  'uid':d[i]['uid'],
                  'ip':d[i]['ip'],
                  'local_list':d[i]['local_list'],
                  'referrer':d[i]['referrer'],
                  'cookie':d[i]['cookie'],
                  'params':d[i]['params']
                };
                this.tableData.push(tmp);
              }
            })
        }
      },

    }


</script>

