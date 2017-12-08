package io.nuls.core.chain.entity;

import io.nuls.core.crypto.VarInt;
import io.nuls.core.utils.crypto.Utils;
import io.nuls.core.utils.io.NulsByteBuffer;
import io.nuls.core.utils.io.NulsOutputStreamBuffer;

import java.io.IOException;

/**
 * @author Niels
 * @date 2017/12/7
 */
public class BasicTypeData<T> extends BaseNulsData {

    private int type;

    private T val;

    public BasicTypeData(T data) {
        this.val = data;
        this.type = getType();
    }
    public BasicTypeData(NulsByteBuffer buffer) {
        this.parse(buffer);
    }
    @Override
    public int size() {
        int size = 1;
        switch (type) {
            case 1:
                size += Utils.sizeOfString((String) val);
                break;
            case 2:
                size += VarInt.sizeOf((Long) val);
                break;
            case 3:
                size += VarInt.sizeOf((Integer) val);
                break;
            case 4:
                size += Utils.sizeOfDouble((Double) val);
                break;
            case 5:
                size += VarInt.sizeOf((Short) val);
                break;
            case 6:
                size++;
                break;
            case 7:
                int length = ((byte[]) val).length;
                size += VarInt.sizeOf(length) + length;
                break;
            default:
                break;
        }
        return size;
    }

    @Override
    public void serializeToStream(NulsOutputStreamBuffer stream) throws IOException {
        stream.write(type);
        switch (type) {
            case 1:
                stream.writeBytesWithLength((String) val);
                break;
            case 2:
                stream.writeVarInt((Long) val);
                break;
            case 3:
                stream.writeVarInt((Integer) val);
                break;
            case 4:
                stream.writeDouble((Double) val);
                break;
            case 5:
                stream.writeShort((Short) val);
                break;
            case 6:
                stream.writeBoolean((Boolean) val);
                break;
            case 7:
                stream.write((byte[]) val);
                break;
            default:
                break;
        }
    }

    @Override
    public void parse(NulsByteBuffer byteBuffer) {
        this.type = byteBuffer.readBytes(1)[0];
        switch (type) {
            case 1:
                this.val = (T) byteBuffer.readString();
                break;
            case 2:
                this.val = (T) ((Object) byteBuffer.readVarInt());
                break;
            case 3:
                this.val = (T) ((Object) byteBuffer.readVarInt());
                break;
            case 4:
                this.val = (T) ((Object) byteBuffer.readDouble());
                break;
            case 5:
                this.val = (T) ((Object) byteBuffer.readVarInt());
                break;
            case 6:
                this.val = (T) ((Object) byteBuffer.readBoolean());
                break;
            case 7:
                this.val = (T) byteBuffer.readByLengthByte();
                break;
            default:
                break;
        }
    }

    public T getVal() {
        return val;
    }

    public int getType() {
        if (val instanceof String) {
            return 1;
        } else if (val instanceof Long) {
            return 2;
        } else if (val instanceof Integer) {
            return 3;
        } else if (val instanceof Double) {
            return 4;
        } else if (val instanceof Short) {
            return 5;
        } else if (val instanceof Boolean) {
            return 6;
        } else if (val instanceof byte[]) {
            return 7;
        } else {
            return 8;
        }
    }
}
