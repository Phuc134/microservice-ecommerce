package org.example.cartservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cartservice.dto.request.CartRequest;
import org.example.cartservice.dto.response.CartResponse;
import org.example.cartservice.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable String cartId, @PathVariable String productId) {
        cartService.removeFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCart(@RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(request));
    }

}
