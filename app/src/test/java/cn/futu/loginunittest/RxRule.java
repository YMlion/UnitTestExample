package cn.futu.loginunittest;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * rxjava rule
 */
public class RxRule implements TestRule
{
    @Override
    public Statement apply(final Statement base, Description description)
    {

        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                final Scheduler scheduler = new Scheduler()
                {
                    @Override
                    public Worker createWorker()
                    {
                        return new ExecutorScheduler.ExecutorWorker(new Executor()
                        {
                            @Override
                            public void execute(Runnable command)
                            {
                                command.run();
                            }
                        });
                    }

                    @Override
                    public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit)
                    {
                        return super.scheduleDirect(run, 0, unit);
                    }
                };
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>()
                {
                    @Override
                    public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception
                    {
                        return scheduler;
                    }
                });
                RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>()
                {
                    @Override
                    public Scheduler apply(Scheduler scheduler) throws Exception
                    {
                        return scheduler;
                    }
                });
                RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>()
                {
                    @Override
                    public Scheduler apply(Callable<Scheduler> schedulerCallable)
                    {
                        return scheduler;
                    }
                });
                RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>()
                {
                    @Override
                    public Scheduler apply(Scheduler scheduler)
                    {
                        return scheduler;
                    }
                });

                base.evaluate(); // 被测方法执行

                RxAndroidPlugins.reset();
                RxJavaPlugins.reset();
            }
        };

    }
}
