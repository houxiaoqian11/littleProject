// pages/user/dingdan.js
//index.js  
//获取应用实例  
var app = getApp();
Page({  
  data: {  
    winWidth: 0,  
    winHeight: 0,  
    // tab切换  
    currentTab: 0,  
    isStatus:'0',//0待付款，1待发货，2待收货 3已完成
    page:0,
    refundpage:0,
    orderList0:[],
    orderList1:[],
    orderList2:[],
    orderList3:[],
    orderList4:[],
  },  
  onLoad: function(options) {  
    this.initSystemInfo();
    this.setData({
      currentTab: parseInt(options.currentTab),
      isStatus:options.otype
    });
    console.log('currentTab'+this.data.currentTab);

    this.loadOrderList();

  },  
  getOrderStatus:function(){
    return this.data.currentTab == 0 ? 1 : this.data.currentTab == 2 ?2 :this.data.currentTab == 3 ? 3:0;
  },

//取消订单
removeOrder:function(e){
    var that = this;
    var orderId = e.currentTarget.dataset.orderId;
    wx.showModal({
      title: '提示',
      content: '你确定要取消订单吗？',
      success: function(res) {
        res.confirm && wx.request({
          url: 'https://www.hxqzsr.club/peakshop/order/changeStatus.do?orderId='+orderId+'&status='+9,
          method:'get',
          header: {
            'Content-Type':  'application/x-www-form-urlencoded'
          },
          success: function (res) {
            //--init data
            var status = res.data.status;
            if(status == 1){
              wx.showToast({
                title: '操作成功！',
                duration: 2000
              });
              that.loadOrderList();
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

      }
    });
  },
  
  //发起支付
  payOrder:function(e){
	  var that = this;
    var orderId = e.currentTarget.dataset.orderId;
	   wx.request({
        url: 'https://www.hxqzsr.club/peakshop/pay/pay.do?id=' + orderId,
        method: 'get',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success: function (res) {
          //--init data        
          var data = res.data;
          if (data.status == 1) {
            //创建订单成功
            if (data.order.pay_type == 'cash') {
              wx.showToast({
                title: "请自行联系商家进行发货!",
                duration: 3000
              });
              return false;
            }
            if (data.pay_type == 'weixin') {
              //微信支付
              wx.requestPayment({
                timeStamp: data.order.timeStamp,
                nonceStr: data.order.nonceStr,
                package: data.order.package,
                signType: 'MD5',
                paySign: data.order.paySign,
                success: function (res) {
                  wx.showToast({
                    title: "支付成功!",
                    duration: 2000,
                  });
                  setTimeout(function () {
                    wx.navigateTo({
                      url: '../user/dingdan?currentTab=1&otype=1',
                    });
                  }, 2500);
                },
                fail: function (res) {
                  console.log('fail', res);
                  wx.showToast({
                    title: res,
                    duration: 3000
                  })
                }
              })
            }
          } else {
            wx.showToast({
              title: data.errorMsg,
              duration: 2500
            });
          }
        },
        fail: function (e) {
          wx.showToast({
            title: '网络异常！err:createProductOrder',
            duration: 2000
          });
        }
      });
  },

  //确认收货
recOrder:function(e){
    var that = this;
    var orderId = e.currentTarget.dataset.orderId;
    wx.showModal({
      title: '提示',
      content: '你确定已收到宝贝吗？',
      success: function(res) {
        res.confirm && wx.request({
          url: 'https://www.hxqzsr.club/peakshop/order/changeStatus.do?orderId='+orderId+'&status='+3,
          method:'get',
          header: {
            'Content-Type':  'application/x-www-form-urlencoded'
          },
          success: function (res) {
            //--init data
            var status = res.data.status;
            if(status == 1){
              wx.showToast({
                title: '操作成功！',
                duration: 2000
              });
              that.loadOrderList();
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

      }
    });
  },

  loadOrderList: function(){
    var that = this;
    var userId = app.d.userId;
    var status = that.data.isStatus;
    wx.request({
      url: 'https://www.hxqzsr.club/peakshop/order/getList.do?userId='+userId+'&status='+status,
      method:'get',
      header: {
        'Content-Type':  'application/x-www-form-urlencoded'
      },
      success: function (res) {
        //--init data        
        var list = res.data.orderlist;
        switch(that.data.currentTab){
          case 0:
            that.setData({
              orderList0: list,
            });
            break;
          case 1:
            that.setData({
              orderList1: list,
            });
            break;  
          case 2:
            that.setData({
              orderList2: list,
            });
            break;
          case 3:
            that.setData({
              orderList3: list,
            });
            break;
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
  
  // returnProduct:function(){
  // },
  initSystemInfo:function(){
    var that = this;  
  
    wx.getSystemInfo( {
      success: function( res ) {  
        that.setData( {  
          winWidth: res.windowWidth,  
          winHeight: res.windowHeight  
        });  
      }    
    });  
  },
  bindChange: function(e) {  
    var that = this;  
    that.setData( { currentTab: e.detail.current });  
  },  
  swichNav: function(e) {  
    var that = this;  
    if( that.data.currentTab === e.target.dataset.current ) {  
      return false;  
    } else { 
      var current = e.target.dataset.current;
      that.setData({
        currentTab: parseInt(current),
        isStatus: e.target.dataset.otype,
      });

      //没有数据就进行加载
      switch(that.data.currentTab){
          case 0:
            !that.data.orderList0.length && that.loadOrderList();
            break;
          case 1:
            !that.data.orderList1.length && that.loadOrderList();
            break;  
          case 2:
            !that.data.orderList2.length && that.loadOrderList();
            break;
          case 3:
            !that.data.orderList3.length && that.loadOrderList();
            break;
          case 4:
            that.data.orderList4.length = 0;
            that.loadReturnOrderList();
            break;
        }
    };
  },
  /**
   * 微信支付订单
   */
  // payOrderByWechat: function(event){
  //   var orderId = event.currentTarget.dataset.orderId;
  //   this.prePayWechatOrder(orderId);
  //   var successCallback = function(response){
  //     console.log(response);
  //   }
  //   common.doWechatPay("prepayId", successCallback);
  // },

  payOrderByWechat: function (e) {
    var order_id = e.currentTarget.dataset.orderId;
    var order_sn = e.currentTarget.dataset.ordersn;
    if(!order_sn){
      wx.showToast({
        title: "订单异常!",
        duration: 2000,
      });
      return false;
    }
    wx.request({
      url: app.d.ceshiUrl + '/Api/Wxpay/wxpay',
      data: {
        order_id: order_id,
        order_sn: order_sn,
        uid: app.d.userId,
      },
      method: 'POST', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }, // 设置请求的 header
      success: function (res) {
        if (res.data.status == 1) {
          var order = res.data.arr;
          wx.requestPayment({
            timeStamp: order.timeStamp,
            nonceStr: order.nonceStr,
            package: order.package,
            signType: 'MD5',
            paySign: order.paySign,
            success: function (res) {
              wx.showToast({
                title: "支付成功!",
                duration: 2000,
              });
              setTimeout(function () {
                wx.navigateTo({
                  url: '../user/dingdan?currentTab=1&otype=deliver',
                });
              }, 3000);
            },
            fail: function (res) {
              wx.showToast({
                title: res,
                duration: 3000
              })
            }
          })
        } else {
          wx.showToast({
            title: res.data.err,
            duration: 2000
          });
        }
      },
      fail: function (e) {
        // fail
        wx.showToast({
          title: '网络异常！',
          duration: 2000
        });
      }
    })
  },

  /**
   * 调用服务器微信统一下单接口创建一笔微信预订单
   */
//   prePayWechatOrder: function(orderId){
//     var uri = "/ztb/userZBT/GetWxOrder";
//     var method = "post";
//     var dataMap = {
//       SessionId: app.globalData.userInfo.sessionId,
//       OrderNo: orderId
//     }
//     console.log(dataMap);
//     var successCallback = function (response) {
//       console.log(response);
//     };
//     common.sentHttpRequestToServer(uri, dataMap, method, successCallback);
//   }
})