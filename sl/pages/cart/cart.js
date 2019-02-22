// pages/cart/cart.js
var app = getApp();
Page({
  data: {
    cartlist: [],
    totalPrice: 0,
    totalCount: 0,
    isAll: false
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    this.updateData();
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  onPullDownRefresh: function (e) {
    this.updateData();
    setTimeout(function () {
      console.log("stopPull")
      wx.stopPullDownRefresh();
    }, 2000)
  },
  updateData: function () {
  	var userId = app.d.userId;
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 300000
    })
    var that = this;
    wx.request({
      url: app.host.currentHost +'/peakshop/cart/getlist.do?userId='+userId,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        that.setData({
          cartlist: res.data.cartlist
        })
        that.checkIsAll()
      },
      fail: function () {
        setTimeout(function () {
          wx.showToast({
            title: "加载失败"
          })
        }, 100)
      },
      complete: function () {
        wx.hideToast()
      }
    })
  },
  changeMode: function (e) {
    var id = e.target.dataset.id;
    var cartlist = this.data.cartlist;
    console.log(id)
    for (var i = 0; i < cartlist.length; i++) {
      if (cartlist[i].cart.id == id) {
        cartlist[i].cart.mode = cartlist[i].cart.mode == 0 ? 1 : 0;
        break;
      }
    }
    this.setData({
      cartlist: cartlist
    })
  }
  ,
  checkItem: function (e) {
    var id = e.target.dataset.id;
    var checked = e.detail.value;
    console.log(id)
    var cartlist = this.data.cartlist;
    for (var i = 0; i < cartlist.length; i++) {
      if (cartlist[i].cart.id == id) {
        cartlist[i].cart.checked = checked;
        console.log(cartlist[i])
        break;
      }
    }
    this.setData({
      cartlist: cartlist
    })
    this.calcateTotal()
    this.checkIsAll()
  },
  checkAll: function (e) {
    var checked = e.detail.value;
    var cartlist = this.data.cartlist;
    for (var i = 0; i < cartlist.length; i++) {
      cartlist[i].cart.checked = checked;
    }
    this.setData({
      cartlist: cartlist
    })
    this.calcateTotal()
  },
  calcateTotal: function () {
    var cartlist = this.data.cartlist;
    var totalPrice = 0;
    for (var i = 0; i < cartlist.length; i++) {
      if (cartlist[i].cart.checked) {
        totalPrice += cartlist[i].product.price * cartlist[i].cart.number;
      }
    }
    console.log(totalPrice)
    this.setData({
      totalPrice: totalPrice
    })
  },
  checkIsAll: function () {
    var cartlist = this.data.cartlist;
    var isAll = cartlist.length!=0?true:false;
    for (var i = 0; i < cartlist.length; i++) {
      if (cartlist[i].cart.checked == false) {
        isAll = false;
        break;
      }
    }
    this.setData({
      isAll: isAll
    })
  },
  updateNum: function (id, num) {
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 300000
    })
    var that = this;
    wx.request({
      url: app.host.currentHost +'/peakshop/cart/changeNum.do?id=' + id + '&num=' + num,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        console.log(res.data.flag)
        if (res.data.flag) {
          setTimeout(function () {
            wx.showToast({
              title: "成功",
              duration: 1500
            })
          }, 100)
        }
      },
      fail: function () {
        setTimeout(function () {
          wx.showToast({
            title: "操作失败"
          })
        }, 100)
      },
      complete: function () {
        wx.hideToast()
      }
    })
    this.calcateTotal()
  },
  addNum: function (e) {
    var id = e.target.dataset.id;
    var cartlist = this.data.cartlist;
    var tc = e.detail.value;
    console.log(id)
    for (var i = 0; i < cartlist.length; i++) {
      if (cartlist[i].cart.id == id) {
        if (cartlist[i].cart.number < cartlist[i].store.store) {
          cartlist[i].cart.number = cartlist[i].cart.number + 1;
          this.updateNum(id, cartlist[i].cart.number);
        } else {
          wx.showToast({
            title: "不能再加了"
          })
        }
        cartlist[i].cart.number - 1;
        break;
      }
    }
    this.setData({
      cartlist: cartlist
    })
  },
  delNum: function (e) {
    var id = e.target.dataset.id;
    var cartlist = this.data.cartlist;
    var tc = e.detail.value;
    console.log(id)
    for (var i = 0; i < cartlist.length; i++) {
      if (cartlist[i].cart.id == id) {
        if (cartlist[i].store.store > 1) {
          cartlist[i].cart.number = cartlist[i].cart.number - 1;
          this.updateNum(id, cartlist[i].cart.number);
        } else {
          wx.showToast({
            title: "不能再减了"
          })
        }
        cartlist[i].cart.number - 1;
        break;
      }
    }
    this.setData({
      cartlist: cartlist
    })
  },
  delItem: function (e) {
    var that = this;
    wx.showModal({
      title: "警告",
      content: "是否从购物车中移除此宝贝?",
      success: function (res) {
        if (res.confirm) {
          var id = e.target.dataset.id;
          var cartlist = that.data.cartlist;
          var tc = e.detail.value;
          console.log(id)
          for (var i = 0; i < cartlist.length; i++) {
            if (cartlist[i].cart.id == id) {
              that.delCart(id);
              break;
            }
          }
        }
      }
    })
  },
  submitCart: function (e) {
  	 // 初始化toastStr字符串
     var toastStr = '';
     // 遍历取出已勾选的cid
     for (var i = 0; i < this.data.cartlist.length; i++) {
     if (this.data.cartlist[i].cart.checked) {
       toastStr += this.data.cartlist[i].cart.id;
       toastStr += ',';
     }
   } 
   if (toastStr==''){
     wx.showToast({
       title: '请选择要结算的商品！',
       duration: 2000
     });
     return false;
   }
        	console.log('toastStr='+toastStr);
   //存回data
   wx.navigateTo({
     url: '../order/pay?cartId=' + toastStr,
   })

  },
  isAllOk: true,
  updateCheck: function (id) {
    var that = this;
    wx.request({
      url: app.host.currentHost +'/peakshop/cart/check.do?id=' + id,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        if (res.data.flag) {

        } else {
          that.isAllOk = false;
        }
      },
      fail: function () {
        that.isAllOk = false;
      },
      complete: function () {
        wx.hideToast()
      }
    })
  },
  delCart: function (id) {
  	var userId = app.d.userId;
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 300000
    })
    var that = this;
    wx.request({
      url: app.host.currentHost +'/peakshop/cart/del.do?id=' + id+'&userId='+userId,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        console.log(res.data)
        if (res.data.flag) {
          setTimeout(function () {
            wx.showToast({
              title: "成功",
              duration: 1500
            })
            that.calcateTotal()
          }, 100),
          that.setData({
          cartlist: res.data.cartlist
        })          
        }
      },
      fail: function () {
        setTimeout(function () {
          wx.showToast({
            title: "操作失败"
          })
        }, 100)
      },
      complete: function () {
        wx.hideToast()
      }
    })
  },
  navigateToGoodView :function(e){
    var id=e.currentTarget.dataset.id;
    console.log("NavigateToGoodView--> id:",id)
    wx.navigateTo({
      url: '../index/good/detail/detail?id='+id
    })
  }
})