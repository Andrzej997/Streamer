package pl.polsl.configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RequestTimerInterceptor extends HandlerInterceptorAdapter {

    private static final String REQ_PARAM_TIMING = "timing";

    private Timer timer;
    private Gauge gauge;
    private MeterRegistry registry;

    @Autowired
    RequestTimerInterceptor(MeterRegistry meterRegistry) {
        this.registry = meterRegistry;

        this.timer = Timer.builder("http.response.time")
                .publishPercentiles(0.5, 0.9, 0.95)
                .percentilePrecision(3)
                .publishPercentileHistogram()
                .distributionStatisticExpiry(Duration.ofSeconds(1L))
                .register(this.registry);

        this.gauge = Gauge.builder("process.cpu.load", this, RequestTimerInterceptor::getProcessCpuLoad)
                .baseUnit("%")
                .description("CPU Load")
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

    public Double getProcessCpuLoad() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});

            return Optional.ofNullable(list)
                    .map(l -> l.isEmpty() ? null : l)
                    .map(List::iterator)
                    .map(Iterator::next)
                    .map(Attribute.class::cast)
                    .map(Attribute::getValue)
                    .map(Double.class::cast)
                    .orElse(null);

        } catch (Exception ex) {
            return null;
        }
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry){
        return new CustomTimedAspect(registry);
    }
}
