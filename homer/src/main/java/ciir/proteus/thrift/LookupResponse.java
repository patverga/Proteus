/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package ciir.proteus.thrift;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LookupResponse implements org.apache.thrift.TBase<LookupResponse, LookupResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("LookupResponse");

  private static final org.apache.thrift.protocol.TField OBJECTS_FIELD_DESC = new org.apache.thrift.protocol.TField("objects", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new LookupResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new LookupResponseTupleSchemeFactory());
  }

  public List<ProteusObject> objects; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    OBJECTS((short)1, "objects");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // OBJECTS
          return OBJECTS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.OBJECTS, new org.apache.thrift.meta_data.FieldMetaData("objects", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ProteusObject.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(LookupResponse.class, metaDataMap);
  }

  public LookupResponse() {
  }

  public LookupResponse(
    List<ProteusObject> objects)
  {
    this();
    this.objects = objects;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public LookupResponse(LookupResponse other) {
    if (other.isSetObjects()) {
      List<ProteusObject> __this__objects = new ArrayList<ProteusObject>();
      for (ProteusObject other_element : other.objects) {
        __this__objects.add(new ProteusObject(other_element));
      }
      this.objects = __this__objects;
    }
  }

  public LookupResponse deepCopy() {
    return new LookupResponse(this);
  }

  @Override
  public void clear() {
    this.objects = null;
  }

  public int getObjectsSize() {
    return (this.objects == null) ? 0 : this.objects.size();
  }

  public java.util.Iterator<ProteusObject> getObjectsIterator() {
    return (this.objects == null) ? null : this.objects.iterator();
  }

  public void addToObjects(ProteusObject elem) {
    if (this.objects == null) {
      this.objects = new ArrayList<ProteusObject>();
    }
    this.objects.add(elem);
  }

  public List<ProteusObject> getObjects() {
    return this.objects;
  }

  public LookupResponse setObjects(List<ProteusObject> objects) {
    this.objects = objects;
    return this;
  }

  public void unsetObjects() {
    this.objects = null;
  }

  /** Returns true if field objects is set (has been assigned a value) and false otherwise */
  public boolean isSetObjects() {
    return this.objects != null;
  }

  public void setObjectsIsSet(boolean value) {
    if (!value) {
      this.objects = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case OBJECTS:
      if (value == null) {
        unsetObjects();
      } else {
        setObjects((List<ProteusObject>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case OBJECTS:
      return getObjects();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case OBJECTS:
      return isSetObjects();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof LookupResponse)
      return this.equals((LookupResponse)that);
    return false;
  }

  public boolean equals(LookupResponse that) {
    if (that == null)
      return false;

    boolean this_present_objects = true && this.isSetObjects();
    boolean that_present_objects = true && that.isSetObjects();
    if (this_present_objects || that_present_objects) {
      if (!(this_present_objects && that_present_objects))
        return false;
      if (!this.objects.equals(that.objects))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_objects = true && (isSetObjects());
    builder.append(present_objects);
    if (present_objects)
      builder.append(objects);

    return builder.toHashCode();
  }

  public int compareTo(LookupResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    LookupResponse typedOther = (LookupResponse)other;

    lastComparison = Boolean.valueOf(isSetObjects()).compareTo(typedOther.isSetObjects());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetObjects()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.objects, typedOther.objects);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("LookupResponse(");
    boolean first = true;

    sb.append("objects:");
    if (this.objects == null) {
      sb.append("null");
    } else {
      sb.append(this.objects);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class LookupResponseStandardSchemeFactory implements SchemeFactory {
    public LookupResponseStandardScheme getScheme() {
      return new LookupResponseStandardScheme();
    }
  }

  private static class LookupResponseStandardScheme extends StandardScheme<LookupResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, LookupResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // OBJECTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list170 = iprot.readListBegin();
                struct.objects = new ArrayList<ProteusObject>(_list170.size);
                for (int _i171 = 0; _i171 < _list170.size; ++_i171)
                {
                  ProteusObject _elem172; // required
                  _elem172 = new ProteusObject();
                  _elem172.read(iprot);
                  struct.objects.add(_elem172);
                }
                iprot.readListEnd();
              }
              struct.setObjectsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, LookupResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.objects != null) {
        oprot.writeFieldBegin(OBJECTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.objects.size()));
          for (ProteusObject _iter173 : struct.objects)
          {
            _iter173.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class LookupResponseTupleSchemeFactory implements SchemeFactory {
    public LookupResponseTupleScheme getScheme() {
      return new LookupResponseTupleScheme();
    }
  }

  private static class LookupResponseTupleScheme extends TupleScheme<LookupResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, LookupResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetObjects()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetObjects()) {
        {
          oprot.writeI32(struct.objects.size());
          for (ProteusObject _iter174 : struct.objects)
          {
            _iter174.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, LookupResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list175 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.objects = new ArrayList<ProteusObject>(_list175.size);
          for (int _i176 = 0; _i176 < _list175.size; ++_i176)
          {
            ProteusObject _elem177; // required
            _elem177 = new ProteusObject();
            _elem177.read(iprot);
            struct.objects.add(_elem177);
          }
        }
        struct.setObjectsIsSet(true);
      }
    }
  }

}
