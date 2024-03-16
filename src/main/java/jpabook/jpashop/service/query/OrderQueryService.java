package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderSearch;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
public class OrderQueryService {

    /*
    * api 로직을 여기로 다 가지고 온다.
    * */
}

/* OSIV (default on)
* OSIV 전략은 트랜잭션 시작처럼 최초 데이터베이스 커넥션 시작 시점부터 API 응답이 끝날 때 까지 영속성 컨텍스트와
* 데이터베이스 커넥션을 유지한다. 그래서 지금까지 View Template이나 API 컨트롤러에서 지연로딩이 가능했다.
* 지연 로딩은 영속성 컨텍스트가 살아있어야 가능하고, 영속성 컨텍스트는 기본적으로 데이터베이스 커넥션을 유지한다. 큰 장점
*  하지만, 이 전략은 너무 오랜시간동안 데이터베이스 커넥션 리소스를 사용하기 때문에 실시간 트래픽이 중요한
* 애플리케이션에서는 커넥션이 모자랄 수 있다 -> 장애
*
* 고객 서비스의 실시간 API는 OISV를 끄고, ADMIN처럼 커넥션을 많이 사용하지 않는 곳에서는 OSIV를 킨다.*/

/* 커멘드와 쿼리 분리
*
* OrderService
* - OrderService: 핵심 비지니스 로직
* - OrderQueryService: 화면이나 API에 맞춘 서비스 (주로 읽기 전용 트랜잭션 사용)
* */