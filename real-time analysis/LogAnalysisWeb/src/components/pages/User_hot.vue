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
        let myChart = this.$echarts.init(document.getElementById('myChart'));

        myChart.setOption({
          title: [{
            text: '歌曲热点',
            left: '25%',
            textAlign: 'center',
            textStyle: {
              color: '#000000',
              fontSize: 22
            }
          },
            {
              text: '歌手热点',
              left: '75%',
              textAlign: 'center',
              textStyle: {
                color: '#000000',
                fontSize: 22
              }
            }],
          tooltip: {},
          grid: [{
            top:40,
            bottom:'50%',
            left:10,
            width:'45%',
            backgroundColor: '#fff',
            containLabel: true
          },
            {
              top: "55%",
              bottom: "20",
              width:'45%',
              left:22,
              backgroundColor: '#fff',
              containLabel: true
            },
            {
              top:40,
              bottom:'50%',
              right:10,
              width:'45%',
              backgroundColor: '#fff',
              containLabel: true
            },
            {
              top: "55%",
              bottom: "20",
              width:'45%',
              right:10,
              backgroundColor: '#fff',
              containLabel: true
            }
          ],

          xAxis: [
            {
              type: 'value',
              boundaryGap: false,
              gridIndex:0,
              splitLine: {
                show: false
              }
            },
            {
              type: 'value',
              boundaryGap: false,
              gridIndex:1,
              splitLine: {
                show: false
              }
            },
            {
              type: 'value',
              boundaryGap: false,
              gridIndex:2,
              splitLine: {
                show: false
              }
            },
            {
              type: 'value',
              boundaryGap: false,
              gridIndex:3,
              splitLine: {
                show: false
              }
            }
          ],
          yAxis: [
            {
              type: 'category',
              data:[],
              gridIndex:0,
              splitLine: {
                show: false
              }
            },
            {
              type: 'category',
              data:[],
              gridIndex:1,
              splitLine: {
                show: false
              }
            },
            {
              type: 'category',
              data:[],
              gridIndex:2,
              splitLine: {
                show: false
              }
            },
            {
              type: 'category',
              data:[],
              gridIndex:3,
              splitLine: {
                show: false
              }
            }
          ],
          series: [
            {
              type: 'bar',
              symbolSize: 6,
              xAxisIndex: 0,
              yAxisIndex: 0,
              data: []
            },
            {
              type: 'bar',
              symbolSize: 6,
              xAxisIndex: 1,
              yAxisIndex: 1,
              data: []
            },
            {
              type: 'bar',
              symbolSize: 6,
              xAxisIndex: 2,
              yAxisIndex: 2,
              data: []
            },
            {
              type: 'bar',
              symbolSize: 6,
              xAxisIndex: 3,
              yAxisIndex: 3,
              data: []
            }]
        });



        this.axios.all([
          this.axios.get('/api/favSong'),
          this.axios.get('/api/favRMSong'),
          this.axios.get('/api/favSinger'),
          this.axios.get('/api/favRMSinger'),
        ])
          .then(this.axios.spread(function (data1, data2,data3,data4) {
            const song1=[];
            const num1=[];
            const song2=[];
            const num2=[];
            const singer1=[];
            const count1=[];
            const singer2=[];
            const count2=[];
            let d1=data1.data;
            let d2=data2.data;
            let d3=data3.data;
            let d4=data4.data;
            for(const key in d1){
              song1.push(key);
              num1.push( d1[key]);
            }
            for(const key in d2){
              song2.push(key);
              num2.push(d2[key]);
            }
            for(const key in d3){
              singer1.push(key);
              count1.push(d3[key]);
            }
            for(const key in d4){
              singer2.push(key);
              count2.push(d4[key]);
            }

            myChart.setOption({
              yAxis:[{
                gridIndex:0,
                data:song1
                //data:['song1','song2']
              },
            {
              gridIndex:1,
              data:song2
            },
            {
              gridIndex:2,
              data:singer1
            },
            {
              gridIndex:3,
              data:singer2
            }],
              series: [
                {
                  xAxisIndex: 0,
                  yAxisIndex: 0,
                  data:num1
                  //data:[12,13]
                },
            {
              xAxisIndex: 1,
              yAxisIndex: 1,
              data:num2
            },
            {
              xAxisIndex: 2,
              yAxisIndex: 2,
              data:count1
            },
            {
              xAxisIndex: 3,
              yAxisIndex: 3,
              data:count2
            }]
            });

          }));

      }
    }
  }
</script>

<style scoped>

</style>
