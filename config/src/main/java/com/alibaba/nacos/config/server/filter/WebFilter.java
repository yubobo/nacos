/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.nacos.config.server.filter;

import static com.alibaba.nacos.config.server.utils.LogUtil.defaultLog;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.core.annotation.Order;
import com.alibaba.nacos.config.server.constant.Constants;

/**
 * encode filter
 * 
 * @author Nacos
 *
 */
@Order(1)
@javax.servlet.annotation.WebFilter(filterName = "webFilter", urlPatterns = "/*")
public class WebFilter implements Filter {
    
    static private String webRootPath;
    
    static public String rootPath() {
        return webRootPath;
    }
    
    /**
     *  方便测试
     * @param path web path
     */
    static public void setWebRootPath(String path) {
        webRootPath = path;
    }
    
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext ctx = filterConfig.getServletContext();
		setWebRootPath(ctx.getRealPath("/"));
	}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(Constants.ENCODE);
        response.setContentType("application/json;charset="+Constants.ENCODE);
        
		try {
			chain.doFilter(request, response);
		} catch (IOException ioe) {
			defaultLog.debug("Filter catch exception, " + ioe.toString(), ioe);
			throw ioe;
		} catch (ServletException se) {
			defaultLog.debug("Filter catch exception, " + se.toString(), se);
			throw se;
		}
    }

    
    @Override
    public void destroy() {
    }

}
