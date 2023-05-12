package lk.ijse.posm.model;

import lk.ijse.posm.db.DbConnection;
import lk.ijse.posm.dto.CuriorMail;
import lk.ijse.posm.dto.Customer;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuriorDetailsModel {

    public static boolean save(Customer customer, CuriorMail curiorMail) throws SQLException {
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
                boolean isSavedParcel = CuriorModel.saveParcel(curiorMail,customer.getCustomer_id());
                boolean isSavedParcelDetails=false;

                if (isSavedParcel){
                   isSavedParcelDetails=CuriorModel.saveParcelWeightDetails(curiorMail.getMail_id());
                }

                if (isSavedParcelDetails) {
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

    public static CuriorMail getAllCuriorData(String mailId) throws SQLException {

        ResultSet resultSet=CrudUtil.execute("SELECT ems_parcel.mail_id,type,send_date,senders_name,sender_address,receiver_name,receivers_contact_no,receivers_address,SUM(parcel_order_details.weight),ems_parcel.price,employee_name,customer_id FROM ems_parcel INNER JOIN parcel_order_details ON ems_parcel.mail_id=parcel_order_details.mail_id INNER JOIN postMan ON postMan.postman_id=ems_parcel.postman_id INNER JOIN employee ON employee.employee_id=postMan.employee_id WHERE ems_parcel.mail_id=?",mailId);

        if (resultSet.next()){
            return new CuriorMail(resultSet.getString(1),
                    resultSet.getString(2),resultSet.getString(3),
                    resultSet.getString(4),resultSet.getString(5),
                    resultSet.getString(6),resultSet.getString(7),
                    resultSet.getString(8),resultSet.getDouble(9),
                    resultSet.getDouble(10),resultSet.getString(11),resultSet.getString(12));
        }

        return new CuriorMail();
    }

    public static Customer getCustomerDetails(String customerId) throws SQLException {

       ResultSet resultSet= CrudUtil.execute("SELECT customer_id,customer_name,customer_contact FROM customer WHERE customer_id=?",customerId);

       if (resultSet.next()){
           return new Customer(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
       }

       return new Customer();
    }

    public static boolean remove(String mailId) throws SQLException {

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isRemoveParcelOrder=CuriorModel.removePlaceOrder(mailId);

            if (isRemoveParcelOrder) {

                boolean isRemoveParcel=CuriorModel.removeParcel(mailId);

                if (isRemoveParcel){
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

    public static boolean update(Customer customer, CuriorMail curiorMail) throws SQLException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isRemoveParcelOrder=CuriorModel.removePlaceOrder(curiorMail.getMail_id());

            if (isRemoveParcelOrder) {

                boolean isRemoveParcel = CuriorModel.removeParcel(curiorMail.getMail_id());

                if (isRemoveParcel) {
                    boolean isSavedCustomer = false;

                    if (customer.getCustomer_id().equals(CustomerModel.generateNextCustId())) {
                        isSavedCustomer = CustomerModel.newCustomerSave(customer);
                    } else {
                        isSavedCustomer = CustomerModel.customerUpdate(customer);
                    }

                    if (isSavedCustomer) {
                        boolean isSavedParcel = CuriorModel.saveParcel(curiorMail, customer.getCustomer_id());
                        boolean isSavedParcelDetails = false;

                        if (isSavedParcel) {
                            isSavedParcelDetails = CuriorModel.saveParcelWeightDetails(curiorMail.getMail_id());
                        }

                        if (isSavedParcelDetails) {
                            con.commit();
                            return true;
                        }
                    }
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
