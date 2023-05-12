package lk.ijse.posm.model;

import lk.ijse.posm.dto.Mail;
import lk.ijse.posm.dto.PostMan;
import lk.ijse.posm.util.CrudUtil.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MailModel {

    public static String generateNextMailId() throws SQLException {

        String sql = "SELECT mail_id FROM mails ORDER BY mail_id DESC LIMIT 1";

        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()) {
            return splitMailId(resultSet.getString(1));
        }
        return splitMailId(null);
    }

    public static String splitMailId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] strings = currentOrderId.split("mail");
            int id = Integer.parseInt(strings[1]);
            id++;

            return (id<10)?"mail00"+id:(id>=10)?"mail0"+id:"mail"+id;
        }
        return "mail001";
    }

    public static PostMan getPostMan(String senders_address) throws SQLException {

        String sql="SELECT postman_id,employee_name FROM postMan INNER JOIN employee ON postMan.employee_id=employee.employee_id WHERE post_area=?";

        ResultSet resultSet = CrudUtil.execute(sql, senders_address);

        if (resultSet.next()){
            return(new PostMan(resultSet.getString(1),resultSet.getString(2)));
        }

        return new PostMan(null,null);

    }

    public static boolean saveDetails(Mail mail) throws SQLException {

        String sql="INSERT INTO mails VALUES (?,?,?,?,?,?,?,?,?)";

        return CrudUtil.execute(sql,mail.getPostman_id(),
                        mail.getMails_id(),
                        mail.getSenders_name(),
                        mail.getSenders_address(),
                        mail.getSend_date(),
                        mail.getReceivers_name(),
                        mail.getReceivers_address(),
                        null,
                        mail.getType());

    }

    public static void removeData(String mail_id) throws SQLException {

        String sql="DELETE FROM mails WHERE mail_id=?";

        CrudUtil.execute(sql,mail_id);
    }

    public static Mail searchData(String mail_id) throws SQLException {

        String sql="SELECT * FROM mails WHERE mail_id=?";

        ResultSet resultSet = CrudUtil.execute(sql, mail_id);

        if (resultSet.next()){
            return new Mail(resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getString(9));
        }

        return new Mail();
    }

    public static boolean updateDetails(Mail mail) throws SQLException {

        String sql="UPDATE mails SET postman_id=?,senders_name=?,sender_address=?,send_date=?,receiver_name=?,receivers_address=?,receive_date=?,mail_type=? WHERE mail_id=?";

        return CrudUtil.execute(sql,mail.getPostman_id(),
                        mail.getSenders_name(),
                        mail.getSenders_address(),
                        mail.getSend_date(),
                        mail.getReceivers_name(),
                        mail.getReceivers_address(),
                        mail.getReceivers_date(),
                        mail.getType(),
                        mail.getMails_id());
    }

    public static int  getTodayTotalMails() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(mail_id) FROM mails WHERE send_date=CURRENT_DATE() AND mail_type='Local'");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getInternationalMails() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(mail_id) FROM mails WHERE send_date=CURRENT_DATE() AND mail_type='International'");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static ArrayList<String> getMailIds() throws SQLException {
        ArrayList<String> mailIds=new ArrayList<>();
        ResultSet resultSet=CrudUtil.execute("SELECT mail_id FROM mails");
        while (resultSet.next()){
            mailIds.add(resultSet.getString(1));
        }
        return mailIds;
    }

    public static boolean isTodayMailsOk() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(mail_id) FROM mails WHERE send_date=current_date");
        if (resultSet.next()){
            if (resultSet.getInt(1)>0){
                return true;
            }
        }
        return false;
    }
}
