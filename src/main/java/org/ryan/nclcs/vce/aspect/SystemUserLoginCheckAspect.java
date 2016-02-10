package org.ryan.nclcs.vce.aspect;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.ryan.nclcs.vce.entity.SysRoles;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class SystemUserLoginCheckAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemUserLoginCheckAspect.class);
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@Pointcut("@annotation(org.ryan.nclcs.vce.annotation.SystemUserLoginIsCheck)")
	public void controllerAspect(){
	}
	
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); 
		HttpSession session = request.getSession();
		if (session.getAttribute("u_id")==null||session.getAttribute("u_id").equals("")){
			logger.info("SYSTEM CHECK USER DO NOT LOGIN...");
			try {
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse().sendRedirect("/nclcs-vce-service/index.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			SysUsers user=sysUsersManagementService.get(Integer.parseInt(""+session.getAttribute("u_id")));
			List<SysRoles> lstRoles=user.getSysRoles();
			int role=-1;
			for (SysRoles sysRole:lstRoles){
				role=sysRole.getId();
				if (role==1||role==2){
					break;
				}
			}
			request.setAttribute("_sys_privilege", role);
		}
	}
}
