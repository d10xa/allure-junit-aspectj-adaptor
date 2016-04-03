package ru.d10xa.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import ru.yandex.qatools.allure.junit.AllureRunListener;

@Aspect
public class AllureRunListenerAspect {

    @Pointcut("execution(void org.junit.runners.ParentRunner.run(org.junit.runner.notification.RunNotifier))")
    public void run() {
    }

    @Around("run()")
    public void run(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        RunNotifier notifier = (RunNotifier) args[0];

        notifier.addListener(new AllureRunListener());
        notifier.fireTestRunStarted(Description.createSuiteDescription("Tests"));

        try {
            pjp.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        notifier.fireTestRunFinished(new Result());
    }

}
