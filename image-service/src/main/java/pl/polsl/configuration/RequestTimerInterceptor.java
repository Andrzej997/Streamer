package pl.polsl.configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RequestTimerInterceptor extends HandlerInterceptorAdapter {

    private static final String REQ_PARAM_TIMING = "timing";

    private Timer timer;
    private MeterRegistry registry;

    @Autowired
    RequestTimerInterceptor(MeterRegistry meterRegistry) {
        this.registry = meterRegistry;

        this.timer = Timer.builder("http.response.time")
                .publishPercentiles(0.5, 0.9, 0.95)
                .publishPercentileHistogram()
                .distributionStatisticExpiry(Duration.ofSeconds(1L))
                .register(this.registry);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(REQ_PARAM_TIMING, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long timingAttr = (Long) request.getAttribute(REQ_PARAM_TIMING);
        long requestTime = System.currentTimeMillis() - timingAttr;
        this.timer.record(requestTime, TimeUnit.MILLISECONDS);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry){
        return new CustomTimedAspect(registry);
    }
}
