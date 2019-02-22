//index.js
//获取应用实例
var app = getApp()
Page({
  data: {
    typeList: [],
    newProduct:[],
    hotProduct:[],
    discountProduct:[],
    groupbuyProduct:[]
  },
    onShow: function () {
    // 页面显示
      console.log("从服务端获取类型");
    var that = this;
    wx.request({
      url: app.host.currentHost +'/peakshop/index/getlist.do',
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        that.setData({
          typeList: res.data.typeList,
          newProduct: res.data.newProduct,
          hotProduct: res.data.hotProduct,
          discountProduct: res.data.discountProduct,
          groupbuyProduct: res.data.groupbuyProduct
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
  onLoad: function () {
    console.log('onLoad')

  },
  onShareAppMessage: function (e) {
    return {
      title: "PEAK专卖",
      desc: "我的第一个小程序"
    }
  },
  navigateToShop: function (e) {
    var id = e.currentTarget.dataset.id;
    console.log("navigateToShop--> ID:", id)
    wx.navigateTo({
      url: './good/good?typeId=' + id
    })
  },
  navigateToProduct: function (e) {
    var id = e.currentTarget.dataset.id;
    console.log("navigateToProduct--> ID:", id)
    wx.navigateTo({
      url: './good/detail/detail?id=' + id
    })
  },
  showWX: function (e) {
  	wx.showModal({
         title: "微信: 133 0123 5050",
         duration: 3000
     })
  },
  goto_counter:function(){
      wx.navigateTo({url:"./video"});
  },

})
