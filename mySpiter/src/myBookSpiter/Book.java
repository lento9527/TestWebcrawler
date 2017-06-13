package myBookSpiter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Book {
	
    // 地址
    private static String HeadURL = "http://www.uuxs.net/book/31/31540/10027705.html";
    // 获取标题正则
    private static final String IMGURL_REG = "<h1 *id=\"BookTitle\">.*</h1>";
    // 获取正文正则 
    private static final String IMGSRC_REG = "<div *id=\"BookText\".*</div>";
    // 获取下页正则 
    private static final String NEXT_PAGE = "<a *id=\"book-next\".*下一章";
    
    public static void main(String[] args) {
    	//开始时间
    	Date begindate = new Date();
    	StringBuffer myBook = null;
    	String title = null;
    	String book = null;
    	Book cm=new Book();
        try {
        	//获得html文本内容
        	String HTML = cm.getHtml(HeadURL);
        	//获取文本名称
        	String bookName = cm.getBookTitle(HTML,"<a *href=\"/book.*</a>");
        	//获取下一页链接
        	do{
            	//每页开始时间
                Date newDate = new Date();
        		//重新获取内容
        		HTML = cm.getHtml(HeadURL);
                //获取标题
                title = cm.getBookTitle(HTML,IMGURL_REG);
                System.out.println(title);
                //获取正文
                book = cm.getBook(HTML);
                //拼接标题和正文
                myBook = new StringBuffer(title+"\n");
                myBook.append(book+"\n\n");
                //将内容写入文件
                cm.write(myBook,bookName);
                //每页结束时间
                Date overdate = new Date();
                double time = overdate.getTime() - newDate.getTime();
                System.out.println("单页耗时：" + time / 1000 + "s");
                
        	} while(cm.getNextPage(HTML));
        }catch (Exception e){
        	e.printStackTrace();
            System.out.println("发生错误");
        }
        Date overdate = new Date();
        double time = overdate.getTime() - begindate.getTime();
        System.out.println("总耗时：" + time / 1000 + "s");
    }

	//获取下一页的链接
	private boolean getNextPage(String html) {
		Matcher matcher=Pattern.compile(NEXT_PAGE).matcher(html);
        matcher.find();
    	String listBookTitle=matcher.group();
     	listBookTitle = listBookTitle.substring(listBookTitle.indexOf("f")+3,
     			listBookTitle.indexOf("\">"));
     	HeadURL = HeadURL.substring(0, HeadURL.lastIndexOf("/")+1)+listBookTitle;
    	if(!listBookTitle.equals("index.html")){
    		return true;
    	}
		return false;
	}

	//获取HTML内容
    private String getHtml(String url)throws Exception{
        URL url1=new URL(url);
        URLConnection connection=url1.openConnection();
        InputStream in=connection.getInputStream();
        InputStreamReader isr=new InputStreamReader(in);
        BufferedReader br=new BufferedReader(isr);
        String line;
        StringBuffer sb=new StringBuffer();
        while((line=br.readLine())!=null){
            sb.append(line,0,line.length());
            sb.append('\n');
        }
        br.close();
        isr.close();
        in.close();
        return sb.toString();
    }
    
    //获取所需文本内容
    private String getBookTitle(String html,String regex) {
        Matcher matcher=Pattern.compile(regex).matcher(html);
        matcher.find();
    	String listBookTitle=matcher.group();
    	listBookTitle = listBookTitle.substring(listBookTitle.indexOf(">")+1,
     			listBookTitle.indexOf("</"));
    	return listBookTitle;
    }
    
    //获取正文
    private String getBook(String html) {
    	Matcher matcher=Pattern.compile(IMGSRC_REG).matcher(html);
    	matcher.find();
     	String listBookTitle=matcher.group();
     	listBookTitle = listBookTitle.substring(listBookTitle.indexOf(">")+1,
     			listBookTitle.indexOf("</div>"));
     	listBookTitle = listBookTitle.replaceAll("<br />", "\n");
     	listBookTitle = listBookTitle.replaceAll("&nbsp;", " ");
     	return listBookTitle;
    }
    
    
   private void write(StringBuffer myBook, String bookName) throws IOException {
	   FileOutputStream fo = new FileOutputStream(new File("src/Download/"+bookName+".txt"),true);
	   fo.write(myBook.toString().getBytes());
	   fo.close();
	}


}
