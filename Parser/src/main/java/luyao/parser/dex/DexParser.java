package luyao.parser.dex;

import luyao.parser.dex.bean.*;
import luyao.parser.dex.bean.clazz.*;
import luyao.parser.utils.Reader;
import luyao.parser.utils.TransformUtils;
import luyao.parser.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static luyao.parser.utils.Reader.log;

public class DexParser {

    public static int POSITION = 0;
    private Dex dex;
    private Reader reader;
    private byte[] dexData;
    private List<DexString> dexStrings = new ArrayList<>();
    private List<DexType> dexTypes = new ArrayList<>();
    private List<DexProtoId> dexProtos = new ArrayList<>();
    private List<DexFieldId> dexFieldIds = new ArrayList<>();
    private List<DexMethodId> dexMethodIds = new ArrayList<>();
    private List<DexClass> dexClasses = new ArrayList<>();

    public DexParser(InputStream in, byte[] dexData) {
        this.dexData = dexData;
        reader = new Reader(in, true);
        dex = new Dex();
    }

    public void parse() {
        parseHeader();
        parseDexString();
        parseDexType();
        parseDexProto();
        parseDexField();
        parseDexMethod();
        parseDexClass();
    }

    private void parseHeader() {
        DexHeader dexHeader = new DexHeader(reader);
        dexHeader.parse();
        dex.setDexHeader(dexHeader);
    }

