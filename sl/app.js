// app.js
App({
  d: {   
    userId:'10'
  },
  onLaunch: function () {
    //调用API从本地缓存中获取数据
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs);
    //login
    this.getUserInfo();
  },
  getUserInfo:function(cb){
    var that = this
    if(this.globalData.userInfo){
      typeof cb == "function" && cb(this.globalData.userInfo)
    }else{
      //调用登录接口
      wx.login({
        success: function (res) {
        	
          var code = res.code;
          console.log('code='+code);
          //get wx user simple info
          wx.getUserInfo({
            success: function (res) {
              that.globalData.userInfo = res.userInfo;
			  var nickName = res.userInfo.nickName;
              typeof cb == "function" && cb(that.globalData.userInfo);
              //get user sessionKey
              //get sessionKey
              that.getUserSessionKey(code,nickName);
            }
          });
        }
      });
    }
  },
  host:{
    currentHost:'http://123.207.30.64:8080'
  },


  getUserSessionKey:function(code,nickName){
    //用户的订单状态
    var that = this;   
    wx.request({
      url: 'https://www.hxqzsr.club/peakshop/weixin/login.do?code='+code+'&nickName='+nickName,
      method:'get',
      header: {
        'Content-Type':  'application/x-www-form-urlencoded'
      },
      success: function (res) {
        //--init data        
        var data = res.data;
        if(data.status==0){
          wx.showToast({
            title: data.err,
            duration: 2000
          });
          return false;
        }
        
        that.globalData.userInfo['sessionId'] = data.session_key;
        that.globalData.userInfo['openid'] = data.openid;
        // that.d.userId = data.userId;
      },
      fail:function(e){
        wx.showToast({
          title: '网络异常！err:getsessionkeys',
          duration: 2000
        });
      },
    });
  },
  getOrBindTelPhone:function(returnUrl){
    var user = this.globalData.userInfo;
    if(!user.tel){
      wx.navigateTo({
        url: 'pages/binding/binding'
      });
    }
  },

 globalData:{
    userInfo:null
  },

  onPullDownRefresh: function (){
    wx.stopPullDownRefresh();
  }

});





