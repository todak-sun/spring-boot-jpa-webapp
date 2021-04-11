package io.todak.study.springbootjpawebapp.service;

import io.todak.study.springbootjpawebapp.domain.Delivery;
import io.todak.study.springbootjpawebapp.domain.Member;
import io.todak.study.springbootjpawebapp.domain.Order;
import io.todak.study.springbootjpawebapp.domain.OrderItem;
import io.todak.study.springbootjpawebapp.domain.item.Item;
import io.todak.study.springbootjpawebapp.repository.ItemRepository;
import io.todak.study.springbootjpawebapp.repository.MemberRepository;
import io.todak.study.springbootjpawebapp.repository.OrderRepository;
import io.todak.study.springbootjpawebapp.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }


    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }


}
