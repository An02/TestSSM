package com.manage.aspect;

import com.alibaba.dubbo.common.json.JSON;
import com.manage.annotation.NoRepeat;
import com.manage.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class LogAdvice {

/*	@DeclareParents(value="com.ctc.ServiceImpl.*+",
			defaultImpl=Adult.class)
	public static AgeGroup ageGroup;*/

    //所有的通知都可以使用这种方式，直接把Pointcut跟Advice连接起来，但是为了更好的理解前文的概念以及图片,这边分开定义。
    //@Before("execution(* com.ctc.ServiceImpl.*.*(..))");
    @Before("com.manage.aspect.UserAspect.addLog()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String argsString = null;
        try {
            argsString = JSON.json(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("切面 before advice 入参： " + argsString);
    }

    @AfterReturning("com.manage.aspect.UserAspect.addLog()")
    public void AfterReturning() {
        System.out.println("切面 after returning advice ");
    }

/*	@AfterThrowing("com.ctc.AspectJ.UserAspect.addLog()")
	public void AfterThrowing(){
		System.out.println("LogAdvice after throwing advice ");
	}

	@After("com.ctc.AspectJ.UserAspect.addLog()")
	public void After(){
		System.out.println("LogAdvice after advice ");
	}*/

    //除了可以通过名字来指向对应的切入点表达式,还可以可以使用'&&', '||' 和 '!'来合并。
    //切入点表达式的 args(user,..) 表示某个与切入表达式匹配的连接点它把User对象作为第一个参数,通过这个语法我们可以在通知中访问到这个User对象。
    // @Around("com.ctc.AspectJ.UserAspect.addLog()&&" +"args(user,..)")
    @Around("com.manage.aspect.UserAspect.addLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("进入  around！");
        System.out.println("代理类："+ joinPoint.getTarget().getClass());
        User obj = (User)joinPoint.proceed();
        Object baseInfo = joinPoint.proceed();
        Map<String,List<String>> map = this.getNoRepeatList(baseInfo);
        System.out.println("around end！");
        return obj;
    }

    /**
     *  获取注解描述以及字段名
     * @param object
     * @return
     */
    public Map<String,List<String>> getNoRepeatList(Object object) {
        Map<String,List<String>> map = new HashMap<String,List<String>>(2);
        List<String> fieldNameList = new ArrayList<>();
        List<String> fieldDescList = new ArrayList<>();
        Class<?> resClass = object.getClass();
        Field[] fields = resClass.getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            NoRepeat noRepeat = field.getAnnotation(NoRepeat.class);
            if (noRepeat != null) {
                fieldDescList.add(noRepeat.desc());
                fieldNameList.add(field.getName());
            }
        }
        if (!CollectionUtils.isEmpty(fieldDescList)) {
            map.put("fieldName",fieldNameList);
            map.put("fieldDesc",fieldDescList);
            return map;
        }
        return null;
    }

}