// pages/index/product/product.js
var app = getApp()
Page({
  data: {
    typelist: [],
    productlist: [],
    orderBy: "id",
    typeId: 1
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options) {
      if (options.typeId) {
        this.setData({
          typeId: options.typeId
        })
      }
    }
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    var id=this.data.typeId
    console.log("onShow --> typeId:", id);
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 390000
    })
    var that = this;
    
     wx.request({
      url: app.host.currentHost +'/peakshop/good/getTypeList.do',
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        console.log(res.data);
        that.setData({
          typelist: res.data
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
    
    wx.request({
      url: app.host.currentHost +'/peakshop/good/getProductList.do?typeId=' + id,
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
      url: './detail/detail?id=' + id
    })
  },
  searchProductByTypeId: function (e) {
    var id = e.currentTarget.dataset.id;
    this.setData({
      typeId: id
    })
    console.log("searchProductByTypeId --> id:", id);
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 390000
    })
    var that = this;
    wx.request({
      url: app.host.currentHost +'/peakshop/good/getProductList.do?typeId=' + id,
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
  searchProduct: function (e) {
    var id = this.data.typeId;
    var keyword = e.detail.value.keyword;
    console.log("SearchProduct--> keyword:" + keyword);
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 390000
    })
    var that = this;
    wx.request({
      url: app.host.currentHost +'/peakshop/good/getProductList.do?typeId=' + id + '&keyword=' + keyword,
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