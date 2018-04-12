/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.hawk.service.api;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
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
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2018-04-12")
public class ModelSpec implements org.apache.thrift.TBase<ModelSpec, ModelSpec._Fields>, java.io.Serializable, Cloneable, Comparable<ModelSpec> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ModelSpec");

  private static final org.apache.thrift.protocol.TField URI_FIELD_DESC = new org.apache.thrift.protocol.TField("uri", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField METAMODEL_URIS_FIELD_DESC = new org.apache.thrift.protocol.TField("metamodelUris", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ModelSpecStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ModelSpecTupleSchemeFactory());
  }

  public String uri; // required
  public List<String> metamodelUris; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    URI((short)1, "uri"),
    METAMODEL_URIS((short)2, "metamodelUris");

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
        case 1: // URI
          return URI;
        case 2: // METAMODEL_URIS
          return METAMODEL_URIS;
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
    tmpMap.put(_Fields.URI, new org.apache.thrift.meta_data.FieldMetaData("uri", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.METAMODEL_URIS, new org.apache.thrift.meta_data.FieldMetaData("metamodelUris", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ModelSpec.class, metaDataMap);
  }

  public ModelSpec() {
  }

  public ModelSpec(
    String uri,
    List<String> metamodelUris)
  {
    this();
    this.uri = uri;
    this.metamodelUris = metamodelUris;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ModelSpec(ModelSpec other) {
    if (other.isSetUri()) {
      this.uri = other.uri;
    }
    if (other.isSetMetamodelUris()) {
      List<String> __this__metamodelUris = new ArrayList<String>(other.metamodelUris);
      this.metamodelUris = __this__metamodelUris;
    }
  }

  public ModelSpec deepCopy() {
    return new ModelSpec(this);
  }

  @Override
  public void clear() {
    this.uri = null;
    this.metamodelUris = null;
  }

  public String getUri() {
    return this.uri;
  }

  public ModelSpec setUri(String uri) {
    this.uri = uri;
    return this;
  }

  public void unsetUri() {
    this.uri = null;
  }

  /** Returns true if field uri is set (has been assigned a value) and false otherwise */
  public boolean isSetUri() {
    return this.uri != null;
  }

  public void setUriIsSet(boolean value) {
    if (!value) {
      this.uri = null;
    }
  }

  public int getMetamodelUrisSize() {
    return (this.metamodelUris == null) ? 0 : this.metamodelUris.size();
  }

  public java.util.Iterator<String> getMetamodelUrisIterator() {
    return (this.metamodelUris == null) ? null : this.metamodelUris.iterator();
  }

  public void addToMetamodelUris(String elem) {
    if (this.metamodelUris == null) {
      this.metamodelUris = new ArrayList<String>();
    }
    this.metamodelUris.add(elem);
  }

  public List<String> getMetamodelUris() {
    return this.metamodelUris;
  }

  public ModelSpec setMetamodelUris(List<String> metamodelUris) {
    this.metamodelUris = metamodelUris;
    return this;
  }

  public void unsetMetamodelUris() {
    this.metamodelUris = null;
  }

  /** Returns true if field metamodelUris is set (has been assigned a value) and false otherwise */
  public boolean isSetMetamodelUris() {
    return this.metamodelUris != null;
  }

  public void setMetamodelUrisIsSet(boolean value) {
    if (!value) {
      this.metamodelUris = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case URI:
      if (value == null) {
        unsetUri();
      } else {
        setUri((String)value);
      }
      break;

    case METAMODEL_URIS:
      if (value == null) {
        unsetMetamodelUris();
      } else {
        setMetamodelUris((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case URI:
      return getUri();

    case METAMODEL_URIS:
      return getMetamodelUris();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case URI:
      return isSetUri();
    case METAMODEL_URIS:
      return isSetMetamodelUris();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ModelSpec)
      return this.equals((ModelSpec)that);
    return false;
  }

  public boolean equals(ModelSpec that) {
    if (that == null)
      return false;

    boolean this_present_uri = true && this.isSetUri();
    boolean that_present_uri = true && that.isSetUri();
    if (this_present_uri || that_present_uri) {
      if (!(this_present_uri && that_present_uri))
        return false;
      if (!this.uri.equals(that.uri))
        return false;
    }

    boolean this_present_metamodelUris = true && this.isSetMetamodelUris();
    boolean that_present_metamodelUris = true && that.isSetMetamodelUris();
    if (this_present_metamodelUris || that_present_metamodelUris) {
      if (!(this_present_metamodelUris && that_present_metamodelUris))
        return false;
      if (!this.metamodelUris.equals(that.metamodelUris))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_uri = true && (isSetUri());
    list.add(present_uri);
    if (present_uri)
      list.add(uri);

    boolean present_metamodelUris = true && (isSetMetamodelUris());
    list.add(present_metamodelUris);
    if (present_metamodelUris)
      list.add(metamodelUris);

    return list.hashCode();
  }

  @Override
  public int compareTo(ModelSpec other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetUri()).compareTo(other.isSetUri());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUri()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.uri, other.uri);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMetamodelUris()).compareTo(other.isSetMetamodelUris());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMetamodelUris()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.metamodelUris, other.metamodelUris);
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
    StringBuilder sb = new StringBuilder("ModelSpec(");
    boolean first = true;

    sb.append("uri:");
    if (this.uri == null) {
      sb.append("null");
    } else {
      sb.append(this.uri);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("metamodelUris:");
    if (this.metamodelUris == null) {
      sb.append("null");
    } else {
      sb.append(this.metamodelUris);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (uri == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'uri' was not present! Struct: " + toString());
    }
    if (metamodelUris == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'metamodelUris' was not present! Struct: " + toString());
    }
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

  private static class ModelSpecStandardSchemeFactory implements SchemeFactory {
    public ModelSpecStandardScheme getScheme() {
      return new ModelSpecStandardScheme();
    }
  }

  private static class ModelSpecStandardScheme extends StandardScheme<ModelSpec> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ModelSpec struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // URI
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.uri = iprot.readString();
              struct.setUriIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // METAMODEL_URIS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list82 = iprot.readListBegin();
                struct.metamodelUris = new ArrayList<String>(_list82.size);
                String _elem83;
                for (int _i84 = 0; _i84 < _list82.size; ++_i84)
                {
                  _elem83 = iprot.readString();
                  struct.metamodelUris.add(_elem83);
                }
                iprot.readListEnd();
              }
              struct.setMetamodelUrisIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ModelSpec struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.uri != null) {
        oprot.writeFieldBegin(URI_FIELD_DESC);
        oprot.writeString(struct.uri);
        oprot.writeFieldEnd();
      }
      if (struct.metamodelUris != null) {
        oprot.writeFieldBegin(METAMODEL_URIS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.metamodelUris.size()));
          for (String _iter85 : struct.metamodelUris)
          {
            oprot.writeString(_iter85);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ModelSpecTupleSchemeFactory implements SchemeFactory {
    public ModelSpecTupleScheme getScheme() {
      return new ModelSpecTupleScheme();
    }
  }

  private static class ModelSpecTupleScheme extends TupleScheme<ModelSpec> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ModelSpec struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.uri);
      {
        oprot.writeI32(struct.metamodelUris.size());
        for (String _iter86 : struct.metamodelUris)
        {
          oprot.writeString(_iter86);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ModelSpec struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.uri = iprot.readString();
      struct.setUriIsSet(true);
      {
        org.apache.thrift.protocol.TList _list87 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
        struct.metamodelUris = new ArrayList<String>(_list87.size);
        String _elem88;
        for (int _i89 = 0; _i89 < _list87.size; ++_i89)
        {
          _elem88 = iprot.readString();
          struct.metamodelUris.add(_elem88);
        }
      }
      struct.setMetamodelUrisIsSet(true);
    }
  }

}

