package org.example.shippingservice.enums;

public enum ShippingStatus {
    SHIPPED {
        @Override
        public boolean canChangeTo(ShippingStatus newStatus) {
            return newStatus == IN_TRANSIT || newStatus == DELIVERED;
        }
    },
    IN_TRANSIT {
        @Override
        public boolean canChangeTo(ShippingStatus newStatus) {
            return newStatus == DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public boolean canChangeTo(ShippingStatus newStatus) {
            return false;
        }
    };

    public abstract boolean canChangeTo(ShippingStatus newStatus);
}
