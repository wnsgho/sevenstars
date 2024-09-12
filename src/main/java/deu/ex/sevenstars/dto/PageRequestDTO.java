package deu.ex.sevenstars.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "페이징 관련 데이터 전송 객체")
public class PageRequestDTO {


    @Builder.Default
    @Min(1)
    private int page = 1;


    @Builder.Default
    @Min(10)
    @Max(100)
    private int size = 20;

    public Pageable getPageable(Sort sort){
        int pageNum = page < 0 ? 1 : page - 1;
        int sizeNum = size <= 10 ? 10 : size ;

        return PageRequest.of(pageNum, sizeNum, sort);
    }
}
