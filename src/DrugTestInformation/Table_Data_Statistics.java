package DrugTestInformation;

public class Table_Data_Statistics {
    
    String record_number, unit_name, f_name, m_name, l_name, birthdate, address, sex, mobile_phone, 
            month, day, year, full_name, transaction_date, purpose;

    public Table_Data_Statistics() {
        
    }

    public Table_Data_Statistics(String record_number, String unit_name, String purpose,
                                String f_name, String m_name, String l_name,
                                String month, String day, String year) {
        this.record_number = record_number;
        this.unit_name = unit_name;
        this.f_name = f_name;
        this.m_name = m_name;
        this.l_name = l_name;
        this.full_name = f_name + " " + m_name + " " + l_name;
        this.month = month;
        this.day = day;
        this.year = year;
        this.transaction_date = month + "/" + day + "/" + year;
        this.purpose = purpose;
    }
    
    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }
    

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getRecord_number() {
        return record_number;
    }

    public void setRecord_number(String record_number) {
        this.record_number = record_number;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }
    
}