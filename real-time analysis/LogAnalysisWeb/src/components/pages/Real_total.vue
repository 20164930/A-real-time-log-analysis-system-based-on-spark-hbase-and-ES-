<template>
  <div id="myChart" :style="{width: '1050px', height: '550px'}"></div>
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
        let myChart = this.$echarts.init(document.getElementById('myChart'));
        // 绘制图表
        myChart.setOption({
          title: [{
            text: '实时曲线',
            textStyle: {
              color: '#000000',
              fontSize: 18
            }
          },
            {
              text: '当日曲线',
              top:'50%',
              textStyle: {
                color: '#000000',
                fontSize: 18
              }
            }],
          grid: [{
            top: "10",
            bottom: "55%",
            backgroundColor: '#fff',
            right: "20px"
          },
            {
              top: "55%",
              bottom: "40",
              backgroundColor: '#fff',
              right: "20px"
            },],

          tooltip: [{
            trigger: 'axis',
            formatter: function (params) {
              params = params[0];
              var date = new Date(params.value);
              return  date.getHours()+":"+date.getMinutes()
                +":"+date.getSeconds()+"<br>用户流量:"+ params.value[1];
            },
            axisPointer: {
              animation: false
            }
          }],
          dataZoom: [{
            type: "inside" , //dataZoom是对图形进行缩放，inside是使用滑轮缩放
            xAxisIndex: 0,

          },
            {
              type: "inside",  //dataZoom是对图形进行缩放，inside是使用滑轮缩放
              xAxisIndex: 1,

            }],
          xAxis: [
            {
              type: 'time',
              gridIndex:0,
              splitLine: {
                show: false
              }
            },
            {
              type: 'time',
              gridIndex:1,
              splitLine: {
                show: false
              }
            }
          ],
          yAxis: [{
            type: 'value',
            gridIndex:0,
            boundaryGap: [0, '100%'],
            splitLine: {
              show: false
            }
          },
            {
              type: 'value',
              gridIndex:1,
              boundaryGap: [0, '100%'],
              splitLine: {
                show: false
              }
            }],
          series: [
            {
              name: '实时流量',
              type: 'line',
              showSymbol: false,
              hoverAnimation: false,
              xAxisIndex: 0,
              yAxisIndex: 0,
              data: []
            },
            {
              name: '当日流量',
              type: 'line',
              showSymbol: false,
              hoverAnimation: false,
              xAxisIndex: 1,
              yAxisIndex: 1,
              data: []
            }
          ]
        });

        setInterval(()=>{
          this.axios.all([
            this.axios.get('/api/realReal'),
            this.axios.get('/api/realTotal'),
          ])
            .then(this.axios.spread(function (re1,re2)  {
              let d1=re1.data;
              let d2=re2.data;
              const data1=[];
              const data2=[];
              for(const key in d1){
                let tmp=new Date(parseInt(key));
                data1.push([tmp,d1[key]]);
              }
              for(const key in d2){
                let tmp=new Date(parseInt(key));
                data2.push([tmp,d2[key]]);
              }
              myChart.setOption({
                series:[{
                  data:data1
                },{
                  data:data2
                }]
              })
            }));
        }, 3000);
      }
    }
  }
</script>

<style scoped>

</style>
