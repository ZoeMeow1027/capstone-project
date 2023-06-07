package io.zoemeow.pbl6.phonestoremanager.utils;

import java.util.Arrays;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductCategory;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductManufacturer;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;

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
        if (checkMethod.compareTo("update") == 0 && product.getId() == null) {
            throw new Exception("Must submit ID value in update item!");
        }
    }

    public static void validateUser(User user, String checkMethod) throws Exception {
        // Check method is working in "update" action only.
        if (checkMethod.compareTo("update") == 0) {
            if (user.getId() == null) {
                throw new Exception("ID is missing!");
            }
        }
        // Check username
        if (user.getUsername() == null) {
            throw new Exception("Username is missing!");
        } else if (user.getUsername().length() < 6) {
            throw new Exception("Username must be at least 5 characters!");
        }
        // Check password
        if (user.getPassword() == null) {
            throw new Exception("Password is missing!");
        } else if (user.getPassword().length() < 6) {
            throw new Exception("Password must be at least 8 characters!");
        }
        // Check name
        if (user.getName() == null) {
            throw new Exception("Name is missing!");
        } if (user.getName().trim().length() < 6) {
            throw new Exception("Name must be at least 6 characters!");
        }
        // Check phone number
        if (user.getPhone() != null) {
            if (!user.getPhone().startsWith("0") || !(user.getPhone().length() >= 10 && user.getPhone().length() <= 11)) {
                throw new Exception("Invalid Phone value!");
            }
        }
        // Check usertype
        if (user.getUserType() == null) {
            throw new Exception("UserType is missing!");
        } else if (!Arrays.asList(0, 1, 2).contains(user.getUserType())) {
            throw new Exception("Invalid UserType value!");
        }
    }

    public static void validateProductCategory(ProductCategory productCategory, String checkMethod) throws Exception {
        // Check id if update
        if (checkMethod.compareTo("update") == 0) {
            // Check id
            if (productCategory.getId() == null) {
                throw new Exception("ID is missing!");
            }
        }
        // Check name
        if (productCategory.getName() == null) {
            throw new Exception("Name is missing!");
        } if (productCategory.getName().trim().length() < 2) {
            throw new Exception("Name must be at least 2 characters!");
        }
    }

    public static void validateProductManufacturer(ProductManufacturer productManufacturer, String checkMethod) throws Exception {
        // Check id if update
        if (checkMethod.compareTo("update") == 0) {
            // Check id
            if (productManufacturer.getId() == null) {
                throw new Exception("ID is missing!");
            }
        }
        // Check name
        if (productManufacturer.getName() == null) {
            throw new Exception("Name is missing!");
        } if (productManufacturer.getName().trim().length() < 2) {
            throw new Exception("Name must be at least 2 characters!");
        }
    }
}
