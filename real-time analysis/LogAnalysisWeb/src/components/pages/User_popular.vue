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
            text: '用户流行',
            textAlign: 'center',
            textStyle: {
              color: '#000000',
              fontSize: 22
            }
          }],
          tooltip: {},
          grid: [{
            backgroundColor: '#fff',
            containLabel: true
          }],
          xAxis: [
            {
              type: 'value',
              boundaryGap: false,
              splitLine: {
                show: false
              }
            }
          ],
          yAxis: [
            {
              type: 'category',
              data:[],
              splitLine: {
                show: false
              }
            }
          ],
          series: [
            {
              type: 'bar',
              symbolSize: 6,
              data: []
            }]
        });



        this.axios
            .get('/api/userSearch')
            .then(re=> {
              let d=re.data;
              const keyword=[];
              const num=[];
              for(const key in d){
                keyword.push(key);
                num.push(d[key]);
              }

              myChart.setOption({
                yAxis:[{
                  data:keyword
                }],
                series:[{
                  data:num
                }]
              })
            });


      }
    }
  }
</script>


<style scoped>

</style>
