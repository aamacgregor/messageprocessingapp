package com.aamacgregor.messageprocessor.accessor.dao.detail;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesAdjustmentDao;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This is just simple sale adjustment memory storage backed by a list
 */
@Component
public class InMemorySalesAdjustmentStorage implements ISalesAdjustmentDao {

    private final List<SaleValueAdjustment> adjustments = new LinkedList<>();

    @Override
    public void createSalesAdjustment(SaleValueAdjustment sale) {
        adjustments.add(sale);
    }

    @Override
    public Collection<SaleValueAdjustment> readSalesAdjustments() {
        return new ArrayList<>(adjustments);
    }
}
