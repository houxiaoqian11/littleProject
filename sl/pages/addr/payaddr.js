// pages/addr/addr.js
var app = getApp();
Page({
  data: {
    addrlist: [],
    cartId: 0
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    var uid = app.d.userId;

    this.setData({
      cartId: options.cartId,
      userId: uid
    });
    console.log('gouwuid' + this.data.cartId);
    this.updateAddr();
  },
  updateAddr: function () {
    var that = this;
    var userId = app.d.userId;
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 300000
    })
    wx.request({
      url: 'https://www.hxqzsr.club/peakshop/address/getlist.do?userId=' + userId,
      //data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        console.log('getlist:', res.data)
        for (var i = 0; i < res.data.addrlist.length; i++) {
          var addr = res.data.addrlist[i];
          if (i == res.data.length - 1) {
            addr.isLast = true;
          } else {
            addr.isLast = false;
          }
        }
        that.setData({
          addrlist: res.data.addrlist
        })
      },
      fail: function () {
        // fail
        setTimeout(function () {
          wx.showToast({
            title: "获取地址失败",
            duration: 2000
          })
          // setTimeout(function () {
          //   wx.navigateBack({
          //     delta: 1, // 回退前 delta(默认为1) 页面
          //   })
          // }, 2000)
        }, 100)
      },
      complete: function () {
        // complete
        wx.hideToast();
      }
    })
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    this.updateAddr();
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  onPullDownRefresh: function () {
    var that = this;
    setTimeout(function () {
      that.updateAddr();
      wx.stopPullDownRefresh();
      console.log("stoppull")
    }, 2000)
  },
    navigateToPay: function (e) {
		var id = e.target.dataset.id;
		var cartId = this.data.cartId;
		wx.navigateTo({
      url: '../order/pay?cartId='+cartId+'&addrId='+id
    })
	}
})