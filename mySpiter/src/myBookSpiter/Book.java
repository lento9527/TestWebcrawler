package myBookSpiter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Book {
	
    // ��ַ
    private static String HeadURL = "http://www.uuxs.net/book/31/31540/10027705.html";
    // ��ȡ��������
    private static final String IMGURL_REG = "<h1 *id=\"BookTitle\">.*</h1>";
    // ��ȡ�������� 
    private static final String IMGSRC_REG = "<div *id=\"BookText\".*</div>";
    // ��ȡ��ҳ���� 
    private static final String NEXT_PAGE = "<a *id=\"book-next\".*��һ��";
    
    public static void main(String[] args) {
    	//��ʼʱ��
    	Date begindate = new Date();
    	StringBuffer myBook = null;
    	String title = null;
    	String book = null;
    	Book cm=new Book();
        try {
        	//���html�ı�����
        	String HTML = cm.getHtml(HeadURL);
        	//��ȡ�ı�����
        	String bookName = cm.getBookTitle(HTML,"<a *href=\"/book.*</a>");
        	//��ȡ��һҳ����
        	do{
            	//ÿҳ��ʼʱ��
                Date newDate = new Date();
        		//���»�ȡ����
        		HTML = cm.getHtml(HeadURL);
                //��ȡ����
                title = cm.getBookTitle(HTML,IMGURL_REG);
                System.out.println(title);
                //��ȡ����
                book = cm.getBook(HTML);
                //ƴ�ӱ��������
                myBook = new StringBuffer(title+"\n");
                myBook.append(book+"\n\n");
                //������д���ļ�
                cm.write(myBook,bookName);
                //ÿҳ����ʱ��
                Date overdate = new Date();
                double time = overdate.getTime() - newDate.getTime();
                System.out.println("��ҳ��ʱ��" + time / 1000 + "s");
                
        	} while(cm.getNextPage(HTML));
        }catch (Exception e){
        	e.printStackTrace();
            System.out.println("��������");
        }
        Date overdate = new Date();
        double time = overdate.getTime() - begindate.getTime();
        System.out.println("�ܺ�ʱ��" + time / 1000 + "s");
    }

	//��ȡ��һҳ������
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

	//��ȡHTML����
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
    
    //��ȡ�����ı�����
    private String getBookTitle(String html,String regex) {
        Matcher matcher=Pattern.compile(regex).matcher(html);
        matcher.find();
    	String listBookTitle=matcher.group();
    	listBookTitle = listBookTitle.substring(listBookTitle.indexOf(">")+1,
     			listBookTitle.indexOf("</"));
    	return listBookTitle;
    }
    
    //��ȡ����
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
