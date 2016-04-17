package com.mbds.material.PatrouilleNFC.Utils.Nfc;

/**
 * Created by Safidimahefa on 05/04/2016.
 */
public class NFCWriteException extends Exception {
    private static final long serialVersionUID = 4647185067874734143L;

    public enum NFCErrorType {
        ReadOnly, NoEnoughSpace, tagLost, formattingError, noNdefError, unknownError
    };

    NFCErrorType type;

    public NFCWriteException(NFCErrorType type) {
        super();
        this.type = type;
    }

    public NFCWriteException(NFCErrorType type, Exception e) {
        super(e);
        this.type = type;
    }


    public NFCErrorType getType() {
        return type;
    }
}
