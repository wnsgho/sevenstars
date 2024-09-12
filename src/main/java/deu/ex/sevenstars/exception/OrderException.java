package deu.ex.sevenstars.exception;

public enum OrderException {
    NOT_FOUND("Order NOT_FOUND", 400),
    NOT_REGISTERED("Order NOT_REGISTERED", 400),
    NOT_MODIFIED("Order NOT_MODIFIED", 400),
    NOT_REMOVED("Order NOT_REMOVED", 400),
    NOT_FETCHED("Order NOT_FETCHED", 400),
    NO_IMAGE("Order NO_IMAGE", 400),
    REGISTER_ERR("NO AUTHENTICATED_USER",403);

    private OrderTaskException orderTaskException;

    OrderException(String message, int code){
        orderTaskException = new OrderTaskException(message,code);
    }

    public OrderTaskException get(){
        return orderTaskException;
    }

}
