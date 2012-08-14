package com.zenika.cudf.parser;

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.parser.model.ParsedBinary;

import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public abstract class AbstractTestParser {

    protected BinaryId binaryId1 = new BinaryId("jar1", "zenika", 1);
    protected BinaryId binaryId2 = new BinaryId("jar2", "zenika", 1);
    protected BinaryId binaryId3 = new BinaryId("jar3", "zenika", 2);

    protected Binary findBinaryByBinaryId(BinaryId binaryId, Set<Binary> binaries) {
        for (Binary binary : binaries) {
            if (binary.getBinaryId().equals(binaryId)) {
                return binary;
            }
        }
        return null;
    }

    protected ParsedBinary findParsedBinaryByBinaryId(BinaryId binaryId, Set<ParsedBinary> parsedBinaries) {
        for (ParsedBinary parsedBinary : parsedBinaries) {
            if (parsedBinary.getBinaryId().equals(binaryId)) {
                return parsedBinary;
            }
        }
        return null;
    }
}
