package deu.ex.sevenstars.scheduler;

import deu.ex.sevenstars.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class OrderScheduler {
    private final OrderService orderService;

    //스케줄러를 사용
    @Scheduled(cron = "0 0 14 * * ?")//매일 2시마다 실행
    public void run(){
        log.info("스케줄러가 작동 중입니다.");
        orderService.updateOrderStatus();
    }
}
