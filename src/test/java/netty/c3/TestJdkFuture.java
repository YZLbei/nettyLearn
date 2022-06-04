package netty.c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

@Slf4j
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);

        //2.提交任务
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                sleep(1000);
                return 20;
            }
        });
        
        //3.主线程通过future获取结果
        log.debug("等待结果");
        log.debug("{}",future.get());
        //future.get();

    }
}
