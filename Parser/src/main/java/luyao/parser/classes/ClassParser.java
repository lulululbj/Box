package luyao.parser.classes;



import luyao.parser.classes.bean.*;
import luyao.parser.utils.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static luyao.parser.utils.Reader.log;




/**
 * Created by luyao
 * on 2018/8/27 11:11
 */
public class ClassParser {

    private Reader reader;

    public ClassParser(InputStream in) {
        reader = new Reader(in, false);
    }

    public void parse() {
        parseHeader();
        parseConstantPool();
        parseOther();
    }

    private void parseHeader() {
        try {
            String magic = reader.readHexString(4);
            log("magic: %s", magic);

            int minor_version = reader.readUnsignedShort();
            log("minor_version: %d", minor_version);

            int major_version = reader.readUnsignedShort();
            log("major_version: %d", major_version);
        } catch (IOException e) {
            log("Parser header error:%s", e.getMessage());
        }
    }

    private void parseConstantPool() {
        try {
            int constant_pool_count = reader.readUnsignedShort();
            log("constant_pool_count: %d", constant_pool_count);

            for (int i = 0; i < constant_pool_count - 1; i++) {

                int tag = reader.readUnsignedByte();
                switch (tag) {
                    case ConstantTag.METHOD_REF:
                        ConstantMethodref methodRef = new ConstantMethodref();
                        methodRef.read(reader);
                        log("%s", methodRef.toString());
                        break;

                    case ConstantTag.FIELD_REF:
                        ConstantFieldRef fieldRef = new ConstantFieldRef();
                        fieldRef.read(reader);
                        log("%s", fieldRef.toString());
                        break;

                    case ConstantTag.STRING:
                        ConstantString string = new ConstantString();
                        string.read(reader);
                        log("%s", string.toString());
                        break;

                    case ConstantTag.CLASS:
                        ConstantClass clazz = new ConstantClass();
                        clazz.read(reader);
                        log("%s", clazz.toString());
                        break;

                    case ConstantTag.UTF8:
                        ConstantUtf8 utf8 = new ConstantUtf8();
                        utf8.read(reader);
                        log("%s", utf8.toString());
                        break;

                    case ConstantTag.NAME_AND_TYPE:
                        ConstantNameAndType nameAndType = new ConstantNameAndType();
                        nameAndType.read(reader);
                        log("%s", nameAndType.toString());
                        break;

                }

            }
        } catch (IOException e) {
            log("Parser constant pool error:%s", e.getMessage());
        }
    }

    private void parseOther() {
        try {
            int access_flags = reader.readUnsignedShort();
            log("access_flags: %d", access_flags);

            int this_class = reader.readUnsignedShort();
            log("this_class: %d", this_class);

            int super_class = reader.readUnsignedShort();
            log("super_class: %d", super_class);

            int interfaces_count = reader.readUnsignedShort();
            log("interfaces_count: %d", interfaces_count);

            // TODO  parse interfaces[]

            int fields_count = reader.readUnsignedShort();
            log("fields_count: %d", fields_count);

            List<Field> fieldList=new ArrayList<>();
            for (int i = 0; i < fields_count; i++) {
                Field field=new Field();
                field.read(reader);
                fieldList.add(field);
                log(field.toString());
            }

            int method_count=reader.readUnsignedShort();
            log("method_count: %d", method_count);

            List<Method> methodList=new ArrayList<>();
            for (int i=0;i<method_count;i++){
                Method method=new Method();
                method.read(reader);
                methodList.add(method);
                log(method.toString());
            }

            int attribute_count=reader.readUnsignedShort();
            log("attribute_count: %d", attribute_count);

            List<Attribute> attributeList = new ArrayList<>();
            for (int i = 0; i < attribute_count; i++) {
                Attribute attribute=new Attribute();
                attribute.read(reader);
                attributeList.add(attribute);
                log(attribute.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

