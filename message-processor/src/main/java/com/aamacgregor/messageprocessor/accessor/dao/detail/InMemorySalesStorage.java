package com.aamacgregor.messageprocessor.accessor.dao.detail;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesDao;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This is simple Sale instance storage backed by a list
 */
@Component
public class InMemorySalesStorage implements ISalesDao {

    private List<Sale> sales = new LinkedList<>();

    @Override
    public void createSale(Sale sale) {
        sales.add(sale);
    }

    @Override
    public Collection<Sale> readSales() {
        return new ArrayList<>(sales);
    }

    @Override
    public void updateSales(Collection<Sale> sales) {
        this.sales = new LinkedList<>(sales);
    }
}
