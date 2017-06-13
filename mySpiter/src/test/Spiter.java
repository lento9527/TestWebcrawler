package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spiter
{
	static StringBuffer SendGet(String url)
	{
		// ����һ���ַ��������洢��ҳ����
		StringBuffer result = new StringBuffer();
		// ����һ�������ַ�������
		BufferedReader in = null;
		try
		{
			// ��stringת��url����
			URL realUrl = new URL(url);
			// ��ʼ��һ�����ӵ��Ǹ�url������
			URLConnection connection = realUrl.openConnection();
			// ��ʼʵ�ʵ�����
			connection.connect();
			// ��ʼ�� BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			// ������ʱ�洢ץȡ����ÿһ�е�����
			String line;
			while ((line = in.readLine()) != null)
			{
				// ����ץȡ����ÿһ�в�����洢��result����
				result.append(line);
			}
		} catch (Exception e)
		{
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally���ر�������
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			} catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return result;
	}

	static String RegexString(StringBuffer result, String patternStr)
	{
		StringBuffer allCase = new StringBuffer();
		// ����һ����ʽģ�壬����ʹ��������ʽ����������Ҫץ������
		// �൱�����������ƥ��ĵط��ͻ����ȥ
		Pattern pattern = Pattern.compile(patternStr);
		// ����һ��matcher������ƥ��
		Matcher matcher = pattern.matcher(result);
		// ����ҵ���
		while (matcher.find())
		{
			for (int i = 0; i < matcher.groupCount(); i++) {
				allCase.append(matcher.group(i+1)+"\n");
			}
			// ��ӡ�����
		}
		return allCase.toString();
	}

	public static void main(String[] args)
	{
		// ���弴�����ʵ�����
		String url = "http://a.qidian.com/";
		// �������Ӳ���ȡҳ������
		StringBuffer result = SendGet(url);
		// ʹ������ƥ��ͼƬ��src����
		String imgSrc = RegexString(result, "src=\"(.+?)\"");
		// ��ӡ���
		System.out.println(imgSrc);
	}
}