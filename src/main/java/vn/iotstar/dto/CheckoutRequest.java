package vn.iotstar.dto;

public class CheckoutRequest {

    private String receiverName;

    private String phone;

    private String address;


    public CheckoutRequest() {
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(
            String receiverName) {

        this.receiverName =
                receiverName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone) {

        this.phone =
                phone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(
            String address) {

        this.address =
                address;
    }
}