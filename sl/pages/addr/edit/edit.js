// pages/addr/edit/edit.js
var app = getApp();
Page({
  data: {
    addr:{}
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options.id) {
      this.setData({
        "addr.id": options.id
      })
    }
    var id = this.data.addr.id;
    if (id) {
      var that = this;
      wx.showToast({
        title: "Loading...",
        icon: "loading",
        duration: 900000
      })
      wx.request({
        url: 'https://www.zrshxq.club/peakshop/address/get.do?id=' + id,
        // data: {},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        // header: {}, // 设置请求的 header
        success: function (res) {
          // success
          console.log(res.data.userId);
          var addr = res.data;
          that.setData({
            addr: addr
          })
        },
        fail: function () {
          // fail
          wx.showModal({
            content: "数据加载失败,请检查网络后重试!",
            showCancel: false,
            success: function (res) {
              wx.navigateBack({
                delta: 1, // 回退前 delta(默认为1) 页面
              })
            }
          })
        },
        complete: function () {
          // complete
          wx.hideToast();
        }
      })
    }else{
      wx.setNavigationBarTitle({
        title: '新增地址'
      })
    }
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
   	var userId = app.d.userId;
     this.setData({
        "addr.userId": userId
      })
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  saveAddr: function (e) {
    var addr = e.detail.value;
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 900000
    })
    console.log("userid="+this.data.addr.userId);
    wx.request({
      url: 'https://www.zrshxq.club/peakshop/address/saveOrUpdate.do',
      data: addr,
      method: 'POST', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        setTimeout(function () {
          if (res.data.flag == 'true') {
            wx.showToast({
              title: "保存成功",
              duration: 1500
            })
            setTimeout(function () {
              wx.navigateBack({
                delta: 1, // 回退前 delta(默认为1) 页面
              })
            }, 2000)
          } else {
            wx.showToast({
              title: "操作失败",
              duration: 1500
            })
          }
        }, 100)
      },
      fail: function () {
        // fail
        wx.showModal({
          content: "操作失败,未保存成功,请检查网络后重试",
          showCancel: false
        })
      },
      complete: function () {
        // complete
        wx.hideToast();
      }
    })
  },
  delAddr: function (e) {
    var id = e.target.dataset.id;
    console.log(id)
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 900000
    })
    wx.request({
      url: 'https://www.zrshxq.club/peakshop/address/del.do?id=' + id,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        setTimeout(function () {
          wx.showToast({
            title: "删除成功",
            duration: 1500
          })
          setTimeout(function () {
            wx.navigateBack({
              delta: 1, // 回退前 delta(默认为1) 页面
            })
          }, 2000)
        }, 100)
      },
      fail: function () {
        wx.showModal({
          content: "操作失败,未删除该地址,请检查网络后重试",
          showCancel: false
        })
      },
      complete: function () {
        // complete
        wx.hideToast();
      }
    })
  }
})