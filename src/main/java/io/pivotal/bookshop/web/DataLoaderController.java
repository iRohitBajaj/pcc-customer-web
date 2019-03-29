package io.pivotal.bookshop.web;

import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.Customer;
import io.pivotal.bookshop.services.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the main controller providing minimal REST endpoints for verifying correct database initialization
 * and setup.
 */
@RestController
public class DataLoaderController {
    private Logger logger = LoggerFactory.getLogger("CustomerController");
    @Autowired
    private DataService service;


    @GetMapping("/isInitialized")
    public String isDbInitialized() {
        if (service.isDbInitialized() ) {
            return "Database has been initialized";
        } else {
            return "Database was not initialized!!";
        }
    }

    @GetMapping("/customer/{customerId}")
    public Customer getCustomer(@PathVariable Integer customerId) {
        return service.getCustomerById(customerId);
    }

    @GetMapping("/book/{itemNumber}")
    public BookMaster getBook(@PathVariable Integer itemNumber) {
        return service.getBookById(itemNumber);
    }
}
