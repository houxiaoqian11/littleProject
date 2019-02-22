var app = getApp();
// pages/order/downline.js
Page({
  data: {
    itemData: {},
    userId: 0,
    paytype: 'weixin',//0线下1微信
    remark: '',
    cartId: 0,
    addrId: '',//收货地址//测试--
    btnDisabled: false,
    productData: [],
    address: {},
    total: 0,
    vprice: 0,
    vid: 0,
    addemt: 1,
    vou: []
  },
  onLoad: function (options) {
    var uid = app.d.userId;
    var addrId = options.addrId;
    if (addrId == undefined) {
      addrId = ''
    }

    this.setData({
      cartId: options.cartId,
      userId: uid,
      addrId: addrId
    });
    this.loadProductDetail();
  },
  loadProductDetail: function () {
    var that = this;
    var cartId = that.data.cartId;
    var addrId = that.data.addrId;
    wx.request({
      url: app.host.currentHost +'/peakshop/pay/buy.do?cartIdStr=' + cartId + '&addrId=' + addrId,
      method: 'get',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        //that.initProductData(res.data);
        console.log(res.data.address);
        console.log(res.data.productData);
        console.log(res.data.total);

        var adds = res.data.address;
        if (adds) {
          var addrId = adds.id;
          that.setData({
            address: adds,
            addrId: addrId
          });
        }
        that.setData({
          addemt: 0,
          productData: res.data.productData,
          total: res.data.total
        });
        //endInitData
      },
    });
  },

  remarkInput: function (e) {
    this.setData({
      remark: e.detail.value,
    })
  },

  //选择优惠券
  getvou: function (e) {
    var vid = e.currentTarget.dataset.id;
    var price = e.currentTarget.dataset.price;
    var zprice = this.data.vprice;
    var cprice = parseFloat(zprice) - parseFloat(price);
    this.setData({
      total: cprice,
      vid: vid
    })
  },

  //微信支付
  createProductOrderByWX: function (e) {
    this.setData({
      paytype: 'weixin',
    });

    this.createProductOrder();
  },

  //线下支付
  createProductOrderByXX: function (e) {
    this.setData({
      paytype: 'cash',
    });
    wx.showToast({
      title: "线下支付开通中，敬请期待!",
      duration: 3000
    });
    return false;
    this.createProductOrder();
  },

  //确认订单
  createProductOrder: function () {
    this.setData({
      btnDisabled: false,
    })

    //创建订单
    var that = this;
    var cartId = that.data.cartId;
    var remark = that.data.remark;
    var addrId = that.data.addrId;
    wx.request({
      url: app.host.currentHost +'/peakshop/pay/createOrder.do?cartIdStr=' + cartId + '&remark=' + remark + '&addrId=' + addrId,
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

  //调起微信支付
  wxpay: function (order) {
    wx.request({
      url: app.d.ceshiUrl + '/Api/Wxpay/wxpay',
      data: {
        order_id: order.order_id,
        order_sn: order.order_sn,
        uid: this.data.userId,
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
              }, 2500);
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
      fail: function () {
        // fail
        wx.showToast({
          title: '网络异常！err:wxpay',
          duration: 2000
        });
      }
    })
  },


});