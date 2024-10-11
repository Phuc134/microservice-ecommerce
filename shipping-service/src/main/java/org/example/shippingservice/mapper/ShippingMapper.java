package org.example.shippingservice.mapper;

import org.example.shippingservice.dto.request.ShippingRequest;
import org.example.shippingservice.dto.response.ShippingResponse;
import org.example.shippingservice.entity.Shipping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingMapper {
    Shipping toShipping(ShippingRequest request);
    ShippingResponse toShippingResponse(Shipping shipping);
}
