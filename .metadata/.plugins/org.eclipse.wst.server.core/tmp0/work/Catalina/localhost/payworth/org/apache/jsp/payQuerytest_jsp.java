/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.84
 * Generated at: 2018-03-21 06:54:01 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Date;
import java.text.*;

public final class payQuerytest_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html lang=\"en\">\r\n");
      out.write("<head>\r\n");
      out.write("<meta name=\"viewport\"\r\n");
      out.write("\tcontent=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\r\n");
      out.write("\r\n");
      out.write("<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\r\n");
      out.write("<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\r\n");
      out.write("<!--[if lt IE 9]>\r\n");
      out.write("    <script src=\"https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js\"></script>\r\n");
      out.write("    <script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\r\n");
      out.write("    <![endif]-->\r\n");
      out.write("<link rel=\"stylesheet\"\r\n");
      out.write("\thref=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\"\r\n");
      out.write("\tintegrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\"\r\n");
      out.write("\tcrossorigin=\"anonymous\">\r\n");
      out.write("\r\n");
      out.write("<style media=\"screen\">\r\n");
      out.write("/* Sticky footer styles\r\n");
      out.write("        -------------------------------------------------- */\r\n");
      out.write("html {\r\n");
      out.write("\tposition: relative;\r\n");
      out.write("\tmin-height: 100%;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("body {\r\n");
      out.write("\t/* Margin bottom by footer height */\r\n");
      out.write("\tmargin-bottom: 150px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".footer {\r\n");
      out.write("\tposition: absolute;\r\n");
      out.write("\tbottom: 0;\r\n");
      out.write("\twidth: 100%;\r\n");
      out.write("\t/* Set the fixed height of the footer here */\r\n");
      out.write("\theight: 60px;\r\n");
      out.write("\tbackground-color: #f5f5f5;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("/* Custom page CSS\r\n");
      out.write("        -------------------------------------------------- */\r\n");
      out.write("/* Not required for template or sticky footer method. */\r\n");
      out.write("body>.container {\r\n");
      out.write("\tpadding: 30px 15px 0;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".container .text-muted {\r\n");
      out.write("\tmargin: 20px 0;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".footer>.container {\r\n");
      out.write("\tpadding-right: 15px;\r\n");
      out.write("\tpadding-left: 15px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("code {\r\n");
      out.write("\tfont-size: 80%;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".nav-pills>li {\r\n");
      out.write("\tfont-size: 18px;\r\n");
      out.write("\tline-height: 2;\r\n");
      out.write("}\r\n");
      out.write("</style>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("\t<div class=\"container\">\r\n");
      out.write("\t\t<div>\r\n");
      out.write("\t\t\t<nav class=\"navbar navbar-default navbar-fixed-top\">\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<ul class=\"nav nav-pills\">\r\n");
      out.write("\t\t\t\t\t\t<li role=\"presentation\" class=\"active\"><a href=\"index.html\">API文档</a></li>\r\n");
      out.write("\t\t\t\t\t\t<li role=\"presentation\" class=\"dropdown\"><a\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\"\r\n");
      out.write("\t\t\t\t\t\t\trole=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\"> 接口\r\n");
      out.write("\t\t\t\t\t\t\t\t<span class=\"caret\"></span>\r\n");
      out.write("\t\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t\t\t<ul class=\"dropdown-menu\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<li><a href=\"paytest.jsp\">支付接口</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t<li><a href=\"agentpaytest.jsp\">代付接口</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t<li><a href=\"payQuerytest.jsp\">支付查询</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t\t<li><a href=\"agentpayquerytest.jsp\">代付查询</a></li>\r\n");
      out.write("\t\t\t\t\t\t\t</ul></li>\r\n");
      out.write("\t\t\t\t\t</ul>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</nav>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div class=\"page-header\">\r\n");
      out.write("\t\t\t<h1>支付查询</h1>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div class=\"row main\">\r\n");
      out.write("\t\t\t<div class=\"col-sm-8\">\r\n");
      out.write("\t\t\t\t<form class=\"m-t\" method=\"POST\" action=\"payQuery.jsp\">\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<label for=\"baseUri\">支付查询服务器地址</label>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"input-group\">\r\n");
      out.write("\t\t\t\t\t\t\t<input type=\"text\" class=\"form-control\" id=\"baseUri\"\r\n");
      out.write("\t\t\t\t\t\t\t\tname=\"baseUri\" placeholder=\"https://ebank.ztpo.cn\"\r\n");
      out.write("\t\t\t\t\t\t\t\tvalue=\"https://ebank.ztpo.cn\" aria-describedby=\"basic-addon2\">\r\n");
      out.write("\t\t\t\t\t\t\t<span class=\"input-group-addon\" id=\"basic-addon2\">/payment/v1/order/{merchantId}-{orderNo}</span>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<hr />\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<label for=\"charset\">字符编码</label> <input type=\"text\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"form-control\" id=\"charset\" name=\"charset\"\r\n");
      out.write("\t\t\t\t\t\t\tplaceholder=\"UTF-8\" value=\"UTF-8\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<label for=\"merchantId\">商户号</label> <input type=\"text\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"form-control\" id=\"merchantId\" name=\"merchantId\"\r\n");
      out.write("\t\t\t\t\t\t\tplaceholder=\"100000000002350\" value=\"100000000002350\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<label for=\"orderNo\">订单号</label> <input type=\"text\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"form-control\" id=\"orderNo\" name=\"orderNo\"\r\n");
      out.write("\t\t\t\t\t\t\tplaceholder=\"ORDERNO1511876908109\" value=\"ORDERNO1511876908109\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<label for=\"signType\">签名方式</label> <input type=\"text\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"form-control\" id=\"signType\" name=\"signType\"\r\n");
      out.write("\t\t\t\t\t\t\tplaceholder=\"SHA\" value=\"SHA\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<div class=\"form-group\">\r\n");
      out.write("\t\t\t\t\t\t<label for=\"apiKey\">API Key</label> <input type=\"text\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"form-control\" id=\"apiKey\" name=\"apiKey\"\r\n");
      out.write("\t\t\t\t\t\t\tplaceholder=\"4b4aad5aaa15bfad568c68gg3gc850f6ed46740635715beb12d38ae4494d2e4e\"\r\n");
      out.write("\t\t\t\t\t\t\tvalue=\"4b4aad5aaa15bfad568c68gg3gc850f6ed46740635715beb12d38ae4494d2e4e\">\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t<button type=\"submit\" class=\"btn btn-default\">Submit</button>\r\n");
      out.write("\t\t\t\t</form>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<script\r\n");
      out.write("\t\tsrc=\"http://lib.sinaapp.com/js/jquery/2.2.4/jquery-2.2.4.min.js\"></script>\r\n");
      out.write("\t<script\r\n");
      out.write("\t\tsrc=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"\r\n");
      out.write("\t\tintegrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\"\r\n");
      out.write("\t\tcrossorigin=\"anonymous\"></script>\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
