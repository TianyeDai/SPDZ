/*
 * Hold a share value and its MAC.
 */
public class Share {
    private final FieldValue shareValue;
    private final FieldValue MAC;

    public Share(FieldValue shareValue, FieldValue MAC){
        this.shareValue = shareValue;
        this.MAC = MAC;
    }

    public FieldValue getShareValue(){
        return shareValue;
    }

    public FieldValue getMAC(){
        return MAC;
    }

    public Share add(Share share){
        FieldValue newValue = shareValue.add(share.getShareValue());
        FieldValue newMAC = MAC.add(share.getMAC());
        return new Share(newValue, newMAC);
    }

    public Share subtract(Share share){
        FieldValue newValue = shareValue.subtract(share.getShareValue());
        FieldValue newMAC = MAC.subtract(share.getMAC());
        return new Share(newValue, newMAC);
    }

    public Share multiply(Share share){
        FieldValue newValue = shareValue.multiply(share.getShareValue());
        FieldValue newMAC = MAC.multiply(share.getShareValue());
        return new Share(newValue, newMAC);
    }
}
