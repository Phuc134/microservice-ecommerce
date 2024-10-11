package org.example.orderservice.enums;

public enum OrderStatus {
    PENDING {
        @Override
        public boolean canChangeTo(OrderStatus newStatus) {
            return newStatus == SHIPPED || newStatus == CANCELLED;
        }
    },
    SHIPPED {
        @Override
        public boolean canChangeTo(OrderStatus newStatus) {
            return newStatus == DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public boolean canChangeTo(OrderStatus newStatus) {
            return false;
        }
    },
    CANCELLED {
        @Override
        public boolean canChangeTo(OrderStatus newStatus) {
            return false;
        }
    };

    public abstract boolean canChangeTo(OrderStatus newStatus);}
