package org.core.p3.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 将系统的资源文件提取到代理服务器的主目录
 * @author admin
 * 
 */
public class ResourcesRecoverServlet extends HttpServlet {
	private static final long serialVersionUID = -3917462637788796413L;

	@Override
	public void init(ServletConfig config) {
		try {
			Logger logger = Logger.getLogger(ResourcesRecoverServlet.class);
			
			PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
			Resource[] resources = resourceLoader
					.getResources("classpath*:content/**/*.*");

			//webapp路径
			String path = config.getServletContext()
					.getResource(File.separator).getPath();

			if(logger.isInfoEnabled())
				logger.info("Begin recove resources.....");
			for (Resource resource : resources) {
				
				InputStream is = resource.getInputStream();
				
				StringBuffer filePath = new StringBuffer(resource.getURL()
						.getFile());

				//排除vm模板文件
				if(filePath.lastIndexOf(".vm") == filePath.length() - 3){
					continue;
				}

				//排除文件夹
				if(filePath.lastIndexOf("/") == filePath.length() - 1){
					continue;
				}

				//排除非jar资源
				if(filePath.indexOf("!") == -1){
					continue;
				}
				
				//取得目标路径
				filePath.delete(0, filePath.indexOf("!") + 10).insert(0,path);
				//目标文件
				File file = new File(filePath.toString());
				System.out.println("--------------filePath--------------filePath-----------------filePath--------"+filePath);
				//目标目录
				File dir = new File(filePath.delete(filePath.lastIndexOf(File.separator),filePath.length()).toString());
				dir.mkdirs();
				
				OutputStream os = new FileOutputStream(file);
				
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = is.read(buff, 0, buff.length))) {
					os.write(buff, 0, bytesRead);
				}

				os.flush();
				os.close();
				is.close();

				if(logger.isInfoEnabled())
					logger.info(file.getAbsolutePath());
			}
			if(logger.isInfoEnabled())
				logger.info("Recoved resources. total count :" + resources.length);
		} catch (IOException e) {
			throw new RuntimeException("提取系统资源文件错误："+e.getMessage(),e);
		}
	}
}
