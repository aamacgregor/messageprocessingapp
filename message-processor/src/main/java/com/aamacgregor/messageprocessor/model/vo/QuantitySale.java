package com.aamacgregor.messageprocessor.model.vo;

import java.util.Optional;

/**
 * Otherwise known as MessageType2
 *
 * Rational for composition vs inheritance:
 * It could be argued that QuantitySale is a large sale and therefore should inherit from Sale (a sale);
 * however, from a behavioural point of view this would be a poor design. It does not make sense to pass an instance
 * of type QuantitySale as an argument to a process method that has a parameter of type Sale. i.e.
 *
 * Let's say a concrete processor inherits from the following interface:
 *
 *   interface Processor {
 *     boolean process(Sale message);
 *   }
 *
 * If we then do the following:
 *
 *   Processor processor = ...;
 *   QuantitySale message = ...;
 *
 *   processor.process(message);
 *
 * Then the QuantitySale instance would be incorrectly processed as only a single sale would be processed, not the
 * quantity of sales specified within the QuantitySale instance. Therefore since a QuantitySale instance cannot be
 * correctly processed using the Sale API, it turns out that QuantitySale is not a Sale and therefore
 * QuantitySale should not extend Sale. Instead, QuantitySale should comprise Sale and a quantity,
 * hence the use of composition.
 *
 * That said, It could legitimately be argued that a Sale is a QuantitySale with a quantity of 1, making Sale
 * completely redundant, but since the problem specifies that messages can be received via one of three specific
 * message types, I'll just roll with it and keep Sale as a specified type.
 *
 */
public class QuantitySale {

    private Sale sale;
    private Integer quantity;

    /**
     * Constucts a QuantitySale object
     * @param quantity The quantity of Sale to be processed
     * @param sale The Sale instance
     */
    public QuantitySale(Integer quantity, Sale sale) {
        this.sale = sale;
        this.quantity = quantity;
    }

    /**
     * Returns a Sale Optional
     * @return Optional.empty() if sale is null, else an Optional containing a Sale instance
     */
    public Optional<Sale> getSale() {
        return Optional.ofNullable(sale);
    }


    /**
     * Returns an Integer Optional for quantity
     * @return Optional.empty() if quantity is null, else an Optional containing an Integer instance
     */
    public Optional<Integer> getQuantity() {
        return Optional.ofNullable(quantity);
    }
}
