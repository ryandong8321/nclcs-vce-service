package org.ryan.nclcs.vce.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ryan.nclcs.vce.annotation.SystemLogIsCheck;
import org.ryan.nclcs.vce.entity.SysLogs;
import org.ryan.nclcs.vce.entity.SysUsers;
import org.ryan.nclcs.vce.service.log.ISysLogManagementService;
import org.ryan.nclcs.vce.service.sysusers.ISysUsersManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class SystemLogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
	
	@Autowired
	private ISysUsersManagementService sysUsersManagementService;
	
	@Autowired
	private ISysLogManagementService sysLogManagementService;
	
	@Pointcut("@annotation(org.ryan.nclcs.vce.annotation.SystemLogIsCheck)")
	public void controllerAspect(){
	}
	
//	@Before("controllerAspect()")
//	public void doBefore(JoinPoint joinPoint) {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); 
//		HttpSession session = request.getSession();
//		
//		if (session.getAttribute("u_id")==null||session.getAttribute("u_id").equals("")){
//			logger.info("SYSTEM CHECK USER DO NOT LOGIN...");
//			try {
//				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse().sendRedirect("/nclcs-vce-service/index.jsp");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	@After("controllerAspect()")
	public void doAfter(JoinPoint joinPoint){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); 
		HttpSession session = request.getSession();
		try {
			Integer userId=Integer.parseInt(""+session.getAttribute("u_id"));
			SysUsers user=sysUsersManagementService.get(userId);
			String strIP=request.getRemoteAddr();
			logger.info("SYSTEM LOG RECORD { REQUEST_METHOD ["+joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName()+"]"
					+ ",METHOD_DESCRIPTION ["+getControllerMethodDescription(joinPoint)+"],OPERATION_USER ["+user.getUserName()+"],OPERATION_IP ["+strIP+"]");
			
			SysLogs log=new SysLogs();
			log.setOperationIp(strIP);
			log.setOperationMethod(joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName());
			log.setOperationUser(user.getUserName());
			log.setOperationDescription(getControllerMethodDescription(joinPoint));
			sysLogManagementService.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					description = method.getAnnotation(SystemLogIsCheck.class).description();
					break;
				}
			}
		}
		return description;
	}

}
