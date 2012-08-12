package com.zenika.cudf.parser;

import java.io.FileNotFoundException;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@zenika.com>
 */
public class ParsingException extends Exception {

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Exception cause) {
        super(message, cause);
    }
}
