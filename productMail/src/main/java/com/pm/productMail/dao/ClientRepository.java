package com.pm.productMail.dao;

import com.pm.productMail.entities.Client;
import com.pm.productMail.entities.EmailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
