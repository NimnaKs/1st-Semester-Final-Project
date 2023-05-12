package lk.ijse.posm.model;

import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.Customer;
import lk.ijse.posm.dto.Payment;

import java.sql.Connection;
import java.sql.SQLException;

public class PaymentDetailsModel {
    public static boolean save(Customer customer, Payment payment) throws SQLException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isSavedCustomer = false;

            if (customer.getCustomer_id().equals(CustomerModel.generateNextCustId())) {
                isSavedCustomer = CustomerModel.newCustomerSave(customer);
            } else {
                isSavedCustomer = CustomerModel.customerUpdate(customer);
            }

            if (isSavedCustomer) {
                boolean isSavedPayment = PaymentModel.savePayment(payment);

                if (isSavedPayment) {
                    con.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }

    }

    public static boolean update(Customer customer, Payment payment) throws SQLException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isUpdatedCustomer = false;

            isUpdatedCustomer = CustomerModel.customerUpdate(customer);

            if (isUpdatedCustomer) {
                boolean isUpdatedPayment = PaymentModel.updatePayment(payment);

                if (isUpdatedPayment) {
                    con.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
