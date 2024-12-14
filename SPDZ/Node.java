public interface Node {
    Share getShare();
    void setLeftChild(Node node);
    void setRightChild(Node node);
    Share getd();
    Share gete();
    FieldValue getSumDMAC(FieldValue Dsum);
    FieldValue getSumEMAC(FieldValue Dsum);
    FieldValue getMacShareCheckD(Share sumOfd);
    FieldValue getMacShareCheckE(Share sumOfe);
}
