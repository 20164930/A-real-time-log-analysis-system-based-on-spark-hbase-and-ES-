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
        let myChart = this.$echarts.init(document.getElementById('myChart'))
        // 绘制图表
        myChart.setOption({
          title: {
            text: '流量曲线',
            textStyle: {
              color: '#000000',
              fontSize: 22
            }
          },
          grid: {
            top: "70px",
            bottom: "60px",
            backgroundColor: '#fff',
            right: "30px"
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['pv','uv','vv','ip']
          },
          dataZoom: [{
            type: "inside"  //dataZoom是对图形进行缩放，inside是使用滑轮缩放
          }],
          toolbox: {
            show: true,
            itemSize: 25,
            itemGap: 15,

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
          calculable: true,
          xAxis: [
            {
              type: 'category',
              boundaryGap: false,
              data:[]
            }
          ],
          yAxis: [
            {
              type: 'value',
            }
          ],
          series: [
            {
              name: 'pv',
              type: 'line',
              symbolSize: 6,
              smooth: true,
              data: []
            },
            {
              name: 'uv',
              type: 'line',
              symbolSize: 6,
              smooth: true,
              data: []
            },
            {
              name: 'vv',
              type: 'line',
              symbolSize: 6,
              smooth: true,
              data: []
            },
            {
              name: 'ip',
              type: 'line',
              symbolSize: 6,
              smooth: true,
              data: []
            },
          ]
        });

        this.axios
          .get('/api/hisTotal')
          .then(re => {
            const d=re.data;
            const date=[];
            const pv=[];
            const uv=[];
            const vv=[];
            const ip=[];

            for(const key in d){
              date.push(key);
              pv.push(d[key][0]);
              uv.push(d[key][1]);
              vv.push(d[key][2]);
              ip.push(d[key][3]);
            }

            console.log(date);
            console.log(pv);
            myChart.setOption({
              xAxis:[{
                data:date
              }],
              series:[
                {
                  name:'pv',
                  data:pv
                },
                {
                  name:'uv',
                  data:uv
                }
                ,
                {
                  name:'vv',
                  data:vv
                }
                ,
                {
                  name:'ip',
                  data:ip
                }
              ]
            })
          })
      }
    }
  }
</script>

<style scoped>

</style>
