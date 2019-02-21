package cn.guddqs.peakshop.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表分页。包含list属性。
 */
public class Pagination extends SimplePage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4318423826620929821L;

	public Pagination() {
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 */
	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
		currentPage = 1;
		setTotalRowsAmount(totalCount);
		setCurrentPage(pageNo);
		
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 * @param list
	 *            分页内容
	 */
	public Pagination(int pageNo, int pageSize, int totalCount, List<?> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	/**
	 * 第一条数据位置
	 * 
	 * @return
	 */
	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 当前页的数据
	 */
	private List<?> list;
	
	/**
	 * 当前页的分页样式
	 */
	private List<String> pageView;

	/**
	 * 获得分页内容
	 * 
	 * @return
	 */
	public List<?> getList() {
		return list;
	}

	/**
	 * 设置分页内容
	 * 
	 * @param list
	 */
	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}
	/**
	 * 获得分页样式
	 * 
	 * @return
	 */
	public List<String> getPageView() {
		return pageView;
	}
	/**
	 * 设置分页样式
	 * 
	 * @param list
	 */
	public void setPageView(List<String> pageView) {
		this.pageView = pageView;
	}


	/**
	 * 分页显示样示部分
	 */
	public void pageView(String url,String params){
		
	 	pageView = new ArrayList<String>();
	 	pageView.add("共<var>" + getTotalRowsAmount() + "条</var>");
        if(this.pageNo != 1){
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo=1'\"><font size=2>首页</font></a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.pageNo-1)+"'\"><font size=2>上一页</font></a>");
        }else{
        	pageView.add("<font size=2>首页</font>");
        	pageView.add("<font size=2>上一页</font>");
        }
    
        if(this.getTotalPage() <= 10){
            for (int i = 0; i < this.getTotalPage(); i++) {
                if((i+1)==this.pageNo){
                    pageView.add("<strong>"+this.pageNo+"</strong>");
                    i = i+1;
                    if(this.pageNo==this.getTotalPage())break;
                }
                pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(i+1)+"'\">"+(i+1)+"</a>");
            }
        }else if(this.getTotalPage() <= 20){
            //没有把...加上
            int l = 0;
            int r = 0;
            if(this.pageNo<5){
                l=this.pageNo-1;
                r=10-l-1;
            }else if(this.getTotalPage()-this.pageNo<5){
                r=this.getTotalPage()-this.pageNo;
                l=10-1-r;
            }else{
                l=4;
                r=5;
            }
            int tmp =  this.pageNo-l;
            for (int i = tmp; i < tmp+10; i++) {
                if(i==this.pageNo){
                    pageView.add("<strong>"+this.pageNo+"</strong>");
                    i = i+1;
                    if(this.pageNo==this.getTotalPage()) break;
                }
                pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(i)+"'\">"+(i)+"</a>");
            }
                
        }else if(this.pageNo<7){
            for (int i = 0; i < 8; i++) {
                if(i+1==this.pageNo){
                    pageView.add("<strong>"+this.pageNo+"</strong>");
                    i = i+1;
                }
                pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(i+1)+"'\">"+(i+1)+"</a>");
            }
            pageView.add("...");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.getTotalPage()-1)+"'\">"+(this.getTotalPage()-1)+"</a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.getTotalPage())+"'\">"+(this.getTotalPage())+"</a>");
        }else if(this.pageNo>this.getTotalPage()-6){
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(1)+"'\">"+(1)+"</a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(2)+"'\">"+(2)+"</a>");
            pageView.add("...");
            for (int i = this.getTotalPage()-8; i <this.getTotalPage() ; i++) {
                if(i+1==this.pageNo){
                    pageView.add("<strong>"+this.pageNo+"</strong>");
                    i = i+1;
                    if(this.pageNo==this.getTotalPage()) break;
                }
                pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(i+1)+"'\">"+(i+1)+"</a>");
            }
        }else{
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(1)+"'\">"+(1)+"</a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(2)+"'\">"+(2)+"</a>");
            pageView.add("...");
            
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.pageNo-2)+"'\">"+(this.pageNo-2)+"</a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.pageNo-1)+"'\">"+(this.pageNo-1)+"</a>");
            pageView.add("<strong>"+this.pageNo+"</strong>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.pageNo+1)+"'\">"+(this.pageNo+1)+"</a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.pageNo+2)+"'\">"+(this.pageNo+2)+"</a>");
            
            pageView.add("...");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.getTotalPage()-1)+"'\">"+(this.getTotalPage()-1)+"</a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.getTotalPage())+"'\">"+(this.getTotalPage())+"</a>");
        }    
        if(this.pageNo != this.getTotalPage()){
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+(this.pageNo+1)+"'\"><font size=2>下一页</font></a>");
            pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?" + params + "&pageNo="+this.getTotalPage()+"'\"><font size=2>尾页</font></a>");
        } else{
        	pageView.add("<font size=2>下一页</font>");
        	pageView.add("<font size=2>尾页</font>");
        }
        pageView.add("共<var>" + getTotalPage() + "</var>页 到第<input type='text' id='PAGENO'  size='3' />页 <input type='button' id='skip' class='hand btn60x20' value='确定' onclick=\"javascript:window.location.href = '" + url + "?" + params + "&pageNo=' + $('#PAGENO').val() \"/>");
	}
	
	int totalRowsAmount;
    boolean rowsAmountSet;
    int pageSize;
    int currentPage;
    int nextPage;
    int previousPage;
    int totalPages;
    boolean hasNext;
    boolean hasPrevious;
    int pageStartRow;
    int pageEndRow;
    
    public void setTotalRowsAmount(int i)
    {
        if(!rowsAmountSet)
        {
            totalRowsAmount = i;
            totalPages = totalRowsAmount / pageSize + (totalRowsAmount % pageSize <= 0 ? 0 : 1);
            setCurrentPage(currentPage);
            rowsAmountSet = true;
        }
    }

    public void setCurrentPage(int i)
    {
        if(i > totalPages)
            currentPage = totalPages;
        else
            currentPage = i;
        nextPage = currentPage + 1;
        previousPage = currentPage - 1;
        if(currentPage * pageSize < totalRowsAmount)
        {
            pageEndRow = currentPage * pageSize;
            pageStartRow = (pageEndRow - pageSize) + 1;
        } else
        {
            pageEndRow = totalRowsAmount;
            pageStartRow = pageSize * (totalPages - 1) + 1;
        }
        if(nextPage > totalPages)
            hasNext = false;
        else
            hasNext = true;
        if(previousPage <= 0)
            hasPrevious = false;
        else
            hasPrevious = true;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public boolean isHasNext()
    {
        return hasNext;
    }

    public boolean isHasPrevious()
    {
        return hasPrevious;
    }

    public int getNextPage()
    {
        return nextPage;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public int getPreviousPage()
    {
        return previousPage;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public int getTotalRowsAmount()
    {
        return totalRowsAmount;
    }

    public void setHasNext(boolean b)
    {
        hasNext = b;
    }

    public void setHasPrevious(boolean b)
    {
        hasPrevious = b;
    }

    public void setNextPage(int i)
    {
        nextPage = i;
    }

    public void setPageSize(int i)
    {
        if(i > 0)
            pageSize = i;
    }

    public void setPreviousPage(int i)
    {
        previousPage = i;
    }

    public void setTotalPages(int i)
    {
        totalPages = i;
    }

    public int getPageEndRow()
    {
        return pageEndRow;
    }

    public int getPageStartRow()
    {
        return pageStartRow;
    }
    
}
