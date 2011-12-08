
package com.soacookbook.ns.ws.catalog;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for category.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="category">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LITERATURE"/>
 *     &lt;enumeration value="PHILOSOPHY"/>
 *     &lt;enumeration value="PROGRAMMING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "category")
@XmlEnum
public enum Category {

    LITERATURE,
    PHILOSOPHY,
    PROGRAMMING;

    public String value() {
        return name();
    }

    public static Category fromValue(String v) {
        return valueOf(v);
    }

}
