package com.trendyol.shipment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Basket {


    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        if (this.getProducts().isEmpty()) {
            throw new IllegalStateException("Basket is empty.");
        }

        int productCount = this.getProducts().size();

        if (productCount < 3) {
            return determineLargestShipmentSize();
        }

        Map<ShipmentSize, Long> sizeCounts = this.getProducts().stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.counting()));

        Optional<Map.Entry<ShipmentSize, Long>> maxEntry = sizeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent() && maxEntry.get().getValue() >= 3) {
            ShipmentSize maxShipmentSize = maxEntry.get().getKey();
            return determineSizeOneStepUp(maxShipmentSize);
        } else {
            return determineLargestShipmentSize();
        }
    }

    private ShipmentSize determineSizeOneStepUp(ShipmentSize maxShipmentSize) {
        return switch (maxShipmentSize) {
            case SMALL -> ShipmentSize.MEDIUM;
            case MEDIUM -> ShipmentSize.LARGE;
            case LARGE, X_LARGE -> ShipmentSize.X_LARGE;
            default -> throw new IllegalStateException("Unexpected value: " + maxShipmentSize);
        };
    }

    private ShipmentSize determineLargestShipmentSize() {
        return this.getProducts().stream()
                .map(Product::getSize)
                .max(Comparator.naturalOrder())
                .orElse(ShipmentSize.SMALL);
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
