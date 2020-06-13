<template>
  <div id="myChart" :style="{width: '1050px', height: '550px'}"></div>
</template>

<script>
  import 'echarts/map/js/china.js';
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
        let myChart = this.$echarts.init(document.getElementById('myChart'),'macarons');
        // 绘制图表
        myChart.setOption({
          tooltip : {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            x:'left',
            data:['用户数']
          },
          dataRange: {
            min: 0,
            max: 10000,
            x: '40',
            y: 'bottom',
            text:['高','低'],
            calculable : true
          },
          toolbox: {
            show: true,
            right:'10',
            itemSize:18,
            itemGap:10,
            feature: {
              mark: {show: true},
              dataView: {
                show: true,
                readOnly: false
              },
              saveAsImage: {
                show: true
              }
            }
          },
          series: [
            {
            name:'用户数',
            type: 'map',
            map: 'china',
            zoom:1.2,
            itemStyle:{
              normal:{label:{show:true}},
              emphasis:{label:{show:true}}
            },
            data:[]
          }]
        });
        this.axios
          .get('/api/userLocal')
          .then(re => {
            const d=re.data;
            const data=[];
            for(const key in d){
              let line={'value':d[key],'name':key};
              data.push(line);
            }

            myChart.setOption({
              series:[
                {
                  data:data
                },
              ]
            });
          });
      }

    }
  }
</script>

<style scoped>

</style>
