// pages/index/product/detail/detail.js
var app = getApp();
Page({
  data: {
    product: {},
    id: 1,
    colorId: 1,
    sizeId: 1,
    store: 0,
    colorIndex: 0,
    sizeIndex: 0,
    showModalStatus: false,
    buynum:1
    
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options) {
      if (options.id) {
        console.log(options.id)
        this.setData({
          id: options.id
        })
      }
    }
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    var id = this.data.id;
    console.log("onShow --> id:", id);
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 390000
    })
    var that = this;
    wx.request({
      url: 'https://www.hxqzsr.club/peakshop/detail/get.do?id=' + id,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        console.log(res.data);
        that.setData({
          product: res.data,
          colorId: res.data.productColors[0].colorId,
          sizeId: res.data.productSizes[0].sizeId,
          store: res.data.store
          
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
  // 弹窗
  setModalStatus: function (e) {
  	
  	console.log("弹窗",e.currentTarget.dataset.status);
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })

    this.animation = animation
    animation.translateY(300).step();
    
    this.setData({
      animationData: animation.export()
    })

    if (e.currentTarget.dataset.status == 1) {

      this.setData(
        {
          showModalStatus: true
        }
      );
    }
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation
      })
      if (e.currentTarget.dataset.status == 0) {
        this.setData(
          {
            showModalStatus: false
          }
        );
      }
    }.bind(this), 200)
  },
  addToCart: function (e) {
    var productId = this.data.product.id;
    var colorId = this.data.colorId;
    var sizeId = this.data.sizeId;
    var userId = app.d.userId;
    console.log("onShow --> id:", productId);
    wx.showToast({
      title: "Loading...",
      icon: "loading",
      duration: 390000
    })
    var that = this;
    wx.request({
      url: 'https://www.hxqzsr.club/peakshop/detail/addToCart.do?productId=' + productId + '&colorId=' + colorId +'&sizeId='+sizeId+'&userId='+userId,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        var data = res.data;
        if (res.data.flag) {
        	var ptype = e.currentTarget.dataset.type;
        	if(ptype == 'buynow'){
            wx.redirectTo({
              url: '../../../order/pay?cartId='+data.cartId
            });
            return;
          }else{
           wx.showToast({
                title: '添加成功！',
                duration: 1500
              });
          } 
        } else {
          setTimeout(function () {
            wx.showToast({
              title: data.errMsg,
              duration: 1500
            })
          }, 100)
        }
      },
      fail: function () {
        // fail
        setTimeout(function () {
          wx.showToast({
            title: "操作失败",
            duration: 1500
          })
        }, 100)
      },
    })
  },
    // 加减
  changeNum:function  (e) {
    var that = this;
    var store = that.data.store;
    if (e.target.dataset.alphaBeta == 0) {
        if (this.data.buynum <= 1) {
            buynum:1
        }else{
            this.setData({
                buynum:this.data.buynum - 1
            })
        };
    }else{
    	if(this.data.buynum >= that.data.store){
    		buynum:store
    	}else{
    		this.setData({
            buynum:this.data.buynum + 1
        })
    	}
    };
  },
  
  changeColo: function (e) {
  	this.data.colorId = this.data.product.productColors[e.detail.value].colorId;
  	var index = e.detail.value;
  	 this.setData({
      "colorIndex": index
    })
    var colorId = this.data.colorId;
    var sizeId = this.data.sizeId;
    var productId = this.data.product.id;
    console.log("changeColo--> colorID:", colorId)
    var that = this;
     wx.request({
      url: 'https://www.hxqzsr.club/peakshop/store/getStore.do?colorId=' + colorId+'&sizeId='+sizeId+'&productId='+productId,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        console.log(res.data);
        that.setData({
          store: res.data.store
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
      url: 'https://www.hxqzsr.club/peakshop/detail/get.do?id=' + productId+'&colorId=' + colorId,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        console.log(res.data);
        that.setData({
          product: res.data
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
  changeSize: function (e) {
  	this.data.sizeId = this.data.product.productSizes[e.detail.value].sizeId;
  	var index = e.detail.value;
  	 this.setData({
      "sizeIndex": index
    })
    var colorId = this.data.colorId;
    var sizeId = this.data.sizeId;
    var productId = this.data.product.id;
    console.log("changeSize--> sizeID:", sizeId)
    var that = this;
     wx.request({
      url: 'https://www.hxqzsr.club/peakshop/store/getStore.do?colorId=' + colorId+'&sizeId='+sizeId+'&productId='+productId,
      // data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      // header: {}, // 设置请求的 header
      success: function (res) {
        // success
        console.log(res.data);
        that.setData({
          store: res.data.store
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