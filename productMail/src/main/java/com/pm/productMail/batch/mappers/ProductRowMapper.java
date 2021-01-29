package com.pm.productMail.batch.mappers;

import com.pm.productMail.dao.ClientRepository;
import com.pm.productMail.dao.ProductRepository;
import com.pm.productMail.entities.Client;
import com.pm.productMail.entities.Product;
import com.pm.productMail.entities.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product mapRow(ResultSet rs, int i) throws SQLException {
        Product product = new Product();
        Client client = new Client();

        product.setId(rs.getLong("id"));
        client.setId(rs.getLong("client_id"));
        product.setClient(client);
        product.setState (rs.getString("state"));
        product.setSerialNumber(rs.getString("serial_number"));
        product.setExpirationDate(rs.getDate("expiration_date"));
        product.setName(rs.getString("name"));

        return product;
    }
}
