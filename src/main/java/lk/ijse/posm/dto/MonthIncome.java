package lk.ijse.posm.dto;

public class MonthIncome {

    private String month;

    private double income;

    public MonthIncome() {
    }

    public MonthIncome(String month, double income) {
        this.month = month;
        this.income = income;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
