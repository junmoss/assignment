package com.service.order.service.query;

import com.service.order.dto.order.OrderDto;
import com.service.order.entity.order.Order;
import com.service.order.entity.order.OrderInfo;
import com.service.order.entity.product.Product;
import com.service.order.error.exception.query.OrderServiceException;
import com.service.order.error.exception.query.ProductServiceException;
import com.service.order.input.order.OrderInput;
import com.service.order.input.order.OrderUpdateInput;
import com.service.order.input.product.ProductInput;
import com.service.order.repository.order.OrderRepository;
import com.service.order.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public long order(OrderInput orderInput, List<Product> products) {
        Order order = saveOrder(Order.from(orderInput));

        for (Product product : products) {
            product.setOrder(order);
        }

        saveOrderProduct(products);
        return order.getId();
    }

    public Order saveOrder(Order order) {
        try {
            return orderRepository.save(order);
        } catch (RuntimeException e) {
            throw new OrderServiceException("주문 데이터 저장에 실패하였습니다.");
        }
    }

    public List<Product> saveOrderProduct(List<Product> products) {
        try {
            return productRepository.saveAll(products);
        } catch (RuntimeException e) {
            throw new ProductServiceException("주문상품 데이터 저장에 실패하였습니다.");
        }
    }

    @Transactional
    public long updateOrder(OrderUpdateInput orderUpdateInput) {
        try {
            Order order = findOrderById(orderUpdateInput.getId());
            OrderInfo orderInfo = order.getOrderInfo();
            orderInfo.setName(orderUpdateInput.getName());
            orderInfo.setAddress(orderUpdateInput.getAddress());
            orderInfo.setRequest(orderUpdateInput.getRequest());
            return order.getId();
        } catch (RuntimeException e) {
            throw new OrderServiceException("주문 데이터 수정에 실패하였습니다.");
        }
    }

    @Transactional
    public void cancelOrder(Long orderId, List<Long> productIds) {
        deleteOrder(orderId);
        deleteOrderProduct(productIds);
    }

    public void deleteOrder(long orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (RuntimeException e) {
            throw new OrderServiceException("주문 데이터 삭제에 실패하였습니다.");
        }
    }

    public void deleteOrderProduct(List<Long> productIds) {
        try {
            productRepository.deleteAllById(productIds);
        } catch (RuntimeException e) {
            throw new ProductServiceException("주문상품 데이터 삭제에 실패하였습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderServiceException("주문 정보를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<ProductInput> findOrderProductInputsById(Long orderId) {
        return findOrderById(orderId).getProductList().stream().map(ProductInput::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDto findOrderDtoById(Long orderId) {
        return OrderDto.from(findOrderById(orderId));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllOrderDto() {
        return orderRepository.findAll().stream().map(OrderDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findOrderPaginationById(Long orderId, Pageable pageable) {
        return orderRepository.findOrderByOrderId(orderId, pageable).getContent().stream().map(OrderDto::from).collect(Collectors.toList());
    }
}
