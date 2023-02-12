package model;

/**
 * This class is inherited from the Part class and specifically models parts that are outsourced
 */
public class OutsourcedPart extends Part{
    private String CompanyName;

    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyname) {
        super(id, name, price, stock, min, max);
        this.CompanyName = companyname;
    }

    /**
     * @return the Company Name
     */
    public String getCompanyName() {

        return CompanyName;
    }

    /**
     * @param companyName the Company Name
     */
    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}