package app;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RestrictAspect {
    @Before("@annotation(app.Restrict) && execution(public * *(..))")
    public void restrict(final JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Restrict annotation = signature.getMethod().getAnnotation(Restrict.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        LoggedUser u= (LoggedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (annotation.admin() && !u.getRole().equals("Administrator")) {
            throw new ForbiddenException("Need admin access");
        }

        if (annotation.localOnly()
                && !request.getRemoteAddr().equals("127.0.0.1")
                && !request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
            throw new ForbiddenException("Only possible from localhost");
        }
    }


}
