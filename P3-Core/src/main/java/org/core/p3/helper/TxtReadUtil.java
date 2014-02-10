package org.core.p3.helper;

import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Freemarker工具类
 * 
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 * 
 */
public class TxtReadUtil{

	final static Logger logger = Logger.getLogger(TxtReadUtil.class);
	private static Configuration _tplConfig = new Configuration();
	static{
		_tplConfig.setClassForTemplateLoading(TxtReadUtil.class, "/");
	}

	/**
	 * 解析ftl
	 * @param tplName 模板名
	 * @param encoding 编码
	 * @param paras 参数
	 * @return
	 */
	public static String parseTemplate(String tplName, String encoding,
			Map<String, Object> paras) {
		try {
			StringWriter swriter = new StringWriter();
			Template mytpl = null;
			mytpl = _tplConfig.getTemplate(tplName, encoding);
			mytpl.process(paras, swriter);
			return swriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}
	public static String parseTemplate(String tplName, Map<String, Object> paras) {
		return parseTemplate(tplName, "utf-8", paras);
	}
	
	public static void main(String[] args) {
		String json = "org/core/p3/utils/content-type.json";
		String s  = parseTemplate(json, null);
		System.out.println(s);
	}
}
