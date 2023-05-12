package lk.ijse.posm.model;

import lk.ijse.posm.Launcher;
import lk.ijse.posm.dto.*;
import lk.ijse.posm.dto.Tm.CuriorTM;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class CuriorModel {

    private static int count;

    private static ArrayList<Parcel_Order> parcel_order_list;

    public static String generateNextMailId() throws SQLException {
        String sql = "SELECT mail_id FROM ems_parcel ORDER BY mail_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitMailId(resultSet.getString(1));
        }
        return splitMailId(null);
    }

    public static String splitMailId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] strings = currentOrderId.split("Cmail");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "Cmail00"+id;
        }
        return "Cmail001";
    }

    public static PostMan getPostMan(String receivers_address) throws SQLException {
        String sql="SELECT postman_id,employee_name FROM postMan INNER JOIN employee ON postMan.employee_id=employee.employee_id WHERE post_area=?";

        ResultSet resultSet = CrudUtil.execute(sql, receivers_address);

        if (resultSet.next()){
            return(new PostMan(resultSet.getString(1),resultSet.getString(2)));
        }

        return new PostMan(null,null);
    }

    public static double calculatePrice(double weight,String type) throws SQLException {

        String sql="SELECT weight_id,unit_price,weight_range FROM ems_parcel_service WHERE type=?";

        ResultSet resultSet = CrudUtil.execute(sql,type);

        ArrayList<Curior> ems=new ArrayList<Curior>();

        while (resultSet.next()){
            Curior curior=new Curior(resultSet.getString(1),resultSet.getDouble(2),resultSet.getString(3));
            if(curior.getWeight_id().equals("weight000")){
                curior.setRange("0");
            }
            ems.add(curior);
        }

        return calculateTotal(ems,weight);
    }

    private static double calculateTotal(ArrayList<Curior> ems, double weight) {

        double total=0.0;

        parcel_order_list=new ArrayList<Parcel_Order>();

        L1:for (Curior curior : ems){

            String weightRange=curior.getRange();

            if (!weightRange.equals("0")) {

                String[] range = curior.getRange().split("-");

                double weight2=Double.parseDouble(range[1]);
                double weight1=Double.parseDouble(range[0]);

                if (weight-(weight2-weight1)>=0){

                    total+=curior.getUnit_price()*(weight2-weight1);
                    parcel_order_list.add(new Parcel_Order(curior.getWeight_id(),curior.getUnit_price(),weight2-weight1));
                    weight-=(weight2-weight1);

                }else if (weight-(weight2-weight1)<0){

                    total+=curior.getUnit_price()*weight;
                    parcel_order_list.add(new Parcel_Order(curior.getWeight_id(),curior.getUnit_price(),weight));
                    weight-=weight;
                    break L1;

                }
            }else{
                total+=curior.getUnit_price();
                parcel_order_list.add(new Parcel_Order(curior.getWeight_id(),curior.getUnit_price(),0));
            }
        }

        return total;
    }

    public static boolean saveParcel(CuriorMail curiorMail,String customerId) throws SQLException {

        return saveParcelDetails(curiorMail,customerId);
    }

    public static boolean saveParcelWeightDetails(String mail_Id) throws SQLException {
        for (Parcel_Order parcel_order : parcel_order_list) {
            if(!saveParcelWeightDetails(parcel_order,mail_Id)) {
                return false;
            }
        }
        return true;
    }

    private static boolean saveParcelWeightDetails(Parcel_Order parcel_order, String mail_id) throws SQLException {

        return CrudUtil.execute("INSERT INTO parcel_order_details VALUES (?,?,?,?)",
                mail_id,parcel_order.getWeight_Id(),parcel_order.getPrice_per_Weight_range(),parcel_order.getWeight());
    }

    private static boolean saveParcelDetails(CuriorMail curiorMail,String customer_id) throws SQLException {

        boolean isSaved=CrudUtil.execute("INSERT INTO ems_parcel VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                curiorMail.getDelivery_postman_id(),
                customer_id,
                Launcher.userId,
                curiorMail.getMail_id(),
                curiorMail.getSend_Name(),
                curiorMail.getSend_Address(),
                curiorMail.getSend_date(),
                curiorMail.getReceiver_Name(),
                curiorMail.getReceiver_Address(),
                "2000-01-01",
                curiorMail.getReceiver_Telephone_No(),
                curiorMail.getMail_type(),
                curiorMail.getWeight(),
                3,
                curiorMail.getPrice()
                );

        return isSaved;
    }

    public static ArrayList<CuriorTM> getAll() throws SQLException {

        ArrayList<CuriorTM> curiorTMArrayList=new ArrayList<>();

        ResultSet resultSet=CrudUtil.execute("SELECT * FROM ems_parcel_service");

        while (resultSet.next()){
            curiorTMArrayList.add(new CuriorTM(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(4)));
        }

        return curiorTMArrayList;
    }

    public static String getweightId() throws SQLException {
        String sql = "SELECT weight_id FROM ems_parcel_service ORDER BY weight_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitWeightId(resultSet.getString(1));
        }
        return splitWeightId(null);
    }

    public static String splitWeightId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] strings = currentOrderId.split("weight");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"weight00"+id:(id>10)?"weight0"+id:"weight"+id;
        }
        return "weight001";
    }

    public static boolean saveEmsParcelDetails(CuriorTM curiorTM) throws SQLException {

        return CrudUtil.execute("INSERT INTO ems_parcel_service VALUES (?,?,?,?)",
                        curiorTM.getWeightId(),curiorTM.getDescription(),curiorTM.getType(),
                        curiorTM.getPrice());
    }

    public static boolean update(CuriorTM curiorTM) throws SQLException {

        return CrudUtil.execute("UPDATE ems_parcel_service SET unit_price=? WHERE weight_id=?",curiorTM.getPrice(),curiorTM.getWeightId());
    }

    public static boolean removePlaceOrder(String mailId) throws SQLException {
        return CrudUtil.execute("DELETE FROM parcel_order_details WHERE mail_id=?",mailId);
    }

    public static boolean removeParcel(String mailId) throws SQLException {
        return CrudUtil.execute("DELETE FROM ems_parcel WHERE mail_id=?",mailId);
    }

    public static int getParcelCount() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(mail_id) FROM ems_parcel WHERE send_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static ArrayList<MonthIncome> getParcelTotalIncome() throws SQLException {
        ArrayList<MonthIncome> parcelDetailsIncome=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i < monthList.size(); i++) {
            ResultSet resultSet=CrudUtil.execute("SELECT SUM(price) FROM ems_parcel WHERE MONTH(send_date)=?",i+1);
            if (resultSet.next()){
                parcelDetailsIncome.add(new MonthIncome(monthList.get(i),resultSet.getDouble(1)));
            }
        }
        return parcelDetailsIncome;
    }

    public static ArrayList<String> getMailIds() throws SQLException {
        ArrayList<String> mailIds=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT mail_id FROM ems_parcel");
        while (resultSet.next()){
            mailIds.add(resultSet.getString(1));
        }
        return mailIds;
    }

    public static boolean isTodayMailsOk() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(mail_id) FROM ems_parcel WHERE send_date=current_date");
        if (resultSet.next()){
            if (resultSet.getInt(1)>0){
                return true;
            }
        }
        return false;
    }
}
