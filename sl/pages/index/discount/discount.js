// pages/index/hot/hot.js
Page({
  data: {
    productlist: [],
    orderBy: "id"
  },
  onLoad: function (options) {

  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {

    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 390000
    })
    var that = this;  
    
    wx.request({
      url: 'https://www.hxqzsr.club/peakshop/discount/getlist.do',
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        console.log(res.data);
        that.setData({
          productlist: res.data
        })
      },
      fail: function () {
        // fail
        setTimeout(function () {
          wx.showToast({
            title: "加载失败",
            duration: 1500
          })
        }, 100)
      },
      complete: function () {
        // complete
        wx.hideToast();
      }
    })
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  navigateToProduct: function (e) {
    var id = e.currentTarget.dataset.id;
    console.log("navigateToProduct --> id:", id);
    wx.navigateTo({
      url: '../good/detail/detail?id=' + id
    })
  },

  searchProduct: function (e) {
    var keyword = e.detail.value.keyword;
    console.log("searchProduct--> keyword:" + keyword);
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 390000
    })
    var that = this;
    wx.request({
      url: 'https://www.hxqzsr.club/peakshop/discount/getlist.do?keyword=' + keyword,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        console.log(res.data);
        that.setData({
          productlist: res.data
        })
      },
      fail: function () {
        // fail
        setTimeout(function () {
          wx.showToast({
            title: "加载失败",
            duration: 1500
          })
        }, 100)
      },
      complete: function () {
        // complete
        wx.hideToast();
      }
    })
  }
})