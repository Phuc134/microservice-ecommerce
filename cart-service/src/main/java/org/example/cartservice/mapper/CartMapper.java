package org.example.cartservice.mapper;

import org.example.cartservice.dto.request.CartRequest;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.CartResponse;
import org.example.cartservice.entity.Cart;
import org.example.cartservice.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartItem toCartItem(CartRequest request);

    CartResponse toCartResponse(Cart cart, List<CartItem> cartItems);
}
