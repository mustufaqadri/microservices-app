package com.dev.ecommerce.order;

import com.dev.ecommerce.customer.CustomerClient;
import com.dev.ecommerce.exception.BusinessException;
import com.dev.ecommerce.orderline.OrderLineRequest;
import com.dev.ecommerce.orderline.OrderLineService;
import com.dev.ecommerce.product.ProductClient;
import com.dev.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;

    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No Customer exists by ID: " + request.customerId()));

        productClient.purchaseProducts((request.products()));

        var order = orderRepository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // todo start payment process

        // send the order confirmation --> notification microservice

        return null;
    }

    public List<OrderResponse> findAllOrders() {
        return null;
    }

    public OrderResponse findById(Integer orderId) {
        return null;
    }

}
