var app = getApp();
// pages/order/detail.js
var util = require("../../utils/util.js");
Page({
  data:{
    orderId:0,
    orderData:{},
    orderTime:"",
  },
  onLoad:function(options){
    this.setData({
      orderId: options.orderId,
    })
    this.loadProductDetail();
  },
  loadProductDetail:function(){
    var that = this;
    var orderId = that.data.orderId;
    wx.request({
      url: app.host.currentHost +'/peakshop/order/getOrder.do?orderId='+orderId,
      method:'get',
      header: {
        'Content-Type':  'application/x-www-form-urlencoded'
      },
      success: function (res) {

        var status = res.data.status;
        if(status==1){
          var ord = res.data.orderInfo;
          console.log(util.formatTime(ord.startTime,'Y/M/D h:m:s'));
          var orderTime = util.formatTime(ord.startTime,'Y/M/D h:m:s');
          that.setData({
            orderData: ord,
            orderTime:orderTime
          });
        }else{
          wx.showToast({
            title: res.data.err,
            duration: 2000
          });
        }
      },
      fail: function () {
          // fail
          wx.showToast({
            title: '网络异常！',
            duration: 2000
          });
      }
    });
  },

})