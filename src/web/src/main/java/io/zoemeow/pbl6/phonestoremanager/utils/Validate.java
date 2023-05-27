package io.zoemeow.pbl6.phonestoremanager.utils;

import java.util.Arrays;

import io.zoemeow.pbl6.phonestoremanager.model.Product;
import io.zoemeow.pbl6.phonestoremanager.model.User;

public class Validate {
    public static void validateProduct(Product product, String checkMethod) throws Exception {
        if (product.getInventoryCount() < 0) {
            throw new Exception("Invalid Inventory count value!");
        }
        if (product.getWarrantyMonth() < 0) {
            throw new Exception("Invalid Warranty month value!");
        }
        if (product.getPrice() < 0) {
            throw new Exception("Invalid Price value!");
        }
        if (checkMethod.compareTo("edit") == 0 && product.getId() == null) {
            throw new Exception("Must submit ID value in update item!");
        }
    }

    public static void validateUser(User user, String checkMethod) throws Exception {
        if (checkMethod.compareTo("add") == 0) {
            if (user.getUsername() == null || user.getPassword() == null || user.getName() == null || user.getUserType() == null) {
                throw new Exception("Missing parameters!");
            }
            if (user.getPassword().length() < 6) {
                throw new Exception("Password must be 6 characters or more!");
            }
            if (user.getUsername().length() < 5) {
                throw new Exception("Username must be 5 characters or more!");
            }
        } else if (checkMethod.compareTo("update") == 0) {
            if (user.getUsername() == null || user.getId() == null || user.getName() == null || user.getUserType() == null) {
                throw new Exception("Missing parameters!");
            }
        } else {
            throw new Exception("Invalid 'action' value (from server!)");
        }
        if (user.getName().trim().length() < 3) {
            throw new Exception("Name must be 6 characters or more!");
        }
        if (user.getPhone() != null) {
            if (!user.getPhone().startsWith("0") || !(user.getPhone().length() >= 10 && user.getPhone().length() <= 11)) {
                throw new Exception("Invalid 'phone' value!");
            }
        }
        if (!Arrays.asList(0, 1, 2).contains(user.getUserType())) {
            throw new Exception("Invalid 'usertype' value!");
        }
    }
}
