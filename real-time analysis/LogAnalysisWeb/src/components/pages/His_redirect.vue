<template>
  <div>
  <el-row>
    <el-col :span="10"><div id="myChart" :style="{width: '400px', height: '550px'}"></div></el-col>
    <el-col :span="14"><div id="myChart2" :style="{width: '700px', height: '550px'}"></div></el-col>
  </el-row>
  </div>
</template>


<script>
  export default {
    name: 'hello',
    data () {
      return {
        msg: ''
      }
    },

    mounted(){
      this.drawLine();
    },

    methods: {
      drawLine(){
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById('myChart'),'macarons')
        // 绘制图表
        myChart.setOption({
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            x : 'left',
            itemGap:8,
            data:["main","search","suggestion","share","other"]
          },
          series: [
            {
              name:'流量分析',
              type:'pie',
              center: ['55%', '65%'],
              radius: '50%',
              data: []
            }
          ]
        });

        let myChart2=this.$echarts.init(document.getElementById('myChart2'),'macarons');
        myChart2.setOption({
            tooltip : {
              trigger: 'axis',
            },
            legend: {
              data:["main","search","suggestion","share","other"]
            },
            toolbox: {
              show: true,
              right:'-6',
              itemSize:18,
              itemGap:10,
              feature: {
                mark: {show: true},
                dataView: {
                  show: true,
                  readOnly: false
                },
                magicType: {
                  show: true,
                  type: ['bar', 'line']
                },
                saveAsImage: {
                  show: true
                }
              }
            },
            calculable : true,
          dataZoom: [{
            type: "inside"  //dataZoom是对图形进行缩放，inside是使用滑轮缩放
          }],
            xAxis : [
              {
                type : 'category',
                data : []
              }
            ],
            yAxis : [
              {
                type : 'value',
                splitArea : {show : true}
              }
            ],
          grid: {
            top: "70px",
            bottom: "60px",
            backgroundColor: '#fff',
            right: "30px"
          },
            series : [
              {
                name:'main',
                type:'bar',
                stack: '总量',
                data:[]
              },
              {
                name:'search',
                type:'bar',
                stack: '总量',
                data:[]
              },
              {
                name:'suggestion',
                type:'bar',
                stack: '总量',
                data:[]
              },
              {
                name:'share',
                type:'bar',
                stack: '总量',
                data:[]
              },
              {
                name:'other',
                type:'bar',
                stack: '总量',
                data:[]
              }
            ]
          });
        this.$echarts.connect([myChart, myChart2]);
        this.axios
          .get('/api/hisFlow')
          .then(re => {
            const d=re.data;
            const data=[];
            const date=[];
            const main=[];
            const search=[];
            const suggestion=[];
            const share=[];
            const other=[];
            for(const key in d){
              date.push(key);
              main.push(d[key][0]);
              search.push(d[key][1]);
              suggestion.push(d[key][2]);
              share.push(d[key][3]);
              other.push(d[key][4]);
            }
            console.log(date);
            console.log(main);

            let line1={'value':this.arrSum(main),'name':'main'};
            let line2={'value':this.arrSum(search),'name':'search'};
            let line3={'value':this.arrSum(suggestion),'name':'suggestion'};
            let line4={'value':this.arrSum(share),'name':'share'};
            let line5={'value':this.arrSum(other),'name':'other'};
            data.push(line1);
            data.push(line2);
            data.push(line3);
            data.push(line4);
            data.push(line5);

            myChart.setOption({
              series:[
                {
                  data:data
                },
              ]
            });

            myChart2.setOption({
              xAxis : [
                {
                  data:date
                }
              ],
              series:[
                {
                  name:'main',
                  data:main
                },
                {
                  name:'search',
                  data:search
                },
                {
                  name:'suggestion',
                  data:suggestion
                },
                {
                  name:'share',
                  data:share
                },
                {
                  name:'other',
                  data:other
                },
              ]
            });

          })
      },

    }
  }
</script>

<style scoped>

</style>
