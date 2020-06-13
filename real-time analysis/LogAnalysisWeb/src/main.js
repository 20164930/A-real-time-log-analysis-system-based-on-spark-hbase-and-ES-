// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from "axios"
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import './assets/css/theme-green/index.css'; // 浅绿色主题
import './assets/css/icon.css';
import echarts from 'echarts'
import  'echarts/theme/macarons.js'

Vue.config.productionTip = false;
Vue.prototype.$echarts = echarts;
Vue.prototype.axios = axios;
Vue.use(ElementUI);
Vue.use(echarts);
Vue.prototype.arrSum = function (arr){
  let s = 0;
  for (let i=arr.length-1; i>=0; i--) {
    s += arr[i];
  }
  return s;
};

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
