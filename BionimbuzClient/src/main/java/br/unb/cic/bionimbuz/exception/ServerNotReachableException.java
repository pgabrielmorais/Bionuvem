package br.unb.cic.bionimbuz.exception;

public class ServerNotReachableException extends Exception {

    private static final long serialVersionUID = 7954263947817153967L;

    public ServerNotReachableException(String message) {
        super(message);
    }

    public ServerNotReachableException(Throwable cause) {
        super(cause);
    }

    public ServerNotReachableException(String message, Throwable cause) {
        super(message, cause);
    }
}
