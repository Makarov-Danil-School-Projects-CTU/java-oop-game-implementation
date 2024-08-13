package thedrake;

import java.io.PrintWriter;

public interface JSONSerializable {
    void toJSON(PrintWriter writer);

    default String JSONStringValue(String value) {
        return '"' + value + '"';
    }

    default String JSONKeyValue(String key) {
        return JSONStringValue(key) + ':';
    }
}
