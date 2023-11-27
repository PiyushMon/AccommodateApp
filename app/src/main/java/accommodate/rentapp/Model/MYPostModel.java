package accommodate.rentapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class MYPostModel implements Serializable {


    public static Comparator<MYPostModel> priceComparator = new Comparator<MYPostModel>() {
        @Override
        public int compare(MYPostModel o1, MYPostModel o2) {
            return (Integer.parseInt(o1.getPrice()) - Integer.parseInt(o2.getPrice()));
        }
    };
    String Tenanttype;
    String properttype;
    String Bhktype;
    String Address;
    String City;
    String Province;
    String Pincode;
    String price;
    String mobilenumber;
    String PID;

    public ArrayList<String> getPhotolist() {
        return photolist;
    }

    public void setPhotolist(ArrayList<String> photolist) {
        this.photolist = photolist;
    }

    ArrayList<String> photolist;

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    String OwnerID;
    String Availability;

    public String getTenanttype() {
        return Tenanttype;
    }

    public void setTenanttype(String tenanttype) {
        Tenanttype = tenanttype;
    }

    public String getProperttype() {
        return properttype;
    }

    public void setProperttype(String properttype) {
        this.properttype = properttype;
    }

    public String getBhktype() {
        return Bhktype;
    }

    public void setBhktype(String bhktype) {
        Bhktype = bhktype;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

}
