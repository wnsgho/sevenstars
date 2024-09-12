package deu.ex.sevenstars.exception;

public enum ProductException {
    NOT_FOUND("Product NOT_FOUND", 400),
    NOT_REGISTERED("Product NOT_REGISTERED", 400),
    NOT_MODIFIED("Product NOT_MODIFIED", 400),
    NOT_REMOVED("Product NOT_REMOVED", 400),
    NOT_FETCHED("Product NOT_FETCHED", 400),
    NO_IMAGE("Product NO_IMAGE", 400),
    REGISTER_ERR("NO AUTHENTICATED_USER",403);

    private ProductTaskException productTaskException;

    ProductException(String message,int code){
        productTaskException = new ProductTaskException(message,code);
    }

    public ProductTaskException get(){
        return productTaskException;
    }

}
