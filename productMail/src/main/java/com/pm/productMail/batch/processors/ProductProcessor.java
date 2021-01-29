package com.pm.productMail.batch.processors;

import com.pm.productMail.entities.Product;
import com.pm.productMail.entities.State;
import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String date1 = format.format(item.getExpirationDate());
        String date2 = format.format(new Date());


        boolean isExpired = (date1.compareTo(date2) == 0) ? true : false;

        if(isExpired){
            item.setState((State.EXPIRED).toString());
            return item;
        }
        return null;
    }
}
