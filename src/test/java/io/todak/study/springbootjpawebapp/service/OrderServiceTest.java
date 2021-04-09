package io.todak.study.springbootjpawebapp.service;

import io.todak.study.springbootjpawebapp.domain.Address;
import io.todak.study.springbootjpawebapp.domain.Member;
import io.todak.study.springbootjpawebapp.domain.Order;
import io.todak.study.springbootjpawebapp.domain.OrderStatus;
import io.todak.study.springbootjpawebapp.domain.item.Book;
import io.todak.study.springbootjpawebapp.domain.item.Item;
import io.todak.study.springbootjpawebapp.exception.NotEnoughStockException;
import io.todak.study.springbootjpawebapp.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @DisplayName("상품주문")
    @Test
    public void orderItem() throws Exception {
        //given
        Member member = createTestMember();

        int bookPrice = 10000;
        int bookQuantity = 10;
        Item book = createTestBook("헤헤", bookPrice, bookQuantity);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(bookPrice * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(bookQuantity - orderCount, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다.");
    }

    @DisplayName("상품주문시 재고수량 초과")
    @Test
    public void orderItem_stock_error() throws Exception {
        //given
        Member testMember = createTestMember();
        Item testBook = createTestBook("헤헤", 10000, 10);

        int orderCount = 11;

        //when
        assertThrows(NotEnoughStockException.class, () -> {
            //then
            orderService.order(testMember.getId(), testBook.getId(), orderCount);
        });

    }

    @DisplayName("주문취소")
    @Test
    public void cancelOrder() throws Exception {
        //given
        Member testMember = createTestMember();
        int originQuantity = 10;
        Item book = createTestBook("헤헤", 10000, originQuantity);

        int orderCount = 2;

        Long orderId = orderService.order(testMember.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는  CANCEL 이다.");
        assertEquals(originQuantity, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");

    }

    private Item createTestBook(String name, int bookPrice, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(bookPrice);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createTestMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "동대문구", "123123"));
        em.persist(member);
        return member;
    }

}