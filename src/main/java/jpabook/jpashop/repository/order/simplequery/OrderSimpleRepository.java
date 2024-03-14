package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleRepository {

    private final EntityManager em;

    /*
     * 일반적인 SQL을 사용할 때 처럼 원하는 값을 선택해서 조회
     * 'new' 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
     * SELECT절에서 원하는 데이터를 직접 선택하므로 DB -> 애플리케이션 네트웍 용량 최적화(생각보다 미비)
     * 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점*/
    public List<OrderSimpleQueryDto> findOrderDtos(){
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
/* 레파지터리 분리하는 이유
* 레파지토리는 가급적 순수한 엔티티 조회용으로 써야하기 때문
* 재사용도 어렵고, 뷰에 딱 붙어있기 때문에 별도의 용도로 분리함
* */