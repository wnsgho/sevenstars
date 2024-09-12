package deu.ex.sevenstars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing        //엔티티 시간 자동 처리 설정
@EnableScheduling		//스케줄러 처리
public class SevenstarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SevenstarsApplication.class, args);
	}

}