    private void parseDexString() {
        log("\nparse DexString");
        try {
            int stringIdsSize = dex.getDexHeader().string_ids__size;
            for (int i = 0; i < stringIdsSize; i++) {
                int string_data_off = reader.readInt();
                byte size = dexData[string_data_off];
                String string_data = new String(Utils.copy(dexData, string_data_off + 1, size));
                DexString string = new DexString(string_data_off, string_data);
                dexStrings.add(string);
                log("string[%d] data: %s", i, string.string_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDexType() {
        log("\nparse DexType");
        try {
            int typeIdsSize = dex.getDexHeader().type_ids__size;
            for (int i = 0; i < typeIdsSize; i++) {
                int descriptor_idx = reader.readInt();
                DexType dexType = new DexType(descriptor_idx, dexStrings.get(descriptor_idx).string_data);
                dexTypes.add(dexType);
                log("type[%d] data: %s", i, dexType.string_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDexProto() {
        log("\nparse DexProto");
        try {
            int protoIdsSize = dex.getDexHeader().proto_ids__size;
            for (int i = 0; i < protoIdsSize; i++) {
                int shorty_idx = reader.readInt();
                int return_type_idx = reader.readInt();
                int parameters_off = reader.readInt();

                DexProtoId dexProtoId = new DexProtoId(shorty_idx, return_type_idx, parameters_off);
                log("proto[%d]: %s %s %d", i, dexStrings.get(shorty_idx).string_data,
                        dexTypes.get(return_type_idx).string_data, parameters_off);

                if (parameters_off > 0) {
                    parseDexProtoParameters(parameters_off);
                }

                dexProtos.add(dexProtoId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDexProtoParameters(int parameters_off) {
        int size = TransformUtils.bytes2Int(Utils.copy(dexData, parameters_off, 4));
        for (int i = 0; i < size; i++) {
            int typeIdx = TransformUtils.bytes2UnsignedShort(Utils.copy(dexData, parameters_off + i * 2 + 4, 2));
            log("parameters[%d]: %s", i, dexTypes.get(typeIdx).string_data);
        }
    }

    private void parseDexField() {
        log("\nparse DexField");
        try {
            int fieldIdsSize = dex.getDexHeader().field_ids__size;
            for (int i = 0; i < fieldIdsSize; i++) {
                int class_idx = reader.readUnsignedShort();
                int type_idx = reader.readUnsignedShort();
                int name_idx = reader.readInt();
                DexFieldId dexFieldId = new DexFieldId(class_idx, type_idx, name_idx);
//                LHello;->HELLO_WORLD:Ljava/lang/String;
                log("field[%d]: %s->%s;%s", i, dexTypes.get(class_idx).string_data,
                        dexStrings.get(name_idx).string_data, dexTypes.get(type_idx).string_data);
                dexFieldIds.add(dexFieldId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDexMethod() {
        log("\nparse DexMethod");
        try {
            int methodIdsSize = dex.getDexHeader().method_ids_size;
            for (int i = 0; i < methodIdsSize; i++) {
                int class_idx = reader.readUnsignedShort();
                int proto_idx = reader.readUnsignedShort();
                int name_idx = reader.readInt();
                DexMethodId dexMethodId = new DexMethodId(class_idx, proto_idx, name_idx);
                log("method[%d]: %s proto[%d] %s", i, dexTypes.get(class_idx).string_data,
                        proto_idx, dexStrings.get(name_idx).string_data);
                dexMethodIds.add(dexMethodId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDexClass() {
        log("\nparse DexClass");
        try {
            int classDefsSize = dex.getDexHeader().class_defs_size;
            for (int i = 0; i < classDefsSize; i++) {
                int class_idx = reader.readInt();
                int access_flags = reader.readInt();
                int superclass_idx = reader.readInt();
                int interfaces_off = reader.readInt();
                int source_file_idx = reader.readInt();
                int annotations_off = reader.readInt();
                int class_data_off = reader.readInt();
                int staticValuesOff = reader.readInt();
                DexClass dexClass = new DexClass(class_idx, access_flags, superclass_idx,
                        interfaces_off, source_file_idx, annotations_off, class_data_off, staticValuesOff);
                log("class[%d]: %s", i, dexClass.toString());
                dexClasses.add(dexClass);

                log("   classIdx: %s", dexTypes.get(class_idx).string_data);
                log("   accessFlags: %d", access_flags);
                log("   superClassIdx: %s", dexTypes.get(superclass_idx).string_data);
                log("   interfaceOff: %d", interfaces_off);
                log("   sourceFileIdx: %s", dexStrings.get(source_file_idx).string_data);

                parseClassData(class_data_off);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseClassData(int class_data_off) {
        POSITION = class_data_off;
        int static_fields_size = Utils.readUnsignedLeb128(dexData, POSITION);
        int instance_fields_size = Utils.readUnsignedLeb128(dexData, POSITION);
        int direct_methods_size = Utils.readUnsignedLeb128(dexData, POSITION);
        int virtual_methods_size = Utils.readUnsignedLeb128(dexData, POSITION);

        DexClassData dexClassData = new DexClassData(static_fields_size, instance_fields_size, direct_methods_size, virtual_methods_size);
        log("   classData: %s", dexClassData.toString());

        // static field
        for (int i = 0; i < static_fields_size; i++) {
            int field_idx = Utils.readUnsignedLeb128(dexData, POSITION);
            int access_flags = Utils.readUnsignedLeb128(dexData, POSITION);
            EncodedField encodedField = new EncodedField(field_idx, access_flags);
            DexFieldId dexFieldId = dexFieldIds.get(field_idx);
            log("   static field[%d]: %s->%s;%s", i, dexTypes.get(dexFieldId.class_idx).string_data,
                    dexStrings.get(dexFieldId.name_idx).string_data, dexTypes.get(dexFieldId.type_idx).string_data);
        }

        // instance field
        for (int i = 0; i < instance_fields_size; i++) {
            int field_idx = Utils.readUnsignedLeb128(dexData, POSITION);
            int access_flags = Utils.readUnsignedLeb128(dexData, POSITION);
            EncodedField encodedField = new EncodedField(field_idx, access_flags);
            DexFieldId dexFieldId = dexFieldIds.get(field_idx);
            log("   instance field[%d]: %s->%s;%s", i, dexTypes.get(dexFieldId.class_idx).string_data,
                    dexStrings.get(dexFieldId.name_idx).string_data, dexTypes.get(dexFieldId.type_idx).string_data);
        }

        // direct method
        for (int i = 0; i < direct_methods_size; i++) {
            int method_idx = Utils.readUnsignedLeb128(dexData, POSITION);
            int access_flags = Utils.readUnsignedLeb128(dexData, POSITION);
            int code_off = Utils.readUnsignedLeb128(dexData, POSITION);

            EncodedMethod encodedMethod = new EncodedMethod(method_idx, access_flags, code_off);
            DexMethodId dexMethodId = dexMethodIds.get(method_idx);
            log("   direct method[%d]: %s proto[%d] %s", i, dexTypes.get(dexMethodId.class_idx).string_data,
                    dexMethodId.proto_idx, dexStrings.get(dexMethodId.name_idx).string_data);

            parseDexCode(code_off);
        }

        // virtual method
        for (int i = 0; i < virtual_methods_size; i++) {
            int method_idx = Utils.readUnsignedLeb128(dexData, POSITION);
            int access_flags = Utils.readUnsignedLeb128(dexData, POSITION);
            int code_off = Utils.readUnsignedLeb128(dexData, POSITION);

            EncodedMethod encodedMethod = new EncodedMethod(method_idx, access_flags, code_off);
            DexMethodId dexMethodId = dexMethodIds.get(method_idx);
            log("   virtual method[%d]: %s proto[%d] %s", i, dexTypes.get(dexMethodId.class_idx).string_data,
                    dexMethodId.proto_idx, dexStrings.get(dexMethodId.name_idx).string_data);

            parseDexCode(code_off);
        }
    }

    private void parseDexCode(int code_off) {
        int registers_size = TransformUtils.bytes2UnsignedShort(Utils.copy(dexData, code_off, 2));
        int ins_size = TransformUtils.bytes2UnsignedShort(Utils.copy(dexData, code_off + 2, 2));
        int outs_size = TransformUtils.bytes2UnsignedShort(Utils.copy(dexData, code_off + 4, 2));
        int tries_size = TransformUtils.bytes2UnsignedShort(Utils.copy(dexData, code_off + 6, 2));
        int debug_info_off = TransformUtils.bytes2Int(Utils.copy(dexData, code_off + 8, 4));
        int insns_size = TransformUtils.bytes2Int(Utils.copy(dexData, code_off + 12, 4));
        int[] insns = new int[insns_size];
        for (int i = 0; i < insns_size; i++) {
            int insns_ = TransformUtils.bytes2UnsignedShort(Utils.copy(dexData, code_off + 16 + i * 2, 2));
            insns[i] = insns_;
        }
        DexCode dexCode=new DexCode(registers_size,ins_size,outs_size,tries_size,debug_info_off,insns_size,insns);
        log("   dexcode: %s",dexCode.toString());
    }
}
