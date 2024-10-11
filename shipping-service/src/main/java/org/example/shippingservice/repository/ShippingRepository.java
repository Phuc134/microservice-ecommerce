package org.example.shippingservice.repository;

import org.example.shippingservice.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, String> {
    Optional<Shipping> findByOrderId(String orderId);
}
